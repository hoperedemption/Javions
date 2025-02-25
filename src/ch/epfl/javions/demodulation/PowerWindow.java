package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * The PowerWindow
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */

public final class PowerWindow {

    /**
     * The length of a batch used here
     */
    private static final int BATCH_SIZE = 1 << 16;
    private int[] arrayOne;
    private int[] arrayTwo;
    private final int windowSize;
    private final PowerComputer powerComputer;
    private int indexStartWindow;
    private int indexEndWindow;
    private int indexLastPowerBytes;
    private long position;


    /**
     * The constructor of the class PowerWindow.
     * It creates the window of a certain size to allow it to go through all the powers, as well as
     * two "help" arrays of the same length that are used to stock the read powerBatches. Finally, it sees how many bytes are meant to be read.
     * Then, it calculates how many powerBatches in total are going to be read. This allows us to stop the reading of the powerBatches
     * when there are no more batches to be collected.
     *
     * @param stream (InputStream) : the initial stream of bytes that is given to be treated by the PowerComputer.
     * @param windowSize (int) : the actual size of the window
     * @throws IOException : if an input/output error occurs while reading the file
     */
    public PowerWindow(InputStream stream, int windowSize) throws IOException {
        Preconditions.checkArgument(0 < windowSize && windowSize <= BATCH_SIZE);

        position = 0;
        indexStartWindow = 0;
        indexEndWindow = (indexStartWindow + windowSize - 1) % BATCH_SIZE;

        this.windowSize = windowSize;

        arrayOne = new int[BATCH_SIZE];
        arrayTwo = new int[BATCH_SIZE];

        powerComputer = new PowerComputer(stream, BATCH_SIZE);
        indexLastPowerBytes = powerComputer.readBatch(arrayOne)-1;
    }

    /**
     * size
     * This function returns the size of the window that we use.
     * @return (int) : the size of the window
     */
    public int size(){
        return windowSize;
    }

    /**
     * This function returns the position of the window (more precisely the index of the starting of the window) in the first array
     * @return (long) : the current position of the window relative to the batch flow
     */
    public long position(){
        return position;
    }

    /**
     * This function verifies if the window is indeed full or not. To do so, we see if the end index of window is strictly bigger
     * than the index of the last Power byte that is decoded. Indeed, this situation can only happen when all the PowerBytes are read
     * and the window goes after the last decoded powerByte. This means that the window has at least one index empty : so it is not full.
     * @return (boolean) : true if the window is full, false otherwise.
     */
    public boolean isFull() { return !(indexEndWindow > indexLastPowerBytes); }

    /**
     * This function allows us to have access to a certain number within the window range at the index i starting from the beginning
     * index of the window.
     *
     * @param i (int) : the index of the power that we want to access starting from the beginning of the window
     * @return (int) :  the i-th element of the window.
     * @throws IndexOutOfBoundsException : if we try to access an element that s not inside the window's bounds
     */
    public int get(int i) {
        Objects.checkIndex(i, windowSize);
        checkValidityOnIndex(i);

        if (indexStartWindow + i >= BATCH_SIZE) {
            return arrayTwo[(indexStartWindow + i) % BATCH_SIZE];
        }
        return arrayOne[indexStartWindow + i];
    }

    /**
     * This function verifies if the given is valid to return a power sample. If it is not valid, it throws an
     * IndexOutOfBoundException.
     * @param i (int) : the index we want to verify its validity
     */
    private void checkValidityOnIndex(int i) {
        boolean inOneBatchIndexOutOfBounds = (indexEndWindow > indexStartWindow)
                && (indexStartWindow + i > indexEndWindow);
        boolean betweenTwoBatchesIndexOutOfBounds = (indexEndWindow < indexStartWindow)
                && (indexStartWindow + i >= BATCH_SIZE && (indexStartWindow + i) % BATCH_SIZE > indexEndWindow);
        if(inOneBatchIndexOutOfBounds || betweenTwoBatchesIndexOutOfBounds) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * This function allows to the window to advance by one index to the right. The description is the following :
     * 1) the starting and the ending indexes are increased by one modulo batchSize.
     * 2) if the ending index arrives to the 0, we store all the new powerBytes in the second array.
     * 3) if the starting index arrives to 0, we consider that the array storing the newest powerBytes is arrayOne.
     * 4) Finally, the position is increased by one at each time
     * Also, if the readBatch() of powerComputer returns a number that is inferior to batchSize-1, this means that we decoded
     * the last batch if powerBatch, thus we fix the variable "lastBatchDecoded" as true. And, if the last batch is decoded and
     * the ending index of the window arrives to 0, that signifies that the ending index is in the last batch.
     *
     * @throws IOException : If there is a problem in the input/output of the file that is read.
     */
    public void advance() throws IOException {
        indexStartWindow = (indexStartWindow + 1) % BATCH_SIZE;
        indexEndWindow = (indexEndWindow + 1) % BATCH_SIZE;

        if(indexEndWindow == 0){
            indexLastPowerBytes = powerComputer.readBatch(arrayTwo)-1;
        }

        if(indexStartWindow == 0){
            int[] temp = arrayOne;
            arrayOne = arrayTwo;
            arrayTwo = temp;
        }

        ++position;
    }

    /**
     * This function repeats the operation of advance() a desired number of time.
     *
     * @param offset (int) : the number of times we want to repeat advance().
     * @throws IOException : if there is a problem in the input/output of the file that is read.
     * @throws IllegalArgumentException : if the offset is not positive (0 is included)
     */
    public void advanceBy(int offset) throws IOException{
        {
            Preconditions.checkArgument( offset >= 0);

            for (int i = 0; i < offset; ++i) {
                advance();
            }
        }
    }
}