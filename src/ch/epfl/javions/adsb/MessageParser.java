package ch.epfl.javions.adsb;

/**
 * The MessageParser
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public class MessageParser {

    private MessageParser(){}

    /**
     * This function returns a message depending on its code type.  If the message is invalid, or if the type code is invalid
     * the function returns null.
     *
     * @param rawMessage (RawMessage) : the message that we verify before returning it if it is valid
     * @return (Message) : the message depending on its code type.
     */
    public static Message parse(RawMessage rawMessage) {
        int typeCode = rawMessage.typeCode();
        return switch (typeCode) {
            case 1, 2, 3, 4 -> AircraftIdentificationMessage.of(rawMessage);

            case 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22 ->
                    AirbornePositionMessage.of(rawMessage);

            case 19 -> AirborneVelocityMessage.of(rawMessage);

            default -> null;
        };

    }
}
