package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * The description of the aircraft
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param string (String) : the description of the aircraft
 */
public record AircraftDescription(String string) {
    /**
     * The pattern used to describe the Description of the plane
     */
    public static Pattern p = Pattern.compile("[ABDGHLPRSTV-][0123468][EJPT-]");
    /**
     * The compact constructor of this record validates the string passed to it and throws
     * an IllegalArgumentException if it does not represent a valid aircraft description.
     * This validation is done using the regular expression "[ABDGHLPRSTV-][0123468][EJPT-]".
     *
     * @param string the string containing the textual representation of the aircraft description.
     * @throws IllegalArgumentException if the string is empty or null, or if the string does not match the pattern that
     * is given ([ABDGHLPRSTV-][0123468][EJPT-]).
     */
    public AircraftDescription {

        Preconditions.checkArgument(string.isEmpty() || p.matcher(string).matches());
    }
}
