package ch.epfl.javions.demodulation;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PowerComputerTest1 {

    @Test
    void readBatch() {
        try(InputStream streamOne = new FileInputStream("resources/samples.bin");
            InputStream streamTwo = new FileInputStream("resources/samples.bin")
        ) {

            PowerComputer computer = new PowerComputer(streamOne, 512);
            int[] batch = new int[512];
            computer.readBatch(batch);
             System.out.println(Arrays.toString(batch));

            PowerComputer computer1 = new PowerComputer(streamTwo, 32);
            int[] batch1 = new int[32];
            int[] test = new int[512];

            for(int i = 0; i < 16; ++i) {
                computer1.readBatch(batch1);
                System.arraycopy(batch1, 0, test, 32*i, 32);
            }

            System.out.println(Arrays.toString(test));
        } catch(IOException e) {
            throw new RuntimeException();
        }
    }


    @Test
    void IllegalArgumentExceptionTestConstructor() {
        assertThrows(IllegalArgumentException.class, ()-> {
            try(InputStream stream = new FileInputStream("resources/samples.bin");) {
                PowerComputer computer = new PowerComputer(stream, 9);
            } catch(IOException e) {
                throw new RuntimeException();
            }
        });

        assertThrows(IllegalArgumentException.class, ()-> {
            try(InputStream stream = new FileInputStream("resources/samples.bin");) {
                PowerComputer computer = new PowerComputer(stream, -8);
            } catch(IOException e) {
                throw new RuntimeException();
            }
        });
    }

    @Test
    void IllegalArgumentExceptionReadBatch() {
        assertThrows(IllegalArgumentException.class, ()-> {
            try(InputStream stream = new FileInputStream("resources/samples.bin");) {
                PowerComputer computer = new PowerComputer(stream, 16);
                int[] batch = new int[2];
                computer.readBatch(batch);
            } catch(IOException e) {
                throw new RuntimeException();
            }
        });
    }
}