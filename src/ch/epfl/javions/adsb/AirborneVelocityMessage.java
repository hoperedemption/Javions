package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * The AirborneVelocityMessage
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param timeStampNs (long) : the time (in nanosecond) when we receive the message
 * @param icaoAddress (IcaoAddress) : the ICAO address of the airplane
 * @param speed (double) : the speed of the airplane in m/s
 * @param trackOrHeading (double) : the direction of the airplane
 */
public record AirborneVelocityMessage(long timeStampNs, IcaoAddress icaoAddress, double speed, double trackOrHeading) implements Message {

    /**
     * This is the index of the start bit of the subType.
     */
    public static int START_OF_SUBTYPE = 48;

    /**
     * This is the length of the subType
     */
    public static int LENGTH_OF_SUBTYPE = 3;

    /**
     * This is the start bit of the velocity message information
     */
    public static int START_OF_VELOCITY_MESSAGE_INFORMATION = 21;

    /**
     * This is the length of the velocity message information
     */
    public static int LENGTH_OF_VELOCITY_MESSAGE_INFORMATION = 22;

    /**
     * This constructor verifies if all the parameters are valid.
     *
     * @throws NullPointerException     : if the IcaoAddress is null
     * @throws IllegalArgumentException if one of the time stamp and the direction is negative (0 not included).
     */
    public AirborneVelocityMessage {
        Objects.requireNonNull(icaoAddress);
        Preconditions.checkArgument(timeStampNs >= 0);
        Preconditions.checkArgument(speed >= 0);
        Preconditions.checkArgument(trackOrHeading >= 0);
    }


    /**
     * This function returns an array containing all the information needed to create a velocity message. The indexes indicate
     * different things depending on the subtype of the message :
     * Subtype 1 or 2 : 0 -> VNS (+1) : the speed following the axis North-South (+1)
     *                  1 -> DNS : the direction is towards north if its value is 0, otherwise (if its value is 1) the direction is to south
     *                  2 -> VEW (+1) : the speed following the axis East-West (+1)
     *                  3 -> DEW : the direction is towards east if its value is 0, otherwise (if the value is 1) the direction is to the west
     * Subtype 3 or 4 : 0 -> SH : the availability of the heading
     *                  1 -> HDG : the value of the heading (*2^10)
     *                  2 -> T (but it is not used in this code)
     *                  3 -> AS (+1) : the value of the air speed (+1)
     * @param a (int) : the integer after the extraction from bit 21 to bit 42 of the ME.
     * @return (int[]) : the array containing all the above information needed to decode the VelocityMessage.
     */

    private static int[] extractInformation(int a) {
        int[] information = new int[4];
        information[0] = Bits.extractUInt(a, 0, 10); //VNS + 1, AS + 1
        information[1] = Bits.testBit(a, 10) ? 1 : 0; //DNS, T(not used)
        information[2] = Bits.extractUInt(a, 11, 10); // VEW + 1, HDG
        information[3] = Bits.testBit(a, 21) ? 1 : 0; //DEW, SH
        return information;
    }

    /**
     * This function tests if the subtype of the message is valid for the decoding. Indeed, if the subtype is not 1, 2, 3 or 4, this
     * function returns false meaning that the message cannot be decoded. Otherwise, it returns true.
     * @param st (int) : the subtype of the message we are treating.
     * @return (boolean) : true if the message is decodable, false otherwise.
     */
    private static boolean validST(int st) {
        return (st == 1 || st == 2 || st == 3 || st == 4);
    }

