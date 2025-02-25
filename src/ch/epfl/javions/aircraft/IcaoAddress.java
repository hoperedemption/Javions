package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * The ICAO address of the aircraft
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param string (String) : the OACI adress of the plane
 */

public record IcaoAddress(String string) {
    /**
     * The used pattern to describe all the possible ICAO Addresses
     */
     private final static Pattern p = Pattern.compile("[0-9A-F]{6}");

    /**
     * The compact constructor of this record validates the string passed to it and throws
     * an IllegalArgumentException if it does not represent a valid ICAO address.
     * This validation is done using the regular expression "[0-9A-F]{6}".
     *
     * @param string the string containing the textual representation of the ICAO address.
     * @throws IllegalArgumentException if the string is empty or null, or if the string does not match the following
     * pattern ([0-9A-F]{6}).
     */
    public IcaoAddress {
        Preconditions.checkArgument(p.matcher(string).matches());
    }
}
