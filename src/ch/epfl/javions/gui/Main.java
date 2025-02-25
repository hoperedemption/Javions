package ch.epfl.javions.gui;

import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.adsb.MessageParser;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public final class Main extends Application {
    private final ObjectProperty<ObservableAircraftState> sap = new SimpleObjectProperty<>();
    private final long second = TimeUnit.SECONDS.toNanos(1);
    private long nextSecond = 0;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        long start = System.nanoTime();

        Path tileCache = Path.of("tile-cache");
        TileManager tm = new TileManager(tileCache, "tile.openstreetmap.org");
        MapParameters mp = new MapParameters(8, 33530, 23070);
        BaseMapController bmc = new BaseMapController(tm, mp);

        URL dbUrl = getClass().getResource("/aircraft.zip");
        assert dbUrl != null;
        String f = Path.of(dbUrl.toURI()).toString();
        AircraftDatabase db = new AircraftDatabase(f);

        AircraftStateManager asm = new AircraftStateManager(db);
        ObjectProperty<ObservableAircraftState> sap = new SimpleObjectProperty<>();

        AircraftController ac =
                new AircraftController(mp, (ObservableSet<ObservableAircraftState>) asm.states(), sap, bmc);

        StackPane stackPane = new StackPane(bmc.pane(), ac.pane());

        StatusLineController statusLineController = new StatusLineController();
        statusLineController.aircraftCountProperty.bind(
                Bindings.size((ObservableSet<ObservableAircraftState>) asm.states()));
        AircraftTableController aircraftTableController =
                new AircraftTableController((ObservableSet<ObservableAircraftState>) asm.states(), sap);
        aircraftTableController.setOnDoubleClick(v -> bmc.centerOn(v.getPosition()));

        BorderPane tableAndLine = new
                BorderPane(aircraftTableController.pane(), statusLineController.pane(), null, null, null);

        SplitPane splitPane = new SplitPane(stackPane, tableAndLine);
        splitPane.setOrientation(Orientation.VERTICAL);

        primaryStage.setTitle("Javions");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        primaryStage.setScene(new Scene(splitPane));
        primaryStage.show();

        ConcurrentLinkedQueue<RawMessage> messageConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        List<String> parameters = getParameters().getRaw();

        Thread gettingMessage = new Thread(() -> {
            if(parameters.isEmpty()){
                try {
                    AdsbDemodulator demodulator = new AdsbDemodulator(System.in);
                    RawMessage rawMessage;
                    if((rawMessage = demodulator.nextMessage()) != null) messageConcurrentLinkedQueue.add(rawMessage);
                }  catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try (DataInputStream s = new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(parameters.get(0))))) {
                    byte[] bytes = new byte[RawMessage.LENGTH];
                    while (true) {
                        long timeStampNs = s.readLong();
                        int bytesRead = s.readNBytes(bytes, 0, bytes.length);
                        assert bytesRead == RawMessage.LENGTH;
                        RawMessage rawMessage = RawMessage.of(timeStampNs, bytes);
                        if(rawMessage != null) {
                            long timeElapsed = System.nanoTime() - start;
                            if(timeStampNs > timeElapsed) {
                                Thread.sleep((timeStampNs - timeElapsed)/1_000_000);
                            }
                            messageConcurrentLinkedQueue.add(rawMessage);
                        }
                    }
                }catch (IOException | InterruptedException ignored) {

                }
            }
        });
        gettingMessage.setDaemon(true);
        gettingMessage.start();


        new AnimationTimer() {
            @Override
            public void handle(long now) {
                    try {
                        if (messageConcurrentLinkedQueue.isEmpty()) return;
                        while(!messageConcurrentLinkedQueue.isEmpty()) {
                            Message message = MessageParser.parse(messageConcurrentLinkedQueue.remove());

                            if (Objects.nonNull(message)) {
                                asm.updateWithMessage(message);
                                statusLineController.messageCountProperty()
                                        .set(statusLineController.messageCountProperty().get() + 1);
                            }
                        }
                        if(now >= nextSecond) {
                            nextSecond = now + second;
                            asm.purge();
                        }
                    } catch (Exception ignored) {
                        /*nothing to do*/
                    }
            }
        }.start();
    }
}
