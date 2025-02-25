package ch.epfl.javions.gui;

import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.AircraftData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class AircraftTableController {
    private final ObjectProperty<ObservableAircraftState> stateOfSelectedAircraft;
    private TableView<ObservableAircraftState> tableView;
    private Consumer<ObservableAircraftState> consumer;
    private NumberFormat numberFormat;
    private final Comparator<String> comparator = (s, u) -> {
        if(s == null || u  == null) {
            return s == null ? -1 : 1;
        } else{
            try {
                double doubleS = numberFormat.parse(s).doubleValue();
                double doubleU = numberFormat.parse(u).doubleValue();

                return Double.compare(doubleS, doubleU);
            } catch (ParseException ignored) {return 0;}
        }
    };

    /**
     * The constructor of the table. It creates the text columns and the numeric columns.
     * @param setOfAircraftStates (ObservableSet<ObservableAircraftState>) : the set of the observable aircrafts
     * @param stateOfSelectedAircraft (ObjectProperty<ObservableAircraftState>) : the state of the selected aircraft
     */
    public AircraftTableController(ObservableSet<ObservableAircraftState> setOfAircraftStates,
                                   ObjectProperty<ObservableAircraftState> stateOfSelectedAircraft){
        this.stateOfSelectedAircraft = stateOfSelectedAircraft;
        this.consumer = ignored -> {};

        createTableView();

        createTextColumns();
        createNumericColumns();
        createImageColumn();

        addListenerToAircraftStates(setOfAircraftStates);
        setSelectionModel(stateOfSelectedAircraft);
    }

    /**
     * This function initializes all the text columns and adds them to the main table.
     */
    private void createTextColumns() {
        TableColumn<ObservableAircraftState, String> icaoAdressColumn = setUpTextColumn("OACI",
                v -> new ReadOnlyObjectWrapper<>(v.getIcaoAddress().string()), 60);
        TableColumn<ObservableAircraftState, String> callSignColumn = setUpTextColumn("Indicatif",
                v -> v.callSignProperty().map(CallSign::string), 70);
        TableColumn<ObservableAircraftState, String> aircraftRegistrationColumn = setUpTextColumn("Immatriculation",
                v -> new ReadOnlyObjectWrapper<>(v.getAircraftData()).map(val -> val.registration().string()), 90);
        TableColumn<ObservableAircraftState, String> modelColumn = setUpTextColumn("Modèle", v ->
                        new ReadOnlyObjectWrapper<>(v.getAircraftData()).map(AircraftData::model) , 230);
        TableColumn<ObservableAircraftState, String> typeColumn = setUpTextColumn("Type", v ->
                new ReadOnlyObjectWrapper<>(v.getAircraftData()).map(val -> val.typeDesignator().string()), 50);
        TableColumn<ObservableAircraftState, String> aircraftDescriptionColumn = setUpTextColumn("Description",
                v -> new ReadOnlyObjectWrapper<>(v.getAircraftData()).map(val -> val.description().string()), 70);

        tableView.getColumns().add(icaoAdressColumn);
        tableView.getColumns().add(callSignColumn);
        tableView.getColumns().add(aircraftRegistrationColumn);
        tableView.getColumns().add(modelColumn);
        tableView.getColumns().add(typeColumn);
        tableView.getColumns().add(aircraftDescriptionColumn);
    }

    /**
     * This function initializes all the numeric columns and adds them to the main table.
     */
    private void createNumericColumns() {
        TableColumn<ObservableAircraftState, String> longitudeColumn = createNumericColumn("Longitude (°)",
                v -> Bindings.createDoubleBinding(()
                        -> v.geoPosProperty().getValue().longitude(), v.geoPosProperty()),
                4, Units.Angle.DEGREE);
        TableColumn<ObservableAircraftState, String> latitudeColumn = createNumericColumn("Latitude (°)",
                v -> Bindings.createDoubleBinding(()
                                -> v.geoPosProperty().getValue().latitude()
                        , v.geoPosProperty()),4, Units.Angle.DEGREE);
        TableColumn<ObservableAircraftState, String> altitudeColumn = createNumericColumn("Altitude (m)",
                ObservableAircraftState::altitudeProperty, 0, Units.Length.METER);
        TableColumn<ObservableAircraftState, String> velocityColumn = createNumericColumn("Vitesse (km/h)",
                ObservableAircraftState::velocityProprety, 0, Units.Speed.KILOMETER_PER_HOUR);

        tableView.getColumns().add(longitudeColumn);
        tableView.getColumns().add(latitudeColumn);
        tableView.getColumns().add(altitudeColumn);
        tableView.getColumns().add(velocityColumn);
    }

    /**
     * This function initializes the image column and adds it to the main table.
     */
    private void createImageColumn() {
        TableColumn<ObservableAircraftState, ImageView> imageColumn = new TableColumn<>("Images");
        imageColumn.setMinWidth(200);
        imageColumn.setCellValueFactory(v -> {
            try {
                return new ReadOnlyObjectWrapper<>(getCustomImage(v.getValue()));
            } catch (IOException ignored) {
            }
            return null;
        });
        tableView.getColumns().add(imageColumn);
    }

    /**
     * This function gives us the NumberFormat that is used for the comparisons depending on the number of digits that
     * we want after the decimal point.
     * @param decimalPlaces (int) : the number of digits after the decimal point
     * @return (NumberFormat) : the number format used for the comparisons
     */
    private NumberFormat getNumberFormat(int decimalPlaces) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("fr"));
        numberFormat.setMinimumFractionDigits(decimalPlaces);
        numberFormat.setMaximumFractionDigits(decimalPlaces);
        this.numberFormat = numberFormat;
        return numberFormat;
    }

    /**
     * This function detects if there was a change in the set of the observable aircraft. Then, if an aircraft was
     * added, it will and its information to the table and resort the whole table according to a default comparator.
     * If an element was removed, it simply deletes the line.
     * @param setOfAircraftStates (ObservableSet<ObservableAircraftState>) : the set of observable aircraft states
     */
    private void addListenerToAircraftStates(ObservableSet<ObservableAircraftState> setOfAircraftStates) {
        setOfAircraftStates.addListener((SetChangeListener<ObservableAircraftState>) change -> {
            if(change.wasAdded()) {
                tableView.getItems().add(change.getElementAdded());
                tableView.sort();
            } else if(change.wasRemoved()) {
                tableView.getItems().remove(change.getElementRemoved());
            }
        });
    }

    /**
     * This function manages the click on a selected aircraft. Indeed, if the click on a selected aircraft, the
     * table will focus on it.
     * @param stateOfSelectedAircraft (ObjectProperty<ObservableAircraftState>) : the state of the selected aircraft.
     */
    private void setSelectionModel(ObjectProperty<ObservableAircraftState> stateOfSelectedAircraft) {
        TableView.TableViewSelectionModel<ObservableAircraftState> selectionModel
                = tableView.getSelectionModel();
        stateOfSelectedAircraft.addListener((value) -> {
            ObservableAircraftState changeState = stateOfSelectedAircraft.get();
            selectionModel.select(changeState);
            tableView.scrollTo(changeState);
        });
        selectionModel.selectedItemProperty().addListener((value) ->
                stateOfSelectedAircraft.setValue(selectionModel.selectedItemProperty().get())
        );
    }

    /**
     * This function sets up a numeric column depending on the following parameters.
     * @param title (String) : the title of the column
     * @param function (Function<ObservableAircraftState, DoubleExpression>) : a function that gives us the
     *                required information for the completion of the column
     * @param decimalPlaces (int) : the number of digits that we want after the decimal point
     * @param unit (double) : the unit used to describe the values of this columns
     * @return (TableColumn<ObservableAircraftState, String>) : a numeric column
     */
    private TableColumn<ObservableAircraftState, String> createNumericColumn(String title,
                        Function<ObservableAircraftState, DoubleExpression> function, int decimalPlaces, double unit) {
        NumberFormat numberFormat = getNumberFormat(decimalPlaces);
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(title);
        column.getStyleClass().add("numeric");
        column.setCellValueFactory(v -> function.apply(v.getValue()).map(
                t -> numberFormat.format(Units.convertTo(t.doubleValue(), unit))));

        column.setPrefWidth(85);
        column.setComparator(comparator);
        return column;
    }

    /**
     * This function sets up a text column depending on the following parameters.
     * @param title (String) : the title of the column
     * @param function (Function<ObservableAircraftState, ObservableValue<String>>) : a function that gives us the
     *                 required information for the completion of the column
     * @param size (int) : the preferred width of the column
     * @return (TableColumn<ObservableAircraftState, String>) : a text column
     */
    private static TableColumn<ObservableAircraftState, String> setUpTextColumn(String title,
                               Function<ObservableAircraftState, ObservableValue<String>> function, int size) {
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(title);
        column.setCellValueFactory(v -> function.apply(v.getValue()));
        column.setPrefWidth(size);
        return column;
    }

    /**
     * This function created the table that will be used.
     */
    private void createTableView() {
        this.tableView = new TableView<>();
        tableView.getStylesheets().add("table.css");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
        tableView.setTableMenuButtonVisible(true);
    }

    /**
     * This function returns the displayed table
     * @return (TableView<ObservableAircraftState>) : the displayed table
     */
    public TableView<ObservableAircraftState> pane(){
        return tableView;
    }

    /**
     * This function reacts on a double click. If a double click was detected, it will accept the method of the
     * given consumer on the set of the observable aircraft.
     * @param consumer (Consumer<ObservableAircraftState>) : the consumer of the aircraft state that we selected.
     */
    public void setOnDoubleClick(Consumer<ObservableAircraftState> consumer) {
        this.consumer = consumer;
        tableView.setOnMouseClicked((value) -> {
            if (value.getClickCount() == 2 && value.getButton() == MouseButton.PRIMARY) {
                this.consumer.accept(stateOfSelectedAircraft.get());
            }
        });
    }

    /**
     * This method returns the status after doing an API request to retrieve the image associated to an
     * observableAircraftState using the ICAO address. We use an input stream to get the content of the received json
     * file.
     *
     * @param observableAircraftState - observableAircraftState the ObservableAircraftState object containing the
     *                               ICAO address
     * @return false if the api request failed - the json file doesn't contain the image (empty photos attribute)
     *         true if the pai request succeeded - the json file contains the image of the aircraft and
     * @throws IOException if an I/O error occurred during request
     */
    private boolean getPlane(ObservableAircraftState observableAircraftState) throws IOException {
        String ICAOAddressString = observableAircraftState.getIcaoAddress().string();
        BufferedReader reader = getReader(ICAOAddressString);

        JSONObject jsonObject = getJsonObjectWithICAO(reader);

        if(!jsonObject.getJSONArray("photos").isEmpty()) {
            QueryImage(ICAOAddressString, jsonObject);
            return true;
        }
        return false;
    }

    /**
     * Gets the buffered reader associated to an ICAO address string by doing an api url request and getting the input 
     * stream reader associated to it and interpreting it using the UTF8 StandardCharset.
     * @param ICAOAddressString (String) the string associated to the ICAO address
     * @return (BufferedReader) the BufferedReader used over the input stream reader that the URL request gets
     * @throws IOException if an I/O error occurred during request
     */
    private static BufferedReader getReader(String ICAOAddressString) throws IOException {
        URL url = new URL("https://api.planespotters.net/pub/photos/hex/"
                + ICAOAddressString);
        return new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * This method queries the image associated to the ICAOAddressString of the aircraft and the jsonObject that we
     * receive from the API url request. It sends a query to the PlaneSpotters API using the url in the photos
     * attribute. When received it saves the image in local cache memory - directory "image-storing".
     * @param ICAOAddressString (String) the string associated to the ICAO address
     * @param jsonObject (JSONObject) the JSONObject associated to the URL request.
     * @throws IOException if an I/O error occurred during request
     */
    private static void QueryImage(String ICAOAddressString, JSONObject jsonObject) throws IOException {
        String mainQuery = jsonObject.getJSONArray("photos").getJSONObject(0).getJSONObject("thumbnail")
                .getString("src");
        URL imageQuery = new URL(mainQuery);
        InputStream in = imageQuery.openConnection().getInputStream();

        Path path = Path.of("image-storing");
        Files.createDirectories(path);

        File file = new File(path.toString(), ICAOAddressString + ".jpg");
        try(OutputStream out = new FileOutputStream(file)) {
            out.write(in.readAllBytes());
        }
    }

    /**
     * This function returns the corresponding JSON object depending on a buffered reader that is read.
     * @param reader (BufferedReader) : the buffered reader obtaining from the URL on planeSpotters.
     * @return (JSONObject) : the corresponding JSON object containing the image of the desired plane
     * @throws IOException : if there is a problem reading the buffered reader
     */
    private static JSONObject getJsonObjectWithICAO(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) sb.append((char) cp);
        return new JSONObject(sb.toString());
    }

    /**
     * This method is used to retrieve an image corresponding to an observableAircraftState. The lookup is done either
     * by directly sending a query to the API by using the getPlane() method or by doing a search through the cache
     * folder "image-storing" created as a directory in our project. This allows to reduce API calls. When the image is
     * successfully retrieved we return the ImageView associated to it. Otherwise, we ignore the call.
     *
     * @param observableAircraftState (ObservableAircraftState) : the ObservableAircraftState object containing
     *                                the ICAO address
     * @return (ImageView) the imageView of the image associated with the given aircraft
     * @throws IOException if an I/O error occurred during request
     */
    public ImageView getCustomImage(ObservableAircraftState observableAircraftState) throws IOException {
        Path path = Path.of("image-storing");
        Path fileResultingPath;
        String aircraftICAO = observableAircraftState.getIcaoAddress().string();


        if (!Files.exists(fileResultingPath = path.resolve(aircraftICAO + ".jpg"))) {
            if (getPlane(observableAircraftState)) {
                try (InputStream stream = new FileInputStream(fileResultingPath.toString())) {
                    Image image = new Image(stream);
                    return new ImageView(image);
                }
            }
        } else {
            try (InputStream stream = new FileInputStream(fileResultingPath.toString())) {
                Image image = new Image(stream);
                return new ImageView(image);
            }
        }
        return new ImageView();
    }
}