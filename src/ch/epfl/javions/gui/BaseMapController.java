package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.WebMercator;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * The BaseMapController
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class BaseMapController {

    /**
     * The length of an edge of the tile.
     */
    public static final int TILE_DIMENSION = 256;
    private final TileManager tileManager;
    private final MapParameters mapParameters;
    private final Pane pane;
    private final Canvas canvas;
    private boolean redrawNeeded;
    private final GraphicsContext context;
    private final ObjectProperty<Point2D> cursorPoint = new SimpleObjectProperty<>();

    /**
     * This is the equatorial circumference on the Earth in kilometers.
     */
    private final static double EQUATORIAL_CIRCUMFERENCE_EARTH = 40_075.016686;

    /**
     * The longitude in radian of the top-left corner of the RLC
     */
    private final static double globalLon = Units.convertFrom(6.56, Units.Angle.DEGREE);

    /**
     * The latitude in radian of the top-left corner of the RLC
     */
    private final static double globalLat = Units.convertFrom(46.51, Units.Angle.DEGREE);


    /**
     * The constructor of the BaseMapController. It creates the canvas and the pane that will be used to draw
     * the map. Also, each time the mouse is used, the map parameters are updated.
     * @param tileManager (TileManager) : the tile manager that is used for the tile placement
     * @param mapParameters (MapParameters) : the map parameters used here
     */
    public BaseMapController(TileManager tileManager, MapParameters mapParameters) {
        this.tileManager = tileManager;
        this.mapParameters = mapParameters;

        canvas = new Canvas();
        pane = new Pane(canvas);
        context = canvas.getGraphicsContext2D();

        setUpCanvasAndPane();

        redrawNeeded = false;

        addListeners();
        scrollManager(new SimpleLongProperty());
        mouseManager();
    }

    /**
     * This function returns the number of pixels needed to represent a certain number of kilometer depending on a
     * certain zoom and a certain latitude.
     * @param kmWanted (double) : the number of kilometers that we want to represent in pixels.
     * @return (double) : the number of pixels needed to represent a certain number of kilometer.
     */
    private double concentricCircle(double kmWanted){
        int zoom = mapParameters.getZoom();
        double distanceOfPixel = (EQUATORIAL_CIRCUMFERENCE_EARTH *
                Math.cos(globalLat)) / Math.scalb(1, zoom+8);

        return kmWanted/distanceOfPixel;
    }

    /**
     * This methods draws a circle, on the canvas, corresponding to the given parameters on the map with
     * the given color.
     * @param wantedKm (double) : the number of kilometers that we want to represent
     * @param color (Color) : the color used to fill the circle
     */
    private void drawCircle(double wantedKm, Color color){
        context.setFill(color);
        double radius = concentricCircle(wantedKm);
        double x = WebMercator.x(mapParameters.getZoom(), globalLon);
        double y = WebMercator.y(mapParameters.getZoom(), globalLat);
        context.fillOval((x - mapParameters.getMinX()) - radius, (y - mapParameters.getMinY()) - radius,
                2*radius, 2*radius);
    }


    /**
     * This function draw all the tiles of the canvas.
     */
    private void draw() {
        //origin coordinate of the tile in the zoom level
        long firstTileXCoordinate = (long) Math.floor(mapParameters.getMinX() / TILE_DIMENSION);
        long firstTileYCoordinate = (long) Math.floor(mapParameters.getMinY() / TILE_DIMENSION);

        // relative position of the top left corner of the tile according to the canvas
        long relativePositionX = (long) (TILE_DIMENSION * firstTileXCoordinate - mapParameters.getMinX());
        long relativePositionY = (long) (TILE_DIMENSION * firstTileYCoordinate - mapParameters.getMinY());

        for (long x = 0; x < canvas.getWidth() / TILE_DIMENSION + 1; ++x) {
            for (long y = 0; y < canvas.getHeight() / TILE_DIMENSION + 1; ++y) {
                try {
                    long xTile = x + firstTileXCoordinate;
                    long yTile = y + firstTileYCoordinate;
                    if(TileManager.TileId.isValid(mapParameters.getZoom(), xTile, yTile)) {
                        Image image = tileManager.imageForTileAt(new TileManager.TileId(mapParameters.getZoom(),
                                xTile, yTile));
                        context.drawImage(image, TILE_DIMENSION * x + relativePositionX,
                                TILE_DIMENSION * y + relativePositionY);
                    }
                } catch (IOException ignored) {/*exception ignored*/}
            }
        }

        //Drawing the concentric circles
        if(mapParameters.getZoom() <= 15){
            context.setGlobalAlpha(0.3);
            drawCircle(200, Color.RED);
            drawCircle(100, Color.YELLOW);
            drawCircle(50, Color.GREEN);
            context.setGlobalAlpha(1);
        }
    }

    /**
     * If a redraw was requested, it draws the map and reset the redrawNeeded attribute
     */
    private void redrawIfNeeded(){
        if (!redrawNeeded) return;
        redrawNeeded = false;
        draw();
    }

    /**
     * This function request a redraw of the map
     */
    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }

    /**
     * This function is a getter of the pane
     * @return (Pane) : returns the pane
     */
    public Pane pane() {
        return pane;
    }

    /**
     * This function centers the map on the given position.
     * @param position (GeoPos) : the position we want to center on the map.
     */
    public void centerOn(GeoPos position) {
        double x = WebMercator.x(mapParameters.getZoom(), position.longitude());
        double y = WebMercator.y(mapParameters.getZoom(), position.latitude());

        mapParameters.scroll(x - mapParameters.getMinX(), y - mapParameters.getMinY());
        mapParameters.scroll(-canvas.getWidth()/2, -canvas.getHeight()/2);
    }


    /**
     * This function sets up the pane and the canvas. Also, it binds the canvas with the pane.
     */
    private void setUpCanvasAndPane() {
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
    }

    /**
     * This function manages the scroll of the mouse.
     * This adapts the zoom level and the minX and minY parameters
     * @param minScrollTime (longProperty) : minimum time to scroll
     */
    private void scrollManager(LongProperty minScrollTime) {
        pane.setOnScroll(e -> {
            int zoomDelta = (int) Math.signum(e.getDeltaY());
            if (zoomDelta == 0) return;

            long currentTime = System.currentTimeMillis();
            if (currentTime < minScrollTime.get()) return;
            minScrollTime.set(currentTime + 200);

            this.cursorPoint.set(new Point2D(e.getX(), e.getY()));

            updateMapParameters(zoomDelta);
        });
    }

    /**
     * This function is called to adapt the new minX and minY parameters. Firstly, we set the point on which the mouse
     * is as the new minX and minY. Then it changes the zoom level. After that we cancel the setting done before by
     * scrolling back to our old minX and minY
     * @param zoomDelta (int) : the difference requested to be applied to the old zoom level
     */
    private void updateMapParameters(int zoomDelta) {
        mapParameters.scroll(cursorPoint.get().getX(), cursorPoint.get().getY());
        mapParameters.changeZoomLevel(zoomDelta);
        mapParameters.scroll(-cursorPoint.get().getX() , -cursorPoint.get().getY());
    }

    /**
     * This function adds listeners to all the attributes of mapParameters and also to the width and the height of the
     * canvas. Meaning that if one of these changes, it requests a redraw one the map.
     */
    private void addListeners() {
        mapParameters.getZoomProperty().addListener((p, oldS, newS) -> redrawOnNextPulse());
        mapParameters.getMinXProperty().addListener((p, oldS, newS) -> redrawOnNextPulse());
        mapParameters.getMinYProperty().addListener((p, oldS, newS) -> redrawOnNextPulse());
        canvas.widthProperty().addListener((p, oldS, newS) -> redrawOnNextPulse());
        canvas.heightProperty().addListener((p, oldS, newS) -> redrawOnNextPulse());
    }

    /**
     * This function manages the usage of the mouse
     */
    public void mouseManager(){
        pane.setOnMousePressed(e -> cursorPoint.set(new Point2D(e.getX(), e.getY())));
        pane.setOnMouseDragged(e -> {
            mapParameters.scroll(cursorPoint.get().getX() - e.getX(), cursorPoint.get().getY() - e.getY());
            cursorPoint.set(new Point2D(e.getX(), e.getY()));
        });
        pane.setOnMouseReleased(e -> cursorPoint.set(null));
    }
}