    /**
     * This function is only used when the subtype of the message is 1 or 2.
     * We use a cartesian plane to determine the adequate projections. Our x-axis represents the South (at the right)
     * and North (at the left) while our y-axis represents West (at the top) and East (at the bottom). If the
     * DNS (Direction North South) is 0 the projection is positive (if 1 negative respectively) and if the
     * DEW (Direction West East) is 0 the projection is positive (if 1 negative respectively).
     * After doing the projections we move on with the calculation of the angle. Since our choice of axis is the accurate
     * we just have to calculate the arc tangent of the quotient of the West East (y axis) component with the
     * North South (x-axis) component -> The angle thus obtained is the one separating north and the direction
     * in which the aircraft's nose is pointing, measured clockwise (thus we have a indirect frame of reference).
     * We only cover by convention angles in [0, 2pi) thus if the angle is negative we add a turn (2pi) to it.
     *
     * @param infos (int[]) : the array containing the information needed to do the computations
     * @return (double) : the clockwise angle from the north representing the heading of the plane.
     */
    private static double calculateAngleForSt1or2(int[] infos) {
        double vns = infos[0] - 1;
        double vew = infos[2] - 1;

        if(infos[1] != 0) {
            vns = -vns;
        }
        if(infos[3] != 0) {
            vew = -vew;
        }

        double angle = Math.atan2(vew, vns);
        if (angle < 0) {
            return angle + Units.Angle.TURN;
        } else {
            return angle;
        }
    }

    /**
     * This function decodes the airborne velocity message corresponding to the given raw message
     * considering multiple intermediate cases. If the case is 1 or 2, we verify that both of the VNS (+1) and the VEW (+1)
     * arguments are positive (meaning that the speeds on the different axis are not strictly negative), it is therefore decodable.
     * Also, if the message is of subtype 3 or 4, we verify that the cap is available (the SH is equal to 1) and that the speed
     * is not strictly negative. After testing all of that, we calculate the angle (if it is in subtype 3 or 4, the angle is first
     * calculated, then converted from turns to radians) and the speed. Furthermore, if the message is of subtype 2 or 4, we convert
     * the obtained speed from 4*knots to meter per seconds. And if it is of subtype 1 or 3, we convert the obtained speed
     * from knots to meter per seconds. Finally, the message is created with the timeStamp and
     * the ICAO address of the initial message, followed by the speed, the angle.
     * @param rawMessage (RawMessage) : the raw message that will be used to get the corresponding velocity message
     * @return (AirborneVelocityMessage) : the airborne velocity message corresponding to the raw message passed to the constructor
     * or null if the subtype is invalid, or if the speed or direction of movement cannot be determined.
     */
    public static AirborneVelocityMessage of(RawMessage rawMessage) {
        long me = rawMessage.payload();
        byte st = (byte) (Bits.extractUInt(me, START_OF_SUBTYPE, LENGTH_OF_SUBTYPE));

        if (!validST(st)) {
            return null;
        }

        double speed = 0;
        double angle = 0;

        int[] infos = extractInformation(Bits.extractUInt(me, START_OF_VELOCITY_MESSAGE_INFORMATION,
                LENGTH_OF_VELOCITY_MESSAGE_INFORMATION));

        if(nullCheckerForCertainCases(st, infos)) {
            return null;
        }
        switch (st) {
            case 1, 2 -> {
                double speedInKnots = Math.hypot(infos[0] - 1, infos[2] - 1);
                speed =  Units.convertFrom(speedInKnots, st == 1 ?  Units.Speed.KNOT : 4 * Units.Speed.KNOT);
                angle = calculateAngleForSt1or2(infos);
            }

            case 3, 4 -> {
                speed = Units.convertFrom(infos[0] - 1, st == 3 ? Units.Speed.KNOT : 4 * Units.Speed.KNOT);
                double angleInTurn = Math.scalb(infos[2], -10);
                angle = Units.convertFrom(angleInTurn, Units.Angle.TURN);
            }
        }

        return new AirborneVelocityMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(), speed, angle);
    }

    /**
     * This function verifies if the attributs of the messages are valid to allow the raw message to be analysed
     * @param st (byte) : the subtype of the raw message
     * @param infos (int[]) : the information used namely the VEW, for example
     * @return (boolean) : if the message can be analysed it returns true. Otherwise, it returns false
     */

    private static boolean nullCheckerForCertainCases(byte st, int[] infos) {
        return ((st == 1 || st == 2)  && (infos[0] == 0 || infos[2] == 0))
                || ((st == 3 || st == 4) && (infos[0] == 0 || infos[3] == 0));
    }
}