package ch.epfl.javions;

import java.util.Objects;

/**
 * The Bits
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public class Bits {
    private Bits() {}

    /**
     * In this function, we extract a sequence of bits of length "size" from the given value from the starting point "start".
     * @param value (long) : vector of 64 bits
     * @param start (int) : index from which we start the extraction of the wanted byte.
     * @param size (int) : the number of bits that we want to extract starting from the input start.
     * @return (int) : this function returns the non-signed integer that was extracted between start and size.
     * @throws IllegalArgumentException if the size is negative or if we want to extract more than 32 bytes.
     * @throws IndexOutOfBoundsException if the start is negative or the bytes that we want to extract is not entirely stocked
     * in our initial array.
     */
    public static int extractUInt(long value, int start, int size) {
        Preconditions.checkArgument(size > 0 && size < Integer.SIZE);
        Objects.checkFromIndexSize(start, size, Long.SIZE);

        long ans = ((1 << size) - 1L) & (value >> start);

        return (int)(ans);
    }

    /**
     * This function returns the value of the given value. If the bit is one it returns true, otherwise it returns false.
     *
     * @param value (long) : the long number on which we want to test one given bit.
     * @param index (int) : it is the index of the number on which the bit is tested
     * @return (boolean) : returns true if the bit at the given index is true. Otherwise, it returns false.
     * @throws IndexOutOfBoundsException if the chosen index is not within the bounds of the number value.
     */
    public static boolean testBit(long value, int index) {
        Objects.checkIndex(index, Long.SIZE);

        return ((1L << index) & (value)) != 0;
    }
}
