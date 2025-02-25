package ch.epfl.javions;

/**
 * A CRC24
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class Crc24 {

    /**
     * The basic generator of CRC24 in this project
     */
    public static final int GENERATOR = 0xFFF409;

    /**
     * The length of the CRC used here
     */
    private static final int N = 24;

    /**
     * The total number of values represented by a byte
     */
    public static final int MAX_BYTE_LENGTH = 256;

    /**
     * The number of bits used in a byte
     */
    public static final int BYTE_SHIFT = Byte.SIZE;
    private final int generator;
    private final int[] lookUpTable;

    /**
     * This is the constructor of the CRC24. It takes a certain generator and set it as the one that we will use to decode
     * all the messages
     * @param generator (int) : the generator that is used to decode the messages
     */
    public Crc24(int generator) {
        this.generator = generator;
        lookUpTable = BuildTable();
    }

    /**
     * This function returns the CRC24 of a certain byte that is non-augmented.
     *
     * @param bytes (bytes[]) : the byte that is used to calculate its crc24.
     * @return (int) : this functions returns the crc24 of an initial array containing a single byte. It takes it, increases it with
     * 3 more bytes containing only zeros, then apply the division to obtain the crc24 of the parameter bytes. Firstly,
     * it shifts the crc by one and perform an OR operation with all the ith bits of the dividend. If the 24th bit of the crc
     * is a zero, we xor the dividend by a zero, otherwise we xor it with the 3 least significant bytes of the generator.
     * Finally, we return the 24 least significant bits of the calculated crc.
     */
    public int crc_bitwise(byte[] bytes) {
        int crc = 0;
        int newGenerator = Bits.extractUInt(generator, 0, N);

        int[] table = {0, newGenerator};

        for (byte Bytes : bytes) {
            for (int i = 0; i < BYTE_SHIFT; ++i) {
                crc = ((crc << 1) | Bits.extractUInt(Bytes, 7 - i, 1)) ^ table[Bits.extractUInt(crc, N-1, 1)];
            }
        }

        for(int i = 0; i < N; ++i){
            crc = (crc << 1) ^ table[Bits.extractUInt(crc, N-1, 1)];
        }

        return Bits.extractUInt(crc, 0, N);
    }

    /**
     * This function returns the lookUp table containing the crc24 of all the first 256 bytes
     *
     * @return (int[]) : returns the lookUp table containing the crc24 of all the first 256 bytes
     * (thus from 0 to 255).
     */
    public int[] BuildTable(){
        int[] table1 = new int[MAX_BYTE_LENGTH];
        for(int i = 0; i < MAX_BYTE_LENGTH; ++i){
            table1[i] = crc_bitwise(new byte[] {(byte) i});
        }
        return table1;
    }

    /**
     * It returns the CRC of length 24 of a non-augmented message
     *
     * @param bytes (bytes[]) : the byte that is used to calculate its crc24.
     * @return (int) : this functions returns the crc24 of an initial array containing a single byte. It applies the division to obtain
     * the crc24 of the parameter bytes. Firstly, it shifts the crc by one and perform an OR operation with all the ith bits of the dividend.
     * If the 24th bit of the crc is a zero, we xor the dividend by a zero, otherwise we xor it with the 3 least significant bytes of the generator.
     * Then, it adds three bytes (of a value of zero) at the end of the message (to transform into an augmented one). Then it reiterates the
     * previous operation. The only difference is that all the bits of b are zeros, so we can reduce the previous operation to a shift of 1 of the crc
     * then a xor with the 23rd bit of crc24 that is being calculated. Finally, we return the 24 least significant bits of the calculated crc.
     */
    public int crc(byte[] bytes){
        int crc = 0;

        for (byte aByte : bytes) {
            crc = ((crc << BYTE_SHIFT) | Bits.extractUInt(aByte, 0, BYTE_SHIFT)) ^ lookUpTable[Bits.extractUInt(crc, N - BYTE_SHIFT, BYTE_SHIFT)];
        }

        for(int i = 0; i < 3; ++i){
            crc = (crc << BYTE_SHIFT) ^ lookUpTable[Bits.extractUInt(crc, N - BYTE_SHIFT, BYTE_SHIFT)];
        }

        return Bits.extractUInt(crc, 0, N);
    }
}