package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.WebMercator;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.*;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.beans.property.ObjectProperty;
import javafx.collections.SetChangeListener;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.Objects;

import static ch.epfl.javions.gui.ObservableAircraftState.AirbornePos;
import static javafx.scene.paint.CycleMethod.NO_CYCLE;

/**
 * The aircraft controller.
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class AircraftController {
    /**
     * The highest altitude a plane can reach
     */
    public static final double HIGHEST_ALTITUDE = 12000;

    /**
     * The factor that is used to distinguish the difference between two low altitudes
     */
    public static final double LOW_ALTITUDE_DISTINGUISH = 1 / 3d;

    /**
     * The scale of color used to distinguish the different altitude
     */
    public static final ColorRamp PLASMA = ColorRamp.PLASMA;
    public static final AircraftTypeDesignator EMPTY_DESIGNATOR = new AircraftTypeDesignator("");
    public static final AircraftDescription EMPTY_DESCRIPTION = new AircraftDescription("");


    private final MapParameters mapParameters;
    private final ObjectProperty<ObservableAircraftState> stateOfSelectedAircraft;
    private final Pane pane;

    private final BaseMapController bmc;


    /**
     * The constructor of AircraftController. This class sets up a new pane and create a whole tree of connection
     * linking all the new indexed airplanes to the only node present here. Also, each time a change is noticed in
     * the set of observable aircraft (meaning that each time a plane is either added or its characteristics are updated
     * or is removed), we update, the node corresponding to it.
     *
     * @param mapParameters           (MapParameters) : the parameters of the map
     * @param setOfAircraftStates     (ObservableSet<ObservableAircraftState>) : the set of all the planes that we can
     *                                position on earth (because we know their position)
     * @param stateOfSelectedAircraft (ObjectProperty<ObservableAircraftState>) : the state of a selected airplane
     */
    public AircraftController(MapParameters mapParameters, ObservableSet<ObservableAircraftState> setOfAircraftStates,
                              ObjectProperty<ObservableAircraftState> stateOfSelectedAircraft,
                              BaseMapController bmc) {

        this.bmc = bmc;
        this.mapParameters = mapParameters;
        this.stateOfSelectedAircraft = stateOfSelectedAircraft;
        this.pane = new Pane();
        this.pane.setPickOnBounds(false);
        this.pane.getStylesheets().add("/aircraft.css");

        setOfAircraftStates.addListener((SetChangeListener<ObservableAircraftState>) change -> {
            if (change.wasAdded()) {
                addGroupCorrespondingToChange(change.getElementAdded());
            } else if (change.wasRemoved()) {
                deleteGroupCorrespondingToChange(change.getElementRemoved());
            }
        });
    }

    /**
     * This function adds to the pane the group corresponding to the airplane that was just added to the set of the
     * airplanes after finding the closest icon for this plane.
     *
     * @param addedState(ObservableAircraftState) the state of the aircraft to be added
     */
    private void addGroupCorrespondingToChange(ObservableAircraftState addedState) {
        AircraftData dataOfAircraft = addedState.getAircraftData();
        boolean flag = Objects.nonNull(dataOfAircraft);

        AircraftIcon aircraftIcon = AircraftIcon.iconFor(
                flag ? dataOfAircraft.typeDesignator() : EMPTY_DESIGNATOR,
                flag ? dataOfAircraft.description() : EMPTY_DESCRIPTION,
                addedState.getCategory(),
                flag ? dataOfAircraft.wakeTurbulenceCategory() : WakeTurbulenceCategory.UNKNOWN);

        Group group = setUpGroupForGivenAircraft(aircraftIcon, addedState);
        pane.getChildren().add(group);
    }

    /**
     * This function removes from the pane the airplane that was just removed from the set of observable aircraft
     *
     * @param deletedState the state of the deleted Aircraft
     */
    private void deleteGroupCorrespondingToChange(ObservableAircraftState deletedState) {
        pane.getChildren().removeIf(child ->
                child.idProperty().get().equals(deletedState.getIcaoAddress().string())
        );
    }

    /**
     * This function sets the group containing all the information (trajectory, icon, label, SVG path) in the ICAO
     * group of the plane.
     *
     * Addition (bonus) : if we click on a certain plane, we automatically recenter the view on this plane.
     *
     * @param aircraftIcon (AircraftIcon) : the closest icon representing the airplane
     * @param addedState   (ObservableAircraftState) : the observable state that was just added to set of aircraft.
     * @return (Group) : the group containing all the information (trajectory, icon, label, SVG path) in the ICAO
     * group of the plane.
     */
    private Group setUpGroupForGivenAircraft(AircraftIcon aircraftIcon, ObservableAircraftState addedState) {
        Group mainAircraftGroup = new Group();
        mainAircraftGroup.setId(addedState.getIcaoAddress().string());
        mainAircraftGroup.viewOrderProperty().bind(addedState.altitudeProperty().negate());

        Group labelAndIconGroup = new Group();
        bindLabelAndIconGroupWithCoordinates(labelAndIconGroup, addedState.geoPosProperty());

        Group labelGroup = new Group();
        labelGroup.getStyleClass().add("label");
        bindLabelComponents(labelGroup, addedState);

        SVGPath path = new SVGPath();
        path.getStyleClass().add("aircraft");
        bindSvgPathComponents(aircraftIcon, addedState, path);

        Group trajectoryGroup = new Group();
        trajectoryGroup.getStyleClass().add("trajectory");
        bindAndInstallTrajectoryGroup(addedState, trajectoryGroup);

        path.setOnMouseClicked(value -> {
                    stateOfSelectedAircraft.setValue(addedState);
                    stateOfSelectedAircraft.get().geoPosProperty().addListener((o, ov, nv) ->
                        bmc.centerOn(stateOfSelectedAircraft.get().getPosition())
                    );
                }
        );

        setUpGroup(path, mainAircraftGroup, labelAndIconGroup, labelGroup, trajectoryGroup);

        return mainAircraftGroup;
    }

    /**
     * This function adds a new line to the group of the trajectory. Also, whenever the zoom level changes, the
     * trajectory is recalculated.
     *
     * @param addedState      (ObservableAircraftState) : the observable state that was just added to set of aircraft.
     * @param trajectoryGroup (Group) : the group containing the trajectory of the airplane.
     */
    private void bindAndInstallTrajectoryGroup(ObservableAircraftState addedState, Group trajectoryGroup) {
        ObservableList<AirbornePos> positionObservableList = addedState.getTrajectoryUnmodifiable();

        positionObservableList.addListener((ListChangeListener<AirbornePos>) change -> {
                    if (canBeVisibleTrajectory(addedState)) {
                        recalculateTrajectory(trajectoryGroup, positionObservableList);
                    }
                }
        );
        mapParameters.getZoomProperty().addListener((value) -> {
                    if (canBeVisibleTrajectory(addedState)) {
                        recalculateTrajectory(trajectoryGroup, positionObservableList);
                    }
                }
        );

        trajectoryGroup.layoutXProperty().bind(mapParameters.getMinXProperty().negate());
        trajectoryGroup.layoutYProperty().bind(mapParameters.getMinYProperty().negate());
        trajectoryGroup.visibleProperty().bind(Bindings.createBooleanBinding(() ->
                canBeVisibleTrajectory(addedState), mapParameters.getZoomProperty(), stateOfSelectedAircraft));
    }

    /**
     * This function creates a whole dependency of lines representing the whole trajectory of the plane. For that,
     * we iterate through all the trajectories in the list and for each consecutive pair, we define a line linking the
     * two points. This line is then added to the group representing the trajectory. If the zoom, changed previously, it
     * clears the whole group and recalculates the trajectory.
     *
     * @param trajectoryGroup (Group) : the group containing all the lines that together form the total trajectory
     *                        of a plane
     * @param observableList  (ObservableList<AirbornePos> observableList) : the list of all the trajectories of the plane
     */
    private void recalculateTrajectory(Group trajectoryGroup, ObservableList<AirbornePos> observableList) {
        trajectoryGroup.getChildren().clear();
        Iterator<AirbornePos> it = observableList.iterator();
        AirbornePos firstPosition;
        AirbornePos secondPosition = it.next();

        while (it.hasNext()) {
            firstPosition = secondPosition;
            secondPosition = it.next();
            Line line = calculateLine(firstPosition, secondPosition);
            trajectoryGroup.getChildren().add(line);
        }
    }

    /**
     * This function returns the line representing the trajectory that followed a plane between two points. Then,
     * it colors the trajectory of the plane with a gradient of colors symbolising the difference of altitude from
     * the start to the destination.
     *
     * @param currentPosition (AirbornePos) : the current position of the plane
     * @param lastPosition    (AirbornePos) : the last position of the plane (just before the current one)
     * @return (Line) : the line representing the trajectory that followed a plane between two given points.
     */
    private Line calculateLine(AirbornePos currentPosition, AirbornePos lastPosition) {
        double[] tableX = getTableForXCoordinates(lastPosition, currentPosition);
        double[] tableY = getTableForYCoordinates(lastPosition, currentPosition);
        Line line = new Line(tableX[0], tableY[0], tableX[1], tableY[1]);

        double lastAltitude = lastPosition.altitude();
        double currentAltitude = currentPosition.altitude();
        if (lastAltitude == currentAltitude) {
            Color color = getColorFromRamp(lastAltitude);
            line.setStroke(color);
        } else {
            Stop s1 = new Stop(0, getColorFromRamp(lastAltitude));
            Stop s2 = new Stop(1, getColorFromRamp(currentAltitude));
            line.setStroke(new LinearGradient(0, 0, 1, 0, true, NO_CYCLE, s1, s2));
        }

        return line;
    }

    /**
     * This function returns an array of x positions of the plane in Mercator coordinates. The first component is the
     * x coordinate of the point of start of the plane and the second element of the array is the x coordinate of the
     * point of destination of the plane.
     *
     * @param firstPosition  (AirbornePos) : the start position of the plane
     * @param secondPosition (AirbornePos) : the end position of the plane
     * @return (double[]) : the start and end coordinates of the plane in the x-axis.
     */
    private double[] getTableForXCoordinates(AirbornePos firstPosition,
                                             AirbornePos secondPosition) {
        double startX = WebMercator.x(mapParameters.getZoom(), firstPosition.geoPos().longitude());
        double endX = WebMercator.x(mapParameters.getZoom(), secondPosition.geoPos().longitude());
        return new double[]{startX, endX};
    }

    /**
     * This function returns an array of y positions of the plane in Mercator coordinates. The first component is the
     * y coordinate of the point of start of the plane and the second element of the array is the y coordinate of the
     * point of destination of the plane.
     *
     * @param firstPosition  (AirbornePos) : the start position of the plane
     * @param secondPosition (AirbornePos) : the end position of the plane
     * @return (double[]) : the start and end coordinates of the plane in the y-axis.
     */
    private double[] getTableForYCoordinates(AirbornePos firstPosition,
                                             AirbornePos secondPosition) {
        double startY = WebMercator.y(mapParameters.getZoom(), firstPosition.geoPos().latitude());
        double endY = WebMercator.y(mapParameters.getZoom(), secondPosition.geoPos().latitude());
        return new double[]{startY, endY};
    }

    /**
     * This function links the SVG path with all the characteristics it needs to provide the closest icon corresponding
     * to a given plane, namely the altitude and the speed. Also, the color of the icon changes with the altitude.
     *
     * @param aircraftIcon (AircraftIcon) : the icon representing the airplane
     * @param addedState   (ObservableAircraftState) : the updated state of the given airplane
     * @param path         (SVGPath) : the path that leads to the icon of the plane
     */
    private void bindSvgPathComponents(AircraftIcon aircraftIcon, ObservableAircraftState addedState, SVGPath path) {
        path.contentProperty().bind(Bindings.createStringBinding(
                aircraftIcon::svgPath
        ));
        path.rotateProperty().bind(Bindings.createDoubleBinding(
                () -> aircraftIcon.canRotate() ? Units.convertTo(addedState.getTrackOrHeading(), Units.Angle.DEGREE) : 0,
                addedState.trackOrHeadingProperty()
        ));
        path.fillProperty().bind(Bindings.createObjectBinding(() -> getColorFromRamp(addedState.getAltitude())
                , addedState.altitudeProperty()));
    }

    /**
     * This function creates a link between the label and the Text that should be written. Indeed, each time the text,
     * must be updated, it updates it (with the new altitude for example), and associates it in a rectangle. Then, all
     * these components are put in the corresponding group
     *
     * @param labelGroup (Group) : the group that should be updated
     * @param addedState (ObservableAircraftState) : the state of the plane that should be updated
     */
    private void bindLabelComponents(Group labelGroup, ObservableAircraftState addedState) {
        Text text = new Text();
        text.textProperty().bind(Bindings.createStringBinding(() ->
                        getTheLabelIdentifier(addedState) +
                                '\n' +
                                getTheLabelInformation(addedState)
                , addedState.altitudeProperty(), addedState.velocityProprety()));

        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(text.layoutBoundsProperty().map(bounds -> bounds.getWidth() + 4));
        rectangle.heightProperty().bind(text.layoutBoundsProperty().map(bounds -> bounds.getHeight() + 4));

        labelGroup.visibleProperty().bind(Bindings.createBooleanBinding(() ->
                        canBeVisibleLabel(addedState)
                , mapParameters.getZoomProperty(), stateOfSelectedAircraft));
        labelGroup.getChildren().addAll(rectangle, text);
    }

    /**
     * This function returns a boolean to see if the label can be visible. For that, if the ICAO address is available it
     * makes the label visible. Otherwise, if the zoom is greater or equal to 11 it returns also the label.
     *
     * @param state (ObservableAircraftState) : the observable state of the airplane
     * @return (boolean) : a boolean to see if the label can be visible.
     */
    private boolean canBeVisibleLabel(ObservableAircraftState state) {
        ObservableAircraftState selectedAircraftState = stateOfSelectedAircraft.getValue();
        if (selectedAircraftState != null) {
            return state.getIcaoAddress().string()
                    .equals(selectedAircraftState.getIcaoAddress().string()) || mapParameters.getZoom() >= 11;
        }
        return mapParameters.getZoom() >= 11;
    }

    /**
     * This function returns a boolean to see if the trajectory can be visible. For that, if the ICAO address of the
     * selected plane is equal to the ICAO address of the plane in the state. If yes, the trajectory can be displayed.
     * Otherwise, it does not display it.
     *
     * @param state (ObservableAircraftState) : the state of the airplane that is being tested
     * @return (boolean) : a boolean to see if the trajectory can be visible.
     */
    private boolean canBeVisibleTrajectory(ObservableAircraftState state) {
        ObservableAircraftState selectedAircraftState = stateOfSelectedAircraft.getValue();
        if (selectedAircraftState != null) {
            return state.getIcaoAddress().string().equals(selectedAircraftState.getIcaoAddress().string());
        }
        return false;
    }

    /**
     * This function returns the identity of the plane. Indeed, it will normally return its registration. If this
     * one is not available, it returns its call sign. If this one is also not available, it will return its ICAO
     * address
     *
     * @param addedState (ObservableAircraftState) : the state of the plane that should be updated.
     * @return (String) : the identity of the plane.
     */
    private String getTheLabelIdentifier(ObservableAircraftState addedState) {
        AircraftData data = addedState.getAircraftData();
        AircraftRegistration registration;
        CallSign callSign;

        if(Objects.nonNull(data)) {
            if ((registration = data.registration()) != null) {
                return registration.string();
            } else if ((callSign = addedState.getCallSign()) != null) {
                return callSign.string();
            }
        }
        return addedState.getIcaoAddress().string();
    }

    /**
     * This function returns the speed and the latitude of the label that should be associated to a plane.
     *
     * @param addedState (ObservableAircraftState) : the state of the plane that needs an update
     * @return (String) : the speed and the latitude of the label that should be associated to a plane.
     */
    private String getTheLabelInformation(ObservableAircraftState addedState) {
        double velocity = addedState.getVelocity();
        double altitude = addedState.getAltitude();
        String resVelocity = !Double.isNaN(velocity) ? String.format("%.0f", velocity) : "?";
        String resAltitude = !Double.isNaN(altitude) ? String.format("%.0f", altitude) : "?";
        return String.format("%s km/h" + '\u2002' + "%s m", resVelocity, resAltitude);
    }

    /**
     * This function sets up a new group associated to the ICAO of a certain plane. Indeed, following the given
     * architecture, it creates all the necessary groups and links them together to form a tree of components.
     *
     * @param path              (SVGPath) : the path that leads to the icon representing the plane
     * @param mainAircraftGroup (Group) : the group identified by the ICAO address of the plane
     * @param trajectoryGroup   (Group) : the group representing the trajectory of the plane
     * @param labelAndIconGroup (Group) : the group that contains the Icon group and the label group.
     * @param labelGroup        (Group) : the group containing the label of the plane (its registration, altitude, speed)
     */
    private void setUpGroup(SVGPath path, Group mainAircraftGroup, Group labelAndIconGroup,
                            Group labelGroup, Group trajectoryGroup) {
        labelAndIconGroup.getChildren().add(path);
        labelAndIconGroup.getChildren().add(labelGroup);
        mainAircraftGroup.getChildren().add(trajectoryGroup);
        mainAircraftGroup.getChildren().add(labelAndIconGroup);
    }

    /**
     * This function links the position properties of the label and of the icon (that are at the same position on
     * the pane) to their value. This value is constantly updated whenever there is a change of the position.
     *
     * @param labelAndIconGroup (Group) : the group in which the position of the label and the icon must be
     *                          updated
     * @param posObjectProperty (ReadOnlyObjectProperty<GeoPos>) : the component that must be observed to see if it
     *                          changes
     */
    private void bindLabelAndIconGroupWithCoordinates(Group labelAndIconGroup,
                                                      ReadOnlyObjectProperty<GeoPos> posObjectProperty) {
        labelAndIconGroup.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
                        getXCoordinate(posObjectProperty.get())
                , mapParameters.getZoomProperty(), mapParameters.getMinXProperty(), posObjectProperty));
        labelAndIconGroup.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
                        getYCoordinate(posObjectProperty.get())
                , mapParameters.getZoomProperty(), mapParameters.getMinYProperty(), posObjectProperty));
    }

    /**
     * Given a certain position on earth, this function returns the x  position on the component on the pane. For that,
     * it computes the x coordinate following the Mercator's convention, then it subtract, from the result, the value
     * of the current minimum (namely, the x coordinate of the top left corner of the pane)
     *
     * @param position (GeoPos) : the position of the airplane on Earth
     * @return (Double) : the y position on the component on the pane
     */
    private Double getXCoordinate(GeoPos position) {
        return WebMercator.x(mapParameters.getZoom(), position.longitude()) - mapParameters.getMinX();
    }

    /**
     * Given a certain position on earth, this function returns the y position on the component on the pane. For that,
     * it computes the y coordinate following the Mercator's convention, then it subtract, from the result, the value
     * of the current minimum (namely, the y coordinate of the top left corner of the pane)
     *
     * @param position (GeoPos) : the position of the airplane on Earth
     * @return (Double) : the x position on the component on the pane
     */
    private Double getYCoordinate(GeoPos position) {
        return WebMercator.y(mapParameters.getZoom(), position.latitude()) - mapParameters.getMinY();
    }

    /**
     * This function returns the corresponding color to a given altitude
     *
     * @param altitude (double) : the altitude that we will link to a color
     * @return (Color) : the corresponding color to a given altitude
     */
    private Color getColorFromRamp(double altitude) {
        return !Double.isNaN(altitude) ? PLASMA.at(Math.pow(altitude / HIGHEST_ALTITUDE, LOW_ALTITUDE_DISTINGUISH)) :
                null;
    }

    /**
     * The getter of the pane
     *
     * @return (Pane) : the pane used in this class
     */
    public Pane pane() {
        return pane;
    }
}
