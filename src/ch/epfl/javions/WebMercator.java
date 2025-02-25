package ch.epfl.javions;


import static ch.epfl.javions.Units.Angle.TURN;

/**
 * The WebMercator
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */

public class WebMercator {
    private WebMercator() {}

    /**
     * This function returns the x coordinate on the world map
     *
     * @param zoomLevel (int) : a given zoom level of the world map.
     * @param longitude (double) : a given longitude
     * @return (double) : depending on the arguments, this function returns the x coordinate on the world map
     */
    public static double x(int zoomLevel, double longitude) {
        return Math.scalb(Units.convertTo(longitude, TURN) + 0.5, 8 + zoomLevel);
    }

    /**
     * This function returns the y coordinate on the world map
     *
     * @param zoomLevel (int) : a given zoom level of the world map.
     * @param latitude (double) : a given latitude
     * @return (double) : depending on the arguments, this function returns the y coordinate on the world map.
     */
    public static double y(int zoomLevel, double latitude) {
        return Math.scalb(Units.convertTo(-Math2.asinh(Math.tan(latitude)), TURN) + 0.5, 8 + zoomLevel);
    }
}
