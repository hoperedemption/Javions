package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * The immatriculation of the airplane
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param string (String) : the immatriculation if the airplane
 */
public record AircraftRegistration(String string) {
    /**
     * The pattern used to describe the registration of the plane
     */

    public static Pattern p = Pattern.compile("[A-Z0-9 .?/_+-]+");
    /**
     * The compact constructor of this record validates the string passed to it and throws
     * an IllegalArgumentException if it does not represent a valid aircraft registration.
     * This validation is done using the regular expression "[A-Z0-9 .?/_+-]+".
     *
     * @param string (String) : the string containing the textual representation of the aircraft registration.
     * @throws IllegalArgumentException if the string is empty or null, or if the string des not match the given pattern
     * ([A-Z0-9 .?/_+-]+)
     */
    public AircraftRegistration {
        Preconditions.checkArgument(p.matcher(string).matches());
    }
}
