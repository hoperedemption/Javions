package ch.epfl.javions.gui;

import javafx.scene.paint.Color;

public class ColorRampTest {
    public static void main(String[] args) {
        Color c = Color.color(0.1, 0.2, 0.3);
        Color p = Color.color(0.9, 0.33, 0.11);
        Color u = Color.color(0.3, 0.12, 0.23);
        Color a = Color.color(0.5, 0.11, 0.23);
        Color l = Color.color(0.2, 0.11, 0.12);

     /*   Color b = Color.color(0.1, 0.11, 0.12);
        Color g = Color.color(0.3, 0.11, 0.12);
        Color al = Color.color(0.335, 0.11, 0.12);
        Color gl = Color.color(0.11, 0.11, 0.12);
        Color ol = Color.color(0.0002, 0.11, 0.12);
        Color dl = Color.color(0.13, 0.11, 0.12);*/


        System.out.println();
        ColorRamp ramp = new ColorRamp(c, p, u, a, l);
        Color result = ramp.at(0.3);


        System.out.println(result.getRed());
        System.out.println(result.getBlue());
        System.out.println(result.getGreen());

        System.out.println(0.7799999713897705 == 0.7799999713897705);
        System.out.println(0.1340000033378601 == 0.1340000033378601);
        System.out.println(0.2880000174045563 == 0.2880000174045563);
        /*
        0.7799999713897705
        0.1340000033378601
        0.2880000174045563
        */

        /*
        0.7799999713897705
        0.1340000033378601
        0.2880000174045563
        */
    }

}
