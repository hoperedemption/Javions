package ch.epfl.javions.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Path;

public final class BaseMapControllerTest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        Path tileCache = Path.of("tile-cache");
        TileManager tm =
                new TileManager(tileCache, "tile.openstreetmap.org");
        MapParameters mp =
                new MapParameters(17, 17_389_327, 11_867_430);

        BaseMapController bmc = new BaseMapController(tm, mp);
        primaryStage.setScene(new Scene(bmc.pane()));
        primaryStage.show();

    }
}