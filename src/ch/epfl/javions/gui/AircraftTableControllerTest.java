package ch.epfl.javions.gui;

import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.adsb.MessageParser;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.AircraftDatabase;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AircraftTableControllerTest extends Application {
    public static void main(String[] args) { launch(args); }

    static List<RawMessage> readAllMessages(String fileName) throws IOException {
        List<RawMessage> rawMessageList = new ArrayList<>();
        try (DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream("resources/messages_20230318_0915.bin")))){
            byte[] bytes = new byte[RawMessage.LENGTH];
            while (true) {
                long timeStampNs = s.readLong();
                int bytesRead = s.readNBytes(bytes, 0, bytes.length);
                assert bytesRead == RawMessage.LENGTH;
                RawMessage rawMessage = RawMessage.of(timeStampNs, bytes);
                if(rawMessage != null) rawMessageList.add(rawMessage);
            }
        } catch (EOFException e) {
            System.out.println("Insane Crack ::: nothing to do");
        }
        return rawMessageList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Création de la base de données
        URL dbUrl = getClass().getResource("/aircraft.zip");
        assert dbUrl != null;
        String f = Path.of(dbUrl.toURI()).toString();
        var db = new AircraftDatabase(f);

        AircraftStateManager asm = new AircraftStateManager(db);
        ObjectProperty<ObservableAircraftState> sap =
                new SimpleObjectProperty<>();
        AircraftTableController aircraftTableController =
                new AircraftTableController((ObservableSet<ObservableAircraftState>) asm.states(), sap);
        var root = new StackPane(aircraftTableController.pane());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        var mi = readAllMessages("resources/messages_20230318_0915.bin")
                .iterator();

        // Animation des aéronefs
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    for (int i = 0; i < 10; i += 1) {
                        if(mi.hasNext()) {
                            Message m = MessageParser.parse(mi.next());
                            if (m != null) asm.updateWithMessage(m);
                        }
                    }
                    asm.purge();

                } catch (IOException e) {
                    System.out.println("Amogus WTF");
                }
            }
        }.start();

        b();
    }

    private String getJsonTable(ObservableAircraftState state) throws IOException {
        URL u = new URL("https://api.planespotters.net/pub/photos/reg/"
                + state.getAircraftData().registration().string());
        try(InputStream inputStream = u.openStream()){
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while((c = br.read()) != -1){
                json.append((char) c);
            }
            return json.toString();
        }

        /*Pattern pattern = Pattern.compile("https:\\\\/\\\\/t\\.plnspttrs\\.net\\\\/[1-9]\\\\/[1-9a-z_]\\.jpg");

        //https:\/\/t.plnspttrs.net\/08717\/1418881_7976edea78_t.jpg

        URLConnection c = u.openConnection();
        c.setRequestProperty("User-Agent", "Javions");
        return c;*/
    }

    private void a(String s){

        Pattern pattern = Pattern.compile("https:\\/\\/t.plnspttrs.net\\/[1-9]\\/[1-9a-z_].jpg");
        Matcher matcher = pattern.matcher(s);
        if(matcher.find()){
            String result = matcher.group(1);
            System.out.println(result);
        }
    }

    private void b() throws Exception{
        URL u = new URL("https://api.planespotters.net/pub/photos/reg/"
                + "HB-JDC");
        String r;
        try(InputStream inputStream = u.openStream()) {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = br.read()) != -1) {

                if(json.toString().length() > 3 &&
                        json.toString().substring(json.toString().length()-3).equals("src")) {
                    br.skip(2);
                    json = new StringBuilder();
                    break;
                }
                json.append((char) c);

            }

            while(((c = br.read()) != -1)){
                if(json.toString().length() > 3
                        && json.toString().substring(json.toString().length()-3).equals("jpg")){
                     break;
                }
                json.append((char) c);
            }

            r = json.toString();
        }

        URL url = new URL(r);
        byte[] memory = new BufferedInputStream(url.openStream()).readAllBytes();
        InputStream stream = new ByteArrayInputStream(memory);
        Image image = new Image(stream);

    }


}
