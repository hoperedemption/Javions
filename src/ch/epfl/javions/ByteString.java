package ch.epfl.javions;

import java.util.*;

/**
 * A ByteString
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */

public final class ByteString {
    private final byte[] bytes;

    /**
     * The Hexformat allowing to manipulate hexadecimal numbers
     */
    private final static HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();

    /**
     * The constructor of the ByteString class. Clone the given array in bytes.
     * @param tab (byte[]) : the array that we want to copy
     */
    public ByteString(byte[] tab){
        bytes = tab.clone();
    }

    /**
     * This function returns an array of all the octets composing the number given as a parameter
     *
     * @param hexString (String) : The String possibly written in hexadecimal.
     * @return (ByteString) : returns an array of all the octets composing the number given as a parameter
     * @throws IllegalArgumentException if the String has not an even length.
     * @throws NumberFormatException if the number has not a legal argument of the hexadecimal alphabet in it.
     */
    public static ByteString ofHexadecimalString(String hexString){
        hexString = hexString.toUpperCase();
        Preconditions.checkArgument(hexString.length() % 2 == 0);

        byte[] bytes1 = HEX_FORMAT.parseHex(hexString);
        return new ByteString(bytes1);
    }

    /**
     * This function returns the length of the arrays of bytes
     * @return (int) : returns the length of the arrays of bytes
     */
    public int size() {
        return bytes.length;
    }

    /**
     * This function extract a byte at a certain position. (0 is the index of the most significant byte)
     *
     * @param index (int) : the index of the array where we want to take the byte from.
     * @return (int) : returns the extracted byte from the array (non-signed).
     * @throws IndexOutOfBoundsException if index is lower than 0 or higher than the length of the array of bytes.
     */
    public int byteAt(int index) {
        byte byteSearched = bytes[index];
        return Bits.extractUInt(byteSearched, 0, 8);
    }

    /**
     * This function extract a non-signed sequence of bits from a certain index to an end index (non-included)
     *
     * @param fromIndex (int) : the index from where we begin the extraction of the bytes.
     * @param toIndex (int) : the index where we stop the extraction (this one is excluded from the extraction)
     * @return (long) : the extracted byte (non-signed).
     * @throws IndexOutOfBoundsException if fromIndex is lower than 0 or toIndex is higher than the length of bytes
     * @throws IllegalArgumentException if toIndex is lower than fromIndex or if we want to extract more than 8 bytes.
     */
    public long bytesInRange(int fromIndex, int toIndex) {
        Objects.checkFromToIndex(fromIndex, toIndex, bytes.length);
        /*if(!(0 <= toIndex - fromIndex && toIndex - fromIndex <= 8)) {
            throw new IndexOutOfBoundsException();
        }*/
        long b = 0;
        for (int i = fromIndex; i < toIndex - 1; ++i){
            b = (b | byteAt(i));
            b = (b << 8);
        }

        b = (b | byteAt(toIndex-1));
        return b;
    }

    /**
     * This function states if the tested object is equal to the chosen ByteString.
     *
     * @param O (Object) : the Object on which we test the equality
     * @return (boolean) : returns true if the Object is a ByteString and is equal to the other ByteString. Otherwise,
     * it returns false.
     */
    @Override
    public boolean equals(Object O){
        if(O instanceof ByteString byteO){
            return Arrays.equals(byteO.bytes, this.bytes);
        }
        return false;
    }


    /**
     * This function returns the hashcode of a given array of bytes.
     * @return (int) : returns the hashcode of a given array of bytes.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * this function returns the writing of the given array the hexadecimal code.
     * @return (String) : this function returns the writing of the given array the hexadecimal code.
     */
    public String toString(){
        return HEX_FORMAT.formatHex(bytes);
    }

}