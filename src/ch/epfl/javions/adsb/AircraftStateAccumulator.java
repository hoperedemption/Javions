package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;

import java.util.Objects;

/**
 * The AircraftStateAccumulator
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param <T> : The state setter
 */
public class AircraftStateAccumulator<T extends AircraftStateSetter> {

    /**
     * The maximum time in nanoseconds between two position messages to allow a position to be updated
     */
    public static final double TIME_CONSTRAINT = 10 * 1e9;
    private final T stateSetter;

    private final AirbornePositionMessage[] messagePositionParityTable;

    /**
     * The constructor of the state accumulator. It associates to our state setter, the state setter that is given in parameter.
     * @param stateSetter (T) : the state setter that we will use.
     * @throws NullPointerException : if the state setter given is null.
     */
    public AircraftStateAccumulator(T stateSetter){
        messagePositionParityTable = new AirbornePositionMessage[2];
        this.stateSetter = Objects.requireNonNull(stateSetter);
    }

    /**
     * This function returns the state setter used.
     * @return (T) : this function returns the state setter used.
     */
    public T stateSetter() {
        return stateSetter;
    }

    /**
     * Depending on the type of the message, this function updates the state setter of the aircraft. More precisely, if the message is
     * an AirbornePositionMessage, it stores its two last odd and even messages. Then, it verifies if the time between them is smaller
     * than 10s and if the position can be decoded. Only after that, the position of the aircraft is updated.
     * @param message the given message to be identified
     */
    public void update(Message message) {
        stateSetter.setLastMessageTimeStampNs(message.timeStampNs());
        switch (message) {
            case AircraftIdentificationMessage identificationMessage -> {
                stateSetter.setCategory(identificationMessage.category());
                stateSetter.setCallSign(identificationMessage.callSign());
            }

            case AirbornePositionMessage positionMessage -> {
                stateSetter.setAltitude(positionMessage.altitude());

                int messageParity = positionMessage.parity();
                messagePositionParityTable[positionMessage.parity()] = positionMessage;
                AirbornePositionMessage lastEvenMessage = messagePositionParityTable[0];
                AirbornePositionMessage lastOddMessage = messagePositionParityTable[1];

                setPositionMessage(messageParity, lastEvenMessage, lastOddMessage);
            }

            case AirborneVelocityMessage velocityMessage -> {
                stateSetter.setTrackOrHeading(velocityMessage.trackOrHeading());
                stateSetter.setVelocity(velocityMessage.speed());
            }
            default -> {}
        }
    }

    /**
     * This function updates the position according to certain parameters. Indeed, if the two messages are defined,
     * and if the time separating the two message is less than 10 seconds we can proceed to the update of the
     * position. For that, we decode the position using the CPR decoder, and, we update it if the position returned
     * is not null.
     * @param messageParity (int) : the parity of the last received message
     * @param lastEvenMessage (AirbornePositionMessage) : the last position message of even parity
     * @param lastOddMessage (AirbornePositionMessage) : the last position message of odd parity
     */
    private void setPositionMessage(int messageParity, AirbornePositionMessage lastEvenMessage, AirbornePositionMessage lastOddMessage) {
        if(lastEvenMessage != null && lastOddMessage != null) {
            if(Math.abs(lastEvenMessage.timeStampNs() - lastOddMessage.timeStampNs()) <= TIME_CONSTRAINT) {
                GeoPos position = CprDecoder.decodePosition(lastEvenMessage.x(), lastEvenMessage.y(),
                        lastOddMessage.x(), lastOddMessage.y(), messageParity);

                if(position != null) {
                    stateSetter.setPosition(position);
                }
            }
        }
    }
}
