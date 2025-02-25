package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * The AirbornePositionMessage
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param timeStampNs (long) : the time (in nanosecond) when we receive the message
 * @param icaoAddress (IcaoAddress) : the ICAO address of the plane
 * @param altitude (double) : the altitude of the plane
 * @param parity (int) : the parity of the message (0 or 1)
 * @param x (double) : the longitude of the plane
 * @param y (double : the latitude of the plane
 */
public record AirbornePositionMessage(long timeStampNs, IcaoAddress icaoAddress, double altitude,
                                      int parity, double x, double y) implements Message {

    /**
     * The length of the altitude
     */
    public static final int LENGTH_ALTITUDE = 12;

    /**
     * This constructor verifies if all the given parameters are correct.
     * @throws IllegalArgumentException : if one parameter is incorrect
     */
    public AirbornePositionMessage {
        Objects.requireNonNull(icaoAddress);
        Preconditions.checkArgument(timeStampNs >= 0);
        Preconditions.checkArgument(parity == 0 || parity == 1);
        Preconditions.checkArgument(0 <= x && x < 1);
        Preconditions.checkArgument(0 <= y && y < 1);
    }

    /**
     * This function returns the corresponding AirbornePosition message according to a RawMessage that is given in parameter.
     * If the altitude is invalid while we are decoding the message, it returns null.
     *
     * @param rawMessage (RawMessage) : the initial rawMessage that we try to decode
     * @return (AirbornePositionMessage) : the decoded AirbornePositionMessage of the initial message or null if there is an invalid
     * character.
     */
    public static AirbornePositionMessage of(RawMessage rawMessage) {
        long me = rawMessage.payload();

        int lon_cpr = Bits.extractUInt(me, 0, 17);
        int lat_cpr = Bits.extractUInt(me, 17, 17);

        int format = Bits.extractUInt(me, 34, 1);
        int altitude = Bits.extractUInt(me, 36, LENGTH_ALTITUDE);

        double x = Math.scalb(lon_cpr, -17);
        double y = Math.scalb(lat_cpr, -17);

        byte q = (byte)Bits.extractUInt(altitude, 4, 1);

        int result = 0;

        if(q == 1) {
           int lsb = Bits.extractUInt(altitude, 0, 4);
           int msb = Bits.extractUInt(altitude, 5, 7);

           int multiple25 = (msb << 4) | lsb;
           result = -1000 + 25*multiple25;
        } else {
            int altitudeDetangled = altitudeDetangling(altitude);
            int multiple100LSB = Bits.extractUInt(altitudeDetangled, 0, 3);
            int multiple500MSB = Bits.extractUInt(altitudeDetangled, 3, 9);

            multiple100LSB = grayCodeDecoder(multiple100LSB);
            if(multiple100LSB == 0 || multiple100LSB == 5 || multiple100LSB == 6) {
                return null;
            } else if (multiple100LSB == 7) {
                multiple100LSB = 5;
            }

            multiple500MSB = grayCodeDecoder(multiple500MSB);
            if((multiple500MSB & 1) != 0) {
                multiple100LSB = 6 - multiple100LSB;
            }

            result = -1300 + multiple100LSB*100 + multiple500MSB*500;
        }

        return new AirbornePositionMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(),
                Units.convertFrom(result, Units.Length.FOOT), format, x, y);
    }

    private static int altitudeDetangling(int alt){
        int altitude = detangledHalfAltitude(alt) << 3;
        altitude = altitude | detangledHalfAltitude(alt >>> 6);
        return altitude;
    }

    private static byte compressingWithMask(int alt, int mask){
        if(mask == 0){
            return 0;
        }
        byte b = 0;
        int flag = LENGTH_ALTITUDE;
        while(flag >= 0){
           if((mask & (1 << flag)) != 0){
               b <<= 1;
               b |= Bits.extractUInt(alt, flag, 1);
           }
           --flag;
        }
        return b;
    }

    private static int detangledHalfAltitude(int alt){
        int halfAltitude  = 0;
        halfAltitude = (halfAltitude | compressingWithMask(alt, 21)) << 6;
        halfAltitude = (halfAltitude | compressingWithMask(alt, 42));
        return halfAltitude;
    }

    /**
     * This function decodes an integer using Gray code.
     * @param g (int) : the number to decode (interpreted as a Gray code)
     * @return (int) : the decoded integer.
     */
    private static int grayCodeDecoder(int g) {
        int n = 0;
        for(; g != 0; g >>= 1) {n ^= g;}
        return n;
    }

    /**
     * This function returns the time (in nanosecond) when we receive the message.
     * @return (long) : the time stamp when we receive the message
     */
    @Override
    public long timeStampNs() {
        return timeStampNs;
    }

    /**
     * This function returns the ICAO address of the aircraft
     * @return (IcaoAddress) : this function returns the ICAO address of the aircraft
     */
    @Override
    public IcaoAddress icaoAddress() {
        return icaoAddress;
    }
}
