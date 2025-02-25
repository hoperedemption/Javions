package ch.epfl.javions.demodulation;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class SamplesDecoderTest1 {
    private static short[] batchTest1;

    private static void getBatchTest() {
        try(InputStream stream = new FileInputStream("resources/samples.bin");) {
        int testBatchSize = 2402;
        batchTest1 = new short[testBatchSize];

        byte[] bytes = new byte [2*testBatchSize];
        int readBytes = stream.readNBytes(bytes, 0, 2*testBatchSize);

        for(int i = 0; i < 2402; ++i) {
            byte lsb = (byte)bytes[2*i];
            String s1 = String.format("%8s", Integer.toBinaryString(bytes[2*i] & 0xFF)).replace(' ', '0');
            byte msb = (byte)bytes[2*i + 1];
            String s2 = String.format("%8s", Integer.toBinaryString(bytes[2*i + 1] & 0xFF)).replace(' ', '0');

            String res = s2 + s1;
            batchTest1[i] = (short) ((Integer.parseInt(res, 2)) - 2048);
        }
        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void readBatch() {
        getBatchTest();

        try(InputStream stream = new FileInputStream("resources/samples.bin");) {
            SamplesDecoder sample = new SamplesDecoder(stream, 2402);
            short batch[] = new short[2402];
            sample.readBatch(batch);

            for(int i = 0; i < 2402; ++i) {
                assertEquals(batchTest1[i], batch[i]);
            }
        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void throwsIllegalArgumentExceptionIfNotPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            try (InputStream stream = new FileInputStream("resources/samples.bin");) {
                SamplesDecoder samplesDecoderOne = new SamplesDecoder(stream, -1);
                SamplesDecoder samplesDecoderTwo = new SamplesDecoder(stream, 0);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        );
    }

    @Test
    void throwsNullPointerExceptionIfStreamIsNull() {
        assertThrows(NullPointerException.class, () -> {
            try (InputStream stream = new FileInputStream("resources/samples.bin");) {
                SamplesDecoder samplesDecoder = new SamplesDecoder(null, 2);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        });
    }

    @Test
    void throwsIllegalArgumentExceptionIfNotEqualsToLotSize() {
        assertThrows(IllegalArgumentException.class, ()-> {
            try (InputStream stream = new FileInputStream("resources/samples.bin");) {
                SamplesDecoder samplesDecoder = new SamplesDecoder(stream, 4);
                short[] test = new short[3];
                samplesDecoder.readBatch(test);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        });
    }


}