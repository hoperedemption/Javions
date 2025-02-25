package ch.epfl.javions.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public final class StatusLineController {

    BorderPane borderPane;
    IntegerProperty aircraftCountProperty;
    LongProperty messageCountProperty;


    public StatusLineController() {

        aircraftCountProperty = new SimpleIntegerProperty(0);
        messageCountProperty = new SimpleLongProperty(0);

        Text visibleAircrafts = new Text();
        Text messageText = new Text();

        visibleAircrafts.textProperty().bind(Bindings.createStringBinding(() ->
            "Aéronefs visibles : " + aircraftCountProperty.get(), aircraftCountProperty
        ));
        messageText.textProperty().bind(Bindings.createStringBinding(() ->
                "Messages reçus : " + messageCountProperty.get(), messageCountProperty));

        borderPane = new BorderPane(null, null, messageText, null, visibleAircrafts);
        borderPane.getStylesheets().add("status.css");

    }

    public BorderPane pane() {
        return borderPane;
    }

    public IntegerProperty aircraftCountProperty() {
        return aircraftCountProperty;
    }

    public LongProperty messageCountProperty() {
        return messageCountProperty;
    }
}
