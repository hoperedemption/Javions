package ch.epfl.javions;

/**
 * The Units
 * @author Yassine El groaui (361984)
 * @author Alexandre Raybaut (355794)
 */

public final class Units {

    /**
     * The value of a unit divided by 100 : x10^(-2)
     */
    public static final double CENTI = 1e-2;

    /**
     * The value of a unit multiplied by 1000 : x10^3
     */
    public static final double KILO = 1e3;

    private Units() {}

    /**
     * Inner class Angle with multiple units (T32, degrees, radians and turns)
     */
    public static class Angle {
        private Angle() {}

        /**
         * The unit of the radian
         */
        public static final double RADIAN = 1;

        /**
         * The value of a turn in radian
         */
        public static final double TURN = 2*Math.PI * RADIAN;

        /**
         * The value of a degree in radian
         */
        public static final double DEGREE = TURN/360;

        /**
         * The value of a T32
         */
        public static final double T32 = Math.scalb(TURN, -32);
    }

    /**
     * Inner class Length with multiple units (meter, centimeter, kilometer, inch, foot, nautical mile)
     */
    public static class Length {
        private Length() {}

        /**
         * The unit of a meter
         */
        public static final double METER = 1;

        /**
         * The value of a centimeter
         */
        public static final double CENTIMETER = CENTI * METER;

        /**
         * The value of a kilometer
         */
        public static final double KILOMETER = KILO * METER;

        /**
         * The value of an inch
         */
        public static final double INCH = 2.54 * CENTIMETER;

        /**
         * The value of a foot
         */
        public static final double FOOT  = 12 * INCH;

        /**
         * The value of a nautical mile
         */
        public static final double NAUTICAL_MILE = 1852 * METER;
    }

    /**
     * Inner class Time with multiple units (second, hour, minute)
     */
    public static class Time {
        private Time() {}

        /**
         * The unit of a second
         */
        public static final double SECOND = 1;

        /**
         * The value of a minute in second
         */
        public static final double MINUTE = 60 * SECOND;

        /**
         * The value of an hour in second
         */
        public static final double HOUR = 60 * MINUTE;
    }

    /**
     * Inner class speed with multiple units (meter per second, knot, kilometer per hour)
     */
    public static class Speed {
        private Speed() {}

        /**
         * The unit of a meter per second
         */
        public static final double METER_PER_SECOND = 1;

        /**
         * The value of a knot which is by nautical mile per hour
         */
        public static final double KNOT = (Length.NAUTICAL_MILE)/(Time.HOUR);

        /**
         * The value of a kilometer per hour
         */
        public static final double KILOMETER_PER_HOUR = (Length.KILOMETER)/(Time.HOUR);
    }

    /**
     * This function converts a value from an initial position to a wanted value
     *
     * @param value (double) : the value that we wanted to convert to another unit
     * @param fromUnit (double) : the initial unit of the given value
     * @param toUnit (double) : the wanted unit of our value.
     * @return (double) : returns the conversion of our value from a certain unit to another depending on a given code
     * (to distinguish the different units).
     */
    public static double convert(double value, double fromUnit, double toUnit) {
        return value * (fromUnit / toUnit);
    }

    /**
     * This function converts a value from a non-basic unit to a basic unit
     *
     * @param value (double) : the value that we want to convert
     * @param fromUnit (double) : the initial unit.
     * @return (double) : using this function, we assume that the wanted unit of our value is the basic unit.
     * Thus, this function converts our value from a non-basic unit to the basic unit.
     */
    public static double convertFrom(double value, double fromUnit) {
        return convert(value, fromUnit, 1);
    }

    /**
     * This function converts a value from a basic unit to a non-basic unit
     *
     * @param value (double) :the value that we want to convert
     * @param toUnit (double) : the wanted unit.
     * @return (double) : using this function, we assume that the unit of our value is the basic unit. Thus, this function converts
     * our value from the basic unit to another unit.
     */
    public static double convertTo(double value, double toUnit) {
        return convert(value, 1, toUnit);
    }
}