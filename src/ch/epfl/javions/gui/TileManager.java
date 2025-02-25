package ch.epfl.javions.gui;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;

/**
 * A TileManager
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class TileManager {
    /**
     * The maximal capacity possible of our local cache memory
     */
    public static final int MAXIMAL_CAPACITY = 100;
    private final Path path;
    private final String serverName;
    private final LinkedHashMap<TileId, Image> tileIdImageLinkedHashMap;


    /**
     * The constructor of the TileManager. Also, it creates the memory disk with a LinkedHashMap
     * @param path (Path) : the path of the directory containing the cache disk
     * @param serverName (String) : the name of the server containing the tiles to get
     */
    public TileManager(Path path, String serverName) {
        this.path = path;
        this.serverName = serverName;
        tileIdImageLinkedHashMap = new LinkedHashMap<>(MAXIMAL_CAPACITY, 0.75f, true) {

            /**
             * This function avoids doing a redundant initialisation of an iterator just to remove an element.
             * According to official java documentation, this function automatically removes the eldest element
             * when the size is at its maximal capacity when a new element is added.
             *
             * @param eldest The least recently inserted entry in the map, or if
             *           this is an access-ordered map, the least recently accessed
             *           entry.  This is the entry that will be removed it this
             *           method returns {@code true}.  If the map was empty prior
             *           to the {@code put} or {@code putAll} invocation resulting
             *           in this invocation, this will be the entry that was just
             *           inserted; in other words, if the map contains a single
             *           entry, the eldest entry is also the newest.
             * @return (boolean) the size of the map is bigger than the maximal capacity
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<TileId, Image> eldest) {
                return size() > MAXIMAL_CAPACITY;
            }
        };
    }

    /**
     * This function returns the image corresponding to a certain tile.
     * @param tileId (TileId) : the identity of the tile we want to get
     * @return (Image) : the image corresponding to the tile identity given
     * @throws IOException : If there is a problem with some files
     */
    public Image imageForTileAt(TileId tileId) throws IOException {
        Image image;
        Path resultPath = resultPath(tileId);
        Path fileResultingPath;

        if((image = tileIdImageLinkedHashMap.get(tileId)) != null){
            return image;
        }else if(Files.exists(fileResultingPath = resultPath.resolve((int)tileId.y() + ".png"))){
            try(InputStream stream = new FileInputStream(fileResultingPath.toString())) {
                image = new Image(stream);
                tileIdImageLinkedHashMap.put(tileId, image);
                return image;
            }
        } else {
            URLConnection c = getUrlConnection(tileId);
            try(InputStream i = c.getInputStream()) {
                image = readImage(tileId, resultPath, i);
                return image;
            }
        }
    }

    /**
     * This function returns the path leading to a certain tile in the cache disk
     * @param tileId (TileId) : the tile identity that we want to stock
     * @return (Path) : the path leading the corresponding tile in the cache disk
     */
    private Path resultPath(TileId tileId){
        return path.resolve(Path.of(Integer.toString(tileId.zoom()))
                .resolve(Integer.toString((int)tileId.x())));
    }

    /**
     * This function returns the URL that leads to the website from which we collect the wanted tiles.
     * @param tileId (TileId) : the tile identity used to get a given tile.
     * @return (URLConnection) : the URL that leads to the website allowing us to collect the tiles
     * @throws IOException : if there is a problem during the collection
     */
    private URLConnection getUrlConnection(TileId tileId) throws IOException {
        URL u = new URL("https://" + serverName + "/" +  tileId.zoom + "/" + tileId.x + "/" + tileId.y + ".png");
        URLConnection c = u.openConnection();
        c.setRequestProperty("User-Agent", "Javions");
        return c;
    }

    /**
     * This function returns the image recently created in the cache disk.
     * @param tileId (TileId) : the identity of the tile we want to stock in the cache disk
     * @param resultingPath (Path) : the path leading to the tile we want to write in the cache disk
     * @param i (InputStream) : the stream used to read the bytes needed to create the image
     * @return (Image) : the created image in the cache disk
     * @throws IOException : If there is a problem during the reading or the writing of the image
     */
    private Image readImage(TileId tileId, Path resultingPath, InputStream i) throws IOException {
        byte[] memory = i.readAllBytes();
        InputStream stream = new ByteArrayInputStream(memory);

        Image image = new Image(stream);
        tileIdImageLinkedHashMap.put(tileId, image);

        File file = new File(resultingPath.toString(), tileId.y() + ".png");
        Files.createDirectories(resultingPath);
        try(OutputStream s = new FileOutputStream(file)) {
            s.write(memory);
        }
        return image;
    }

    /**
     * The inner record TileId
     * @param zoom (int) : the zoom of the map
     * @param x (long) : the x coordinate of the tile in the map
     * @param y (long) : the y coordinate of the tile in the map.
     */
    public record TileId(int zoom, long x, long y) {

        /**
         * This function indicates if the given parameters are valid for a tile.
         * @param zoom (int) : the zoom of the analysed tile
         * @param x (long) : the x coordinates of the tile
         * @param y (long) : the y coordinates of the tile
         * @return (boolean) : if the parameters of the tile are valid it returns true. Otherwise, it returns false.
         */
        public static boolean isValid(int zoom, long x, long y) {
            long maxCoord = (1L << (2 * zoom - 1));
            return (0 <= x && x < maxCoord) && (0 <= y && y < maxCoord);
        }
    }
}