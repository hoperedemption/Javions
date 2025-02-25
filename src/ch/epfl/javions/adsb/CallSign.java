package ch.epfl.javions.adsb;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * The CallSign
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 *
 * @param string (String) : the call sign of the aircraft.
 */
public record CallSign(String string) {

    /**
     * The pattern used to describe a call sign
     */
    public static final Pattern p = Pattern.compile("[A-Z0-9 ]{0,8}");

    /**
     * The constructor of the CallSign. It verifies if the call sign is not null or empty, or if the call sign match the
     * following pattern ([A-Z0-9 ]{0,8})
     *
     * @param string (String) : the call sign of the aircraft
     */
    public CallSign{
        Preconditions.checkArgument(string.isEmpty() || p.matcher(string).matches());
    }
}