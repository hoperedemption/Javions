package ch.epfl.javions.demodulation;

import ch.epfl.javions.adsb.RawMessage;
import java.io.IOException;
import java.io.InputStream;

/**
 * The ADS-B Demodulator
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class AdsbDemodulator {

    /**
     * The length of an ADS-B message
     */
    public static final int MESSAGE_LENGTH = 112;

    /**
     * The length of a byte in bits
     */
    public static final int BYTE_SIZE = Byte.SIZE;

    /**
     * The number of sample constituting the preamble
     */
    public static final int SAMPLES_NUMBER = 80;

    /**
     * The length of the PowerWindow used to decode a sequence of ADS-B messages
     */
    private static final int WINDOW_SIZE = 1200;
    private final PowerWindow window;
    private final byte[] message;

    /**
     * The constructor of the adsb demodulator. It creates a window of size 1200.
     *
     * @param inputStream (InputStream) : the stream of data that is given to be read and decoded
     * @throws IOException if there is a problem in the reading of the file.
     */
    public AdsbDemodulator(InputStream inputStream) throws IOException {
        window = new PowerWindow(inputStream, WINDOW_SIZE);
        message = new byte[14];
    }

    /**
     * This function returns the next decoded message of the data stream. However, it does it under certain conditions :
     * - Firstly, we must have SumP(0) > SumP(1) and SumP(0) > SumP(-1)
     * - Secondly, SumP(0) should be greater or equal to 2*SumV()
     * - Finally, the DF of the message should be equal to 17
     *
     * If all the above conditions are true, we decode the message. After that, we still need to verify a couple of things :
     * we calculate the crc24 of the decoded message, and, if the crc is correct, we can return the message and then advance the window
     * by 1200 (because all the elements of the window were already used to decode the message, so we cannot use them twice for a
     * different message). However, if the crc24, is not correct, we return null because the message is not interesting. And, we advance
     * by one because we can still find a message.
     *
     * @return (RawMessage) : a decoded message at a certain position in the stream of data.
     * @throws IOException : if there is a problem in the reading of the file.
     */
    public RawMessage nextMessage() throws IOException{
        int previousSumP = 0;
        int currentSumP = 0;
        int nextSumP = 0;

        for(;window.isFull(); window.advance()){
            nextSumP = window.get(1) + window.get(11) + window.get(36) + window.get(46);

            if(currentSumP > nextSumP && currentSumP > previousSumP && currentSumP >= 2*calculateSumV()
                        && TestIfCorrectMessage()){
                getMessage();
                RawMessage result = RawMessage.of(window.position()*100, message);
                if(result != null) {
                    window.advanceBy(WINDOW_SIZE);
                    return result;
                }
            }
            previousSumP = currentSumP;
            currentSumP = nextSumP;
        }

        return null;
    }

    /**
     * This function calculates the Sum V with the following formula : w5 + w15 + w20 + w30 + w40.
     * @return (int) : the sun V of the window.
     */
    private int calculateSumV(){
        return (window.get(5) + window.get(15) + window.get(20) + window.get(25) + window.get(30) + window.get(40));
    }

    /**
     * This function store all the bytes in a byte array to decode in later as a ByteString. This array represents a decoded message.
     * At the start, b is equal to 17 because this function is only called if the DF is equal to 17. Otherwise, we don't call it.
     * After that, going from the most significant bit to the least one, we decode each bit :
     * 1) if w80 + 10*i >= w85 + 10*i : then the decoded bit is 1.
     * 2) At the contrary, it is 0.
     */
    private void getMessage(){
        byte b = 17;

        for(int i = 5; i < MESSAGE_LENGTH; ++i){
            b = decodeIndividualBit(b, i);

            if((i + 1) % BYTE_SIZE == 0){
                message[i / BYTE_SIZE] = b;
                b = 0;
            }
        }
    }
    /**
     * This function verifies if the DF is indeed equal to 17. Indeed, if not, the message is not taken into consideration.
     * @return (boolean) : the DF is indeed equal to 17.
     */
    private boolean TestIfCorrectMessage(){
        byte b = 0;

        for(int i = 0 ; i < 5; ++i){
            b = decodeIndividualBit(b, i);
        }
        return b == 17;
    }

    /**
     * This function return the decoded bit at a certain index and append it at the end of the given byte.
     * @param b (byte) : the byte we want to complete
     * @param i (int) : the index at which we check the bit
     * @return (byte) : the constructed byte after the appending of the bit at the index i, according to a formula.
     */
    private byte decodeIndividualBit(byte b, int i) {
        if(!(window.get(SAMPLES_NUMBER + 10* i) < window.get(SAMPLES_NUMBER + 5 + 10* i))){
            b = (byte) ((b << 1) | 1);
        }else{
            b = (byte) (b << 1);
        }
        return b;
    }

}