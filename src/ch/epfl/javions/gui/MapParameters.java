package ch.epfl.javions.gui;

import ch.epfl.javions.Math2;
import ch.epfl.javions.Preconditions;
import javafx.beans.property.*;

/**
 * The map parameters
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class MapParameters {
    /**
     * The maximal possible zoom
     */
    public static final int MAX_ZOOM = 19;

    /**
     * The minimal possible zoom
     */
    public static final int MIN_ZOOM = 6;

    private final IntegerProperty zoom = new SimpleIntegerProperty();
    private final DoubleProperty minX = new SimpleDoubleProperty();
    private final DoubleProperty minY = new SimpleDoubleProperty();

    /**
     * The constructor of MapParameters.
     *
     * @param zoom (int) : the zoom used at a certain moment to look at the map
     * @param minX (double) : the coordinate of the top left corner of the visible part of the map
     * @param minY (double) : the coordinate of the top left corner of the visible part of the map
     */
    public MapParameters(int zoom, double minX, double minY) {
        Preconditions.checkArgument(MIN_ZOOM <= zoom && zoom <= MAX_ZOOM);
        this.zoom.set(zoom);
        this.minX.set(minX);
        this.minY.set(minY);
    }

    //Zoom :

    /**
     * The getter of the zoom property
     *
     * @return (ReadOnlyIntegerProperty) : the zoom property
     */
    public ReadOnlyIntegerProperty getZoomProperty() {
        return zoom;
    }

    /**
     * The getter of the value of the zoom property
     *
     * @return (int) : the value of the zoom property
     */
    public int getZoom() {
        return zoom.get();
    }

    //MinX :

    /**
     * The getter of the MinX property
     *
     * @return (ReadOnlyDoubleProperty) : the minX property
     */
    public ReadOnlyDoubleProperty getMinXProperty() {
        return minX;
    }

    /**
     * The getter of the value of the minX property.
     *
     * @return (double) : the value of the minX property
     */
    public double getMinX() {
        return minX.get();
    }

    /**
     * The getter of the minY property
     *
     * @return (ReadOnlyDoubleProperty) : the minY property
     */
    //MinY :
    public ReadOnlyDoubleProperty getMinYProperty() {
        return minY;
    }

    /**
     * The getter of the value of the minY property
     *
     * @return (double) : the value of the minY property
     */
    public double getMinY() {
        return minY.get();
    }

    /**
     * This functions does the scroll of the map in a certain direction according to the given parameters
     *
     * @param x (double) : the x coordinate of the vector we want to advance by
     * @param y (double) : the y coordinate of the vector we want to advance by
     */
    public void scroll(double x, double y) {
        minX.set(getMinX() + x);
        minY.set(getMinY() + y);
    }

    /**
     * This function changes the zoom level used to look the map and adapt the minX and minY parameters according
     * to the new zoom level. If we are already at the edges (namely 6 or 19), this function does nothing.
     *
     * @param dzoom (int) : the difference that is asked to add to the current zoom level
     */
    public void changeZoomLevel(int dzoom) {
        int oldZoom = getZoom();
        zoom.set(Math2.clamp(MIN_ZOOM, getZoom() + dzoom, MAX_ZOOM));
        if ((getZoom() - oldZoom) != 0) {
            minX.set(Math.scalb(getMinX(), dzoom));
            minY.set(Math.scalb(getMinY(), dzoom));
        }
    }
}