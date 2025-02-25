package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.adsb.AircraftStateSetter;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

/**
 * An ObservableAircraftState
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class ObservableAircraftState implements AircraftStateSetter {
    private final IcaoAddress icaoAddress;
    private final AircraftData data;

    private final ObservableList<AirbornePos> trajectoryObservable = FXCollections.observableArrayList();
    private final ObservableList<AirbornePos> trajectoryUnmodifiableList
            = FXCollections.unmodifiableObservableList(trajectoryObservable);

    private final LongProperty lastMessageTimeStampsNsProperty = new SimpleLongProperty();
    private final IntegerProperty categoryProperty = new SimpleIntegerProperty();
    private final ObjectProperty<CallSign> callSignObjectProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<GeoPos> positionProperty = new SimpleObjectProperty<>();
    private final DoubleProperty altitudeProperty = new SimpleDoubleProperty();
    private final DoubleProperty velocityProperty = new SimpleDoubleProperty();
    private final DoubleProperty trackOrHeadingProperty = new SimpleDoubleProperty();

    private long previousTimeStampThatUpdatedTrajectory;
    private GeoPos lastReceivedPosition;
    private double lastReceivedAltitude;

    /**
     * The constructor of ObservableAircraftState. It sets the velocity and the altitude to Nan.
     *
     * @param icaoAddress (IcaoAddress) : the ICAO address of the given airplane
     * @param data        (AircraftData) : the characteristics of the given airplane
     */
    public ObservableAircraftState(IcaoAddress icaoAddress, AircraftData data) {
        this.icaoAddress = icaoAddress;
        this.data = data;
        setVelocity(Double.NaN);
        setAltitude(Double.NaN);
        previousTimeStampThatUpdatedTrajectory = -1;
    }

    /**
     * the getter of the ICAO address.
     *
     * @return (IcaoAddress) : returns the ICAO address of the airplane
     */
    public IcaoAddress getIcaoAddress() {
        return icaoAddress;
    }

    /**
     * The getter of the data of the airplane
     *
     * @return (AircraftData) : returns the characteristics of the airplane
     */
    public AircraftData getAircraftData() {
        return data;
    }

    //TimeStamp

    /**
     * the getter of the lastMessageTimeStampsNs property.
     *
     * @return (ReadOnlyLongProperty) : returns the lastMessageTimeStampsNs property
     */
    public ReadOnlyLongProperty lastMessageTimeStampsNsProperty() {
        return lastMessageTimeStampsNsProperty;
    }

    /**
     * The getter of the value of the lastMessageTimeStampsNs property
     *
     * @return (long) : returns the value of the lastMessageTimeStampsNs property
     */
    public long getTimeStampNs() {
        return lastMessageTimeStampsNsProperty.get();
    }

    @Override
    public void setLastMessageTimeStampNs(long timeStampNs) {
        this.lastMessageTimeStampsNsProperty.set(timeStampNs);
    }

    //Category

    /**
     * the getter of the category property.
     *
     * @return (ReadOnlyIntegerProperty) : returns the category property
     */
    public ReadOnlyIntegerProperty categoryProperty() {
        return categoryProperty;
    }

    /**
     * The getter of the value of the category property
     *
     * @return (int) : returns the value of the category property
     */

    public int getCategory() {
        return categoryProperty.getValue();
    }

    @Override
    public void setCategory(int category) {
        this.categoryProperty.set(category);
    }

    //CallSign

    /**
     * the getter of the callSign property.
     *
     * @return (ReadOnlyObjectProperty < CallSign >) : returns the callSign property
     */
    public ReadOnlyObjectProperty<CallSign> callSignProperty() {
        return callSignObjectProperty;
    }

    /**
     * The getter of the value of the callSign property
     *
     * @return (CallSign) : returns the value of the callSign property
     */
    public CallSign getCallSign() {
        return callSignObjectProperty.get();
    }

    @Override
    public void setCallSign(CallSign callSign) {
        this.callSignObjectProperty.set(callSign);
    }

    //GeoPos

    /**
     * the getter of the geoPos property.
     *
     * @return (ReadOnlyObjectProperty < GeoPos >) : returns the geoPos property
     */
    public ReadOnlyObjectProperty<GeoPos> geoPosProperty() {
        return positionProperty;
    }

    /**
     * The getter of the value of the geoPos property
     *
     * @return (GeoPos) : returns the value of the geoPos property
     */
    public GeoPos getPosition() {
        return positionProperty.get();
    }

    /**
     * Setter for the position
     * When we receive a new position and the altitude is known,
     * then we add the pair (current position, current altitude)
     * to the trajectory list
     * @param position (GeoPos) : the given geographic position
     */
    @Override
    public void setPosition(GeoPos position) {
        if (!Objects.isNull(position)) {
            positionProperty.set(position);
            if (!Double.isNaN(lastReceivedAltitude)) {
                trajectoryObservable.add(new AirbornePos(position, lastReceivedAltitude));
                previousTimeStampThatUpdatedTrajectory = getTimeStampNs();
                lastReceivedPosition = null;
                lastReceivedAltitude = Double.NaN;
            } else {
                lastReceivedPosition = position;
            }
        }
    }

    //Trajectory

    /**
     * This function returns an unmodifiable list containing all the trajectories of the airplane.
     *
     * @return (ObservableList < AirbornePos >) : returns an unmodifiable list containing all the trajectories of
     * the airplane.
     */
    public ObservableList<AirbornePos> getTrajectoryUnmodifiable() {
        return trajectoryUnmodifiableList;
    }

    //Altitude

    /**
     * the getter of the altitude property.
     *
     * @return (ReadOnlyDoubleProperty) : returns the altitude property
     */
    public ReadOnlyDoubleProperty altitudeProperty() {
        return altitudeProperty;
    }

    /**
     * The getter of the value of the altitude property
     *
     * @return (double) : returns the value of the altitude property
     */
    public double getAltitude() {
        return altitudeProperty.get();
    }

    /**
     * Setter for the altitude
     * When we receive a new altitude, if the position is known :
     * -> if the trajectory is empty, then current position and current altitude is added as a pair
     * -> if the timestamp of the current message and the timestamp of the previous message that updated the
     * trajectory are the same then we replace the last element by the new pair.
     * @param altitude (double) : the altitude that is given.
     */
    @Override
    public void setAltitude(double altitude) {
        altitudeProperty.set(altitude);
        if (Objects.nonNull(lastReceivedPosition)) {
            if(trajectoryObservable.isEmpty()) {
                trajectoryObservable.add(new AirbornePos(lastReceivedPosition, altitude));
                previousTimeStampThatUpdatedTrajectory = getTimeStampNs();
                lastReceivedAltitude = Double.NaN;
                lastReceivedPosition = null;
            }else if (previousTimeStampThatUpdatedTrajectory == getTimeStampNs()) {
                trajectoryObservable.set(trajectoryObservable.size() - 1,
                        new AirbornePos(lastReceivedPosition, altitude));
                previousTimeStampThatUpdatedTrajectory = getTimeStampNs();
                lastReceivedAltitude = Double.NaN;
                lastReceivedPosition = null;
            }
        } else {
            lastReceivedAltitude = altitude;
        }
    }

    //Velocity

    /**
     * the getter of the velocity property.
     *
     * @return (ReadOnlyDoubleProperty) : returns the velocity property
     */
    public ReadOnlyDoubleProperty velocityProprety() {
        return velocityProperty;
    }

    /**
     * The getter of the value of the velocity property
     *
     * @return (double) : returns the value of the velocity property
     */
    public double getVelocity() {
        return velocityProperty.get();
    }

    @Override
    public void setVelocity(double velocity) {
        this.velocityProperty.set(velocity);
    }

    //TrackOrHeading

    /**
     * the getter of the trackOrHeading property.
     *
     * @return (ReadOnlyDoubleProperty) : returns the trackOrHeading property
     */
    public ReadOnlyDoubleProperty trackOrHeadingProperty() {
        return trackOrHeadingProperty;
    }

    /**
     * The getter of the value of the trackOrHeading property
     *
     * @return (double) : returns the value of the trackOrHeading property
     */
    public double getTrackOrHeading() {
        return trackOrHeadingProperty.get();
    }

    @Override
    public void setTrackOrHeading(double trackOrHeading) {
        trackOrHeadingProperty.set(trackOrHeading);
    }

    /**
     * Private record AirbornePos
     *
     * @param geoPos   (GeoPos) : the position of the airplane
     * @param altitude (double) : the altitude of the airplane
     */
    public record AirbornePos(GeoPos geoPos, double altitude) {
    }
}
