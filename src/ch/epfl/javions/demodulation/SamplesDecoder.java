package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * The SampleDecoder
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class SamplesDecoder {

    /**
     * The bias used to recenter on 0.
     */
    public static final int BIAS = 2048;

    /**
     * The length in bytes of a short
     */
    public static final int BYTES_IN_SHORT = Short.BYTES;
    private final int batchSize;
    private final byte[] allBytes;

    private final InputStream stream;

    /**
     * Constructor of the class SamplesDecoder.
     *
     * @param  stream (InputStream) : given input stream of data,  bytes from the AirSpy radio
     * @param batchSize (batchSize) :
     * @throws IllegalArgumentException the size of the batches is not strictly positive
     * @throws NullPointerException if the stream is null
     */
    public SamplesDecoder(InputStream stream, int batchSize) {
        Preconditions.checkArgument(batchSize > 0);
        Objects.requireNonNull(stream);

        this.batchSize = batchSize;
        this.stream = stream;

        allBytes = new byte[BYTES_IN_SHORT * batchSize];
    }

    /**
     * Reads from the stream initially passed to the constructor the number of bytes corresponding to a batch,
     * then converts it by placing the first byte as the LSB and the byte after as the MSB and so on.
     * The first byte always contains the 8 least significant bits of the sample, while the second byte contains 4 null bits
     * and the 4 most significant bits of the sample. They are placed according the aforementioned explanation.
     * After these bytes are converted into signed samples, they are placed in the array passed as an argument;
     *
     * @param batch the array passed where the converted values will be saved
     * @return the number of samples actually converted and is always equal to the batch size except when the end
     *       of the stream was reached before enough bytes could be read, in which case it is equal to the number of bytes read divided by two,
     * @throws IOException if an input/output error occurs while reading the file
     * @throws IllegalArgumentException if the size of the array passed as an argument is not equal to the size of a batch.
     */
    public int readBatch(short[] batch) throws IOException {
        Preconditions.checkArgument(batchSize == batch.length);

        int readBytes = stream.readNBytes(allBytes, 0, BYTES_IN_SHORT * batchSize);
        int len = batchSize;

        if(readBytes != BYTES_IN_SHORT * batchSize) {
            len = readBytes / BYTES_IN_SHORT;
        }

        for(int i = 0; i < readBytes; i += 2) {
            byte lsb = allBytes[i];
            byte msb = allBytes[i + 1];

            short result = (short) ((Byte.toUnsignedInt(msb) << Byte.SIZE) | Byte.toUnsignedInt(lsb));
            result -= BIAS;

            batch[i/2] = result;
        }

        return len;
    }
}
