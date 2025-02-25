package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * The AircraftIdentificationMessage
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param timeStampNs (long) : the time (in nanosecond) when we receive the message
 * @param icaoAddress (IcaoAddress) : the IcaoAddress of the aircraft
 * @param category (int) : the category of the airplane
 * @param callSign (CallSign) : the call sign of the aircraft
 */
public record AircraftIdentificationMessage(long timeStampNs, IcaoAddress icaoAddress, int category, CallSign callSign) implements Message {

    /**
     * The constructor of the AircraftIdentificationMessage. It verifies if all the parameter are correct.
     * @throws IllegalArgumentException  if the timeStamp is not positive (0 included)
     * @throws NullPointerException : if the call sign or the icaoAdress are null.
     */
    public AircraftIdentificationMessage {
        Preconditions.checkArgument(timeStampNs >= 0);
        Objects.requireNonNull(icaoAddress);
        Objects.requireNonNull(callSign);
    }

    /**
     * This function returns the corresponding AircraftIdentificationMessage depending on a given RawMessage. However,
     * if one of the character is invalid during the decoding it returns null.
     * @param rawMessage (RawMessage) : the initial raw message that is going to be decoded
     * @return (AircraftIdentificationMessage) : the AircraftIdentificationMessage associated to a RawMessage. If one character is
     * invalid, it returns null.
     */
    public static AircraftIdentificationMessage of(RawMessage rawMessage) {
        StringBuilder result = new StringBuilder();
        long payload = rawMessage.payload();

        int typeCode = rawMessage.typeCode();
        int caLSB = (Bits.extractUInt(payload, 48, 3));
        int codeMSB =  (14 - typeCode);
        int resultCategory = ((codeMSB << 4) | caLSB);

        for(int i = 0; i < 8; ++i) {
            int extractedChar = Bits.extractUInt(payload, 42 - 6 * i, 6);
            if((1 <= extractedChar && extractedChar <= 26) || (48 <= extractedChar && extractedChar <= 57)
                    || (extractedChar == 32)) {
                if(extractedChar <= 26) {
                    result.append((char) (extractedChar + 64));
                } else {
                    result.append((char) (extractedChar));
                }
            } else {
                return null;
             }
        }

        long resultTimeStampsNs = rawMessage.timeStampNs();
        IcaoAddress resultIcao = rawMessage.icaoAddress();
        CallSign resultCallSign = new CallSign(result.toString()
                .replaceFirst("\\s++$", ""));

        return new AircraftIdentificationMessage(resultTimeStampsNs, resultIcao, resultCategory, resultCallSign);
    }
}
