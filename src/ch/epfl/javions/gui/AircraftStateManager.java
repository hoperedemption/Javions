package ch.epfl.javions.gui;

import ch.epfl.javions.adsb.AircraftStateAccumulator;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.IOException;
import java.util.*;

/**
 * The AircraftStateManager
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class AircraftStateManager {

    /**
     * The time that is required before the suppression of a plane from the set of observable aircraft.
     */
    private static final long MAXIMAL_TIME_BEFORE_SUPPRESSION = 60_000_000_000L;

    private long mostRecentMessageTimeStampNs = -1;
    private final ObservableSet<ObservableAircraftState> setOfAircraftState =
            FXCollections.observableSet();
    private final ObservableSet<ObservableAircraftState> unmodifiableObservableStatesSet =
            FXCollections.unmodifiableObservableSet(setOfAircraftState);
    private final Map<IcaoAddress, AircraftStateAccumulator<ObservableAircraftState>> aircraftStateAccumulatorHashMap
            = new HashMap<>();
    private final AircraftDatabase mictronicsDatabase;


    /**
     * This is the constructor of AircraftStateManager.
     *
     * @param mictronicsDatabase (AircraftDatabase) : the database containing all the characteristics of the classified
     *                           airplanes
     */
    public AircraftStateManager(AircraftDatabase mictronicsDatabase) {
        this.mictronicsDatabase = mictronicsDatabase;
    }

    /**
     * getter of an unmodifiable view for the states set
     *
     * @return (Set < ObservableAircraftState >) : unmodifiable view for the states set
     */
    public Set<ObservableAircraftState> states() {
        return unmodifiableObservableStatesSet;
    }

    /**
     * This method updates the state of the airplane whenever a message is received.
     * At first, we search its characteristics in the database and associate it with the airplane.
     * If the characteristics are not null, we link our airplane to its state. If the airplane has been saved
     * in the map linking ICAO to aircraft state, we update the component of the plane according to the message
     * received. If the position of the saved plane is not null, we add it to our set of plane position.
     *
     * @param message (Message) : the message just received.
     * @throws IOException : if there is a problem during the reading of the data in the database.
     */

    public void updateWithMessage(Message message) throws IOException {
        IcaoAddress messageIcaoAddress = message.icaoAddress();
        AircraftData data = mictronicsDatabase.get(messageIcaoAddress);

        aircraftStateAccumulatorHashMap.computeIfAbsent(messageIcaoAddress, k -> new AircraftStateAccumulator<>(
                new ObservableAircraftState(message.icaoAddress(), data)
        )).update(message);
        updateGeneralStateOfAircraft(message, messageIcaoAddress);
    }

    /**
     * This function updates the general state of the aircraft. The state of the aircraft sees its trajectory
     * updated according to the ICAO address of the airplane. If the position stocked in the state is not null,
     * the airplane is stocked in the state of the observable airplanes.
     * Each time we receive a new message, we update the most recent time stamp by associating it to the
     * time stamp of the last plane seen.
     *
     * @param message            (Message) : the message just received.
     * @param messageIcaoAddress (IcaoAddress) : the ICAO address of the airplane that just sent the message
     */
    private void updateGeneralStateOfAircraft(Message message, IcaoAddress messageIcaoAddress) {
        ObservableAircraftState observableState = aircraftStateAccumulatorHashMap.get(messageIcaoAddress).stateSetter();
        if (observableState.getPosition() != null) {
            setOfAircraftState.add(observableState);
        }
        mostRecentMessageTimeStampNs = message.timeStampNs();
    }

    /**
     * This method deletes a plane from the observable set, if we did not receive any message from it
     * for more than one minute.
     */
    public void purge() {
        Iterator<IcaoAddress> it = aircraftStateAccumulatorHashMap.keySet().iterator();
        while (it.hasNext()) {
            IcaoAddress icaoAddress = it.next();
            ObservableAircraftState currentState = aircraftStateAccumulatorHashMap.get(icaoAddress).stateSetter();
            if (mostRecentMessageTimeStampNs - currentState.getTimeStampNs() > MAXIMAL_TIME_BEFORE_SUPPRESSION) {
                setOfAircraftState.remove(currentState);
                it.remove();
            }
        }
    }
}