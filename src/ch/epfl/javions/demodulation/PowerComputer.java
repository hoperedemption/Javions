package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;

/**
 * The PowerComputer
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class PowerComputer {
    private final SamplesDecoder decoder;
    private final int batchSize;
    private final short[] batchDecoded;
    private int front = 0;
    private int rear = 0;
    private final int[] array;

    /**
     * The constructor of powerComputer
     *
     * @param stream (InputStream) the give input stream of bytes from the AirSpy radio to be converted into
     *               their signed version using the SamplesDecoder attribute
     * @param batchSize the given size of the batches
     * @throws IllegalArgumentException if the size of the batches is not a multiple of 8 or
     * if the size is not  strictly positive
     */
    public PowerComputer(InputStream stream, int batchSize){
        Preconditions.checkArgument(batchSize > 0 && batchSize % 8 == 0);

        decoder = new SamplesDecoder(stream, 2*batchSize);
        batchDecoded = new short[2*batchSize];

        this.batchSize = batchSize;
        array = new int[8];

        for(int i = 0; i < 6; ++i){
            addLast(0);
        }
    }


    /**
     * This method reads from the sample decoder the number of samples needed to calculate a batch of power
     * samples, then calculates them using the aforementioned formula and places
     * them in the array passed as an argument;
     *
     * The formula used P[n] = [x[2n - 6] - x[2n - 4] + x[2n - 2] - x[2n]]^2
     *                         + [x[2n - 5] - x[2n - 3] + x[2n - 1] - x[2n + 1]]^2
     * The power batches are calculated with the method calculatePower() of BoundedQueue given inside the inner class.
     * This is possible due to the fact that the byte values are successively passed to the queue in groups of two.
     * Note that the queue itself is initialized inside the constructor thus there is no difference in reading a batch of 160
     * values in one go or reading them successively using batches of eight.

     * @param batch the array where the power samples shall be saved
     * @return (int) the number of power samples placed in the array,
     * @throws IllegalArgumentException if the size of the array passed as an argument is not equal to the size of a batch.
     */
    public int readBatch(int[] batch) throws IOException {
        Preconditions.checkArgument(batch.length == batchSize);

        int nbPower = 0;
        int nbDecoded = decoder.readBatch(batchDecoded);

        for(int i = 0; i < nbDecoded; i += 2){
            addLast(batchDecoded[i]);
            addLast(batchDecoded[i+1]);
            batch[i/2] = calculatePower();
            ++nbPower;

            removeFirst();
            removeFirst();
        }

        return nbPower;
    }

    /**
     * This function adds a new element at the end of our array to calculate a new Power.
     * @param newElement (int) : the new element to add to the array
     */
    private void addLast(int newElement) {
        rear = (rear + 1) & 0x7;
        array[rear] = newElement;
    }

    /**
     * This function removes the first element of our array and returns its value.
     * //J'ai enlevé le retour parce qu'il ne sert à rien.
     */
    private void removeFirst() {
        front = (front + 1) & 0x7;
    }

    /**
     * This function calculates a Power according to the elements present in the array
     * @return (int) : the calculated Power
     */
    private int calculatePower() {
        int b = array[(front) & 0x7] - array[(front + 2) & 0x7]+ array[(front + 4) & 0x7] - array[(front + 6) & 0x7];

        int a = array[(front + 1) & 0x7] - array[(front + 3) & 0x7] +array[(front + 5) & 0x7] - array[(front + 7) & 0x7];

        return a*a + b*b;
    }
}