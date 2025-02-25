package ch.epfl.javions.gui;

import ch.epfl.javions.Preconditions;
import javafx.scene.paint.Color;

/**
 * The ColorRamp
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class ColorRamp {
    private final int numberOfColors;

    /**
     * The default colors used to create a scale
     */
    public static final ColorRamp PLASMA = new ColorRamp(
            Color.valueOf("0x0d0887ff"), Color.valueOf("0x220690ff"),
            Color.valueOf("0x320597ff"), Color.valueOf("0x40049dff"),
            Color.valueOf("0x4e02a2ff"), Color.valueOf("0x5b01a5ff"),
            Color.valueOf("0x6800a8ff"), Color.valueOf("0x7501a8ff"),
            Color.valueOf("0x8104a7ff"), Color.valueOf("0x8d0ba5ff"),
            Color.valueOf("0x9814a0ff"), Color.valueOf("0xa31d9aff"),
            Color.valueOf("0xad2693ff"), Color.valueOf("0xb6308bff"),
            Color.valueOf("0xbf3984ff"), Color.valueOf("0xc7427cff"),
            Color.valueOf("0xcf4c74ff"), Color.valueOf("0xd6556dff"),
            Color.valueOf("0xdd5e66ff"), Color.valueOf("0xe3685fff"),
            Color.valueOf("0xe97258ff"), Color.valueOf("0xee7c51ff"),
            Color.valueOf("0xf3874aff"), Color.valueOf("0xf79243ff"),
            Color.valueOf("0xfa9d3bff"), Color.valueOf("0xfca935ff"),
            Color.valueOf("0xfdb52eff"), Color.valueOf("0xfdc229ff"),
            Color.valueOf("0xfccf25ff"), Color.valueOf("0xf9dd24ff"),
            Color.valueOf("0xf5eb27ff"), Color.valueOf("0xf0f921ff"));
    /**
     * Minimum number of colours to set up a colour ramp
     */
    public static final int MIN_NUMBER_OF_COLOR = 2;

    private final Color[] colors;

    /**
     * The constructor of ColorRamp.
     * @param colors (Color ...) : a variable number of colors that are used to create a scale
     */
    public ColorRamp(Color... colors) {
        numberOfColors = colors.length;
        Preconditions.checkArgument(numberOfColors >= MIN_NUMBER_OF_COLOR);
        this.colors = colors.clone();
    }

    /**
     * this function returns the color located at the index x on the scale. If, after some manipulations, the index
     * is perfectly on a default color, then it returns the default color, otherwise, it returns a gradient of the
     * color that encircle it. Also, if the index is not in the scale it returns one of the bounds.
     * @param x (double) : the location of the color on a scale from 0 to 1
     * @return (Color) : the color corresponding to the index given.
     */
    public Color at(double x) {
        if(x <= 0) return colors[0];
        else if(x >= 1) return colors[numberOfColors -1];

        double index = (numberOfColors - 1) * x;
        int arrayIndex = (int) index;
        return colors[arrayIndex].interpolate(colors[arrayIndex + 1], index - arrayIndex);
    }
}
