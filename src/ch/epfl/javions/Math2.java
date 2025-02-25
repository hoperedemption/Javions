package ch.epfl.javions;

/**
 * Mathematics
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class Math2 {

    private Math2(){}

    /**
     * This function returns v if v is correcty bounded by min and max. Otherwise, it returns min if v is smaller than v or
     * max if v is bigger than max
     *
     * @param min (int) : the lower bound of the set
     * @param v (int) : the possible value of v
     * @param max (int) : the higher bound of the set
     * @return (int) v : if v is located between the two bound of the sets (which are min and max, included) then the actual
     * value of v is returned. However, if v is outside the set, then min or max is returned (depending respectively if v is lower
     * than min or higher than max).
     * @throws IllegalArgumentException if the min is bigger than the max which is illogical.
     */
    public static int clamp(int min, int v, int max){
        Preconditions.checkArgument(min <= max);
        return Math.max(min, Math.min(v, max));
    }

    /**
     * This function returns the hyperbolic arcsinus of an argument given in parameters
     *
     * @param x (double) : the angle
     * @return (double) : this function returns the Hyperbolic Arcsinus of a certain argument.
     */
    public static double asinh(double x){
        return Math.log(x + Math.hypot(1, x));
    }


}