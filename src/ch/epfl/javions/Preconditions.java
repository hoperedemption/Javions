package ch.epfl.javions;

/**
 * A Precondition
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */

public final class Preconditions {
    private Preconditions() {}

    /**
     * This function verifies if the parameter given is true or not.
     * @param shouldBeTrue (boolean) : the truth of the given argument.
     * @throws IllegalArgumentException if the argument is false.
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if(!shouldBeTrue)
            throw new IllegalArgumentException();
    }
}
