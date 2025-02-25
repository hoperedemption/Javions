package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.ByteString;
import ch.epfl.javions.Crc24;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.HexFormat;

/**
 * The RawMessage
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param timeStampNs (long) : the time in nanosecond when we receive the message
 * @param bytes (ByteString) : the bytes of the message
 */

public record RawMessage(long timeStampNs, ByteString bytes) {

    /**
     * The basic CRC24 used for a raw message
     */
    private static final Crc24 CRC24 = new Crc24(Crc24.GENERATOR);

    /**
     * The length of an ADS-B message
     */
    public static final int LENGTH = 14;

    /**
     * The bit of start of the DF attribut of an ADS-B message
     */
    public static final int DF_START = 3;

    /**
     * The length of the DF attribut of an ADS-B message
     */
    public static final int DF_SIZE = 5;

    /**
     * The only valid DF attribute used for an ADS-B message here
     */
    public static final int VALID_TYPE_CHECKER = 17;

    /**
     * The constructor of the RawMessage. It verifies if the parameters are correct.
     */
    public RawMessage {
        Preconditions.checkArgument(timeStampNs >= 0);
        Preconditions.checkArgument(bytes.size() == LENGTH);
    }

    /**
     * This function verifies in the crc24 of the bytes' message is correct and returns  0, meaning that the message is correct.
     * If the message is correct we build a RawMessage with a given array and a given timeStamp. Otherwise, this function
     * returns null.
     *
     * @param timeStampNs (long) : the time in nanosecond when we receive the message
     * @param bytes (byte[]) : the bytes of the message
     * @return (RawMessage) : the decoded message
     */
    public static RawMessage of(long timeStampNs, byte[] bytes) {
        int crcValue = CRC24.crc(bytes);
        return crcValue == 0 ? new RawMessage(timeStampNs, new ByteString(bytes)) : null;
    }

    /**
     * This function returns the size of the message depending on the value of the DF. If the DF is equal to 17, the size of the
     * message is LENGTH (14), otherwise, it is equal to 0.
     *
     * @param byte0 (byte) : the most significant byte of the message
     * @return (int) : the length of the message
     */
    public static int size(byte byte0) {
        int df = Bits.extractUInt(byte0, DF_START, DF_SIZE);
        return df == VALID_TYPE_CHECKER ? LENGTH : 0;
    }

    /**
     * This function returns the type code of the message which are the bits 51 to 56.
     *
     * @param payload (long) : the ME value of the message, its useful charge.
     * @return (int) : the type code of the message
     */
    public static int typeCode(long payload) {
        return Bits.extractUInt(payload, 51, DF_SIZE);
    }

    /**
     * This function returns the DF value of the message.
     *
     * @return (int) : this function returns the DF value of the message.
     */
    public int downLinkFormat() {
        return Bits.extractUInt(bytes.byteAt(0), DF_START, DF_SIZE);
    }

    /**
     * This function returns the OACI address of the message.
     * @return (IcaoAddress) : this function returns the OACI address of the message.
     */
    public IcaoAddress icaoAddress() {
        //return new IcaoAddress(bytes.toString().substring(2, 8));
        return new IcaoAddress(HexFormat.of().withUpperCase().toHexDigits(bytes.bytesInRange(1, 4), 6));
    }

    /**
     * This function returns the ME value of the message, its useful charge.
     * @return (long) : this function returns the ME value of the message, its useful charge.
     */
    public long payload() {
        return bytes.bytesInRange(4, 11);
    }

    /**
     * This function returns the type code of the message which are the bits 51 to 56.
     *
     * @return (int) : the type code of the message
     */
    public int typeCode() {
        return typeCode(payload());
    }
}
