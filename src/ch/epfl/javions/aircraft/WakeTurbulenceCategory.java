package ch.epfl.javions.aircraft;

/**
 * The Wake Turbulence Category of the plane
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public enum WakeTurbulenceCategory {
    LIGHT, MEDIUM, HEAVY, UNKNOWN;

    /**
     * This function returns the associated enum depending on the turbulence category the plane is in.
     *
     * @param s string passed in argument that indicates the wake turbulence category
     * @return the associated enum, LIGHT for L / MEDIUM for M/ HEAVY for H/ UNKNOWN for any other case
     */
    public static WakeTurbulenceCategory of(String s) {
        return switch (s) {
            case "L" -> LIGHT;
            case "M" -> MEDIUM;
            case "H" -> HEAVY;
            default -> UNKNOWN;
        };
    }
}
