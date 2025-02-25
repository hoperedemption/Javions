package ch.epfl.javions;

/**
 * The Geographic Position
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param longitudeT32 (int) : the longitude in T32
 * @param latitudeT32 (int) : the latitude in T32
 */
public record GeoPos(int longitudeT32, int latitudeT32) {

    /**
     * The maximum value of a latitude in T32
     */
    private static final int LIMIT = 1 << 30;

    /**
     * This function verifies if the latitude in T32 is correctly bounded
     * @param latitudeT32 (int) : the value of the latitude expressed in T32.
     * @return (boolean) : if the given latitude is bounded by -2^30 and 2^30, then it returns true. Indeed, the chosen latitude
     * is logic. Otherwise, it returns false.
     */
    public static boolean isValidLatitudeT32(int latitudeT32){
        return latitudeT32 <= LIMIT && latitudeT32 >= -LIMIT;
    }

    /**
     * Constructor of the GeoPos record
     * @throws IllegalArgumentException if the longitude or the latitude are invalidate.
     */
    public GeoPos {
        Preconditions.checkArgument(isValidLatitudeT32(latitudeT32));
    }

    /**
     * This function does the conversion of the longitude in radians
     * @return (double) : does the conversion of the longitude in radians
     */
    public double longitude(){
        return Units.convertFrom(longitudeT32, Units.Angle.T32);
    }

    /**
     * This function does the conversion of the latitude in radians
     * @return (double) : does the conversion of the latitude in radians
     */
    public double latitude(){
        return Units.convertFrom(latitudeT32, Units.Angle.T32);
    }

    /**
     * This function returns the value of the latitude and the longitude in degrees.
     * @return (String) : returns the value of the latitude and the longitude in degrees.
     */
    @Override
    public String toString(){
        return ("(" + Units.convert(longitudeT32, Units.Angle.T32 , Units.Angle.DEGREE) + "°, " + Units.convert(latitudeT32, Units.Angle.T32 , Units.Angle.DEGREE) + "°)");
    }
}