package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * The type of the airplane
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param string (String) : the type of the airplane
 */


public record AircraftTypeDesignator(String string) {

    /**
     * The pattxern used to describe the Type designator
     */
    public static Pattern p = Pattern.compile("[A-Z0-9]{2,4}");
    /**
     * The compact constructor of this record validates the string passed to it and throws
     * an IllegalArgumentException if it does not represent a valid aircraft type designator.
     * This validation is done using the regular expression "[A-Z0-9]{2,4}"
     *
     * @param string (String) : the string containing the textual representation of the aircraft type designator.
     * @throws IllegalArgumentException if the string is empty or null, or if the string does not match the following pattern
     * ([A-Z0-9]{2,4})
     */
    public AircraftTypeDesignator {

        Preconditions.checkArgument(string.isEmpty() || p.matcher(string).matches());
    }
}
