package ch.epfl.javions.demodulation;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PowerWindowTest1 {

    private InputStream stream;

    {
        try {
            stream = new FileInputStream("resources/samples.bin");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] First160Values = {73,292,65,745,98,4226,12244,25722,36818,23825,10730,1657,1285,1280,394,521,1370,200,292,290,
            106,116,194,64,37,50,149,466,482,180,148,5576,13725,26210,28305,14653,4861,
            1489,85,845,3016,9657,19233,29041,25433,13842,3112,392,346,677,160,208,505,697,
            450,244,49,117,61,205,232,65,37,149,81,2,74,17,208,265,676,466,145,185,100,1586,
            9529,17901,28618,27296,16409,5189,2384,377,13,265,178,25,89,148,650,8528,19457,29105,
            31252,15172,4181,1745,85,293,680,7306,14401,24505,33010,16250,5713,1313,397,65,953,5193,20498,31880,
            41225,38537,30025,35272,32113,33329,20921,8005,1818,100,52,2626,8450,18794,28642,24392,13525,3922,1669,340,
            401,257,4,229,1021,585,2804,9325,19661,32378,30420,30298,32218,33800,33610,23666,10244,3589,740,26,137,130,
            521,890,765,841};

    private byte[] temporaryStream = {-97, -99, 53, 7, 33, 123, -56, -31, 123, 29, -85, -82, -128, 46, -115,
            98, 102, -35, -79, 14, -40, 68, 112, 50, 101, 81, -126, 21, -86, -15, 43, -109, -40, -49, 114, -82,
            -100, -27, 45, 62, 0, -89, 42, -69, -57, -99, 42, -87, 66, 45, 121, -46, 22, -54, -8, -120, -67, -94, -30,
            5, -90, -24, 52, -101, 24, 116, -101, -78, -98, 68, 119, 126, -20, 41, 52, -76, 98, 102, 1, -121};

    private int[] PowerValues = {741677498,-987601299,-1496239503,763710634,-1099747791,-296772974,1141675277,
            590315281,29826954,370285409,1314619732,1046313490,955959505,1318737626,-1773560523,-2111152086,
            2098285586,651896933,-1365295856,-1605393503};

    private byte[] temporaryStream1 = {119, -118, 80, 75, -90, -29, -101, 42, 48, 45, -74, -89, 93, 36, -83, -55,
            -63, -9, -81, 98, -30, -27, -23, -37, -109, -82, -108, -53, -5, -43, -112, 96};

    private int[] PowerValues1 = {1329728593,591361690,442123450,423310868,86895757,712704805,1878515362,43628213};

    private byte[] temporaryStream2 = {117, -44, -58, -114, -19, -105, -58, 79, -100, 46, 82, -33, -26, 114, -115,
            33, 36, -14, -75, 83, 86, -122, -93, -62, 2, -33, 105, 111, -6, -56, -20, -114, 6, 93, -109, -37, -107,
            95, -102, 27, -38, -113, 7, 13, 1, -48, 127, 107, -71, -95, -7, 78, -112, 13, 107, 60, -23, -55, 3, 29,
            -85, 8, 24, -126, 12, 50, 97, 68, -59, -73, 31, 34, -123, -17, 12, 124, 84, 59, -91, -82, -88, 6, 49, 64,
            103, -101, 9, 50, 115, 60, -51, -102, 93, -22, -16, -74};

    private int[] PowerValues2 = {1137217181,-1613690816,-72151212,113024669,285844473,-816237407,412387112,1403710661,
            -193397591,1712252309,486003505,1937988709,55270522,-1978646040,1086090853,-1941286238,613536997,524815618,
            670425509,-389261871,-660547391,-1070937947,771849472,-1932720214};

    private byte[] temporaryStream3 = {19, -48, -102, -61, 95, -125, 2, 59, 90, 73, 122, 119, -117, -64, -12, -103,
            95, 34, -37, -72, 125, -41, -51, 116, -1, -90, 96, 111, -16, -57, -7, 61, 89, -94, -70, 63, -99, -47, -70, -92};

    private int[] PowerValues3 = {511576589,-1253754672,855009800,801713293,650995362,-1284194831,202053384,1373191258,
            486512969,-1135248278};

    private byte[] temporaryStream4 = {-67, -91, -33, -111, 122, -77, -28, 120, 53, 44, -47, -32, 116, -90,
            104, -2, -80, 14, -33, -128};

    private int[] PowerValues4 = {1547292106,-784940126,522883792,1100021536,-947640991};

    private byte[] temporaryStream5 = {121, 19, -47, 26, 46, -118, 49, 14, -75, -7, -34, 89, 6, 21, 0, -72, 109, -77,
            -52, -76, 93, 7, 65, 15, 122, 64, 30, 108, -32, -102, -63, -52, -49, -119, -109, -42, -30, -121, -127, -4,
            30, -52, -64, 37, -102, -38, -49, 0, 50, 30, -108, 122, 121, -2, -18, -99, -98, 76, 56, 123};

    private int[] PowerValues5 = {31829458,1245757433,1576650244,-1506910680,356930165,1145362322,634252705,748230436,796109873,1479378770,
            -1374424998,431796714,-1606983776,72986522,514906218};

    private byte[] temporaryStream6 = {-79, 57, 95, 112, 87, 16, -126, 25, -113, -125, -54, 46, 25, 105, -94, -43, 14, 58, -12, 50, 125, -84,
            -5, -109, -50, -9, -119, -87, -46, 105, 4, -85, -77, -3, -78, -81, 30, -124, -107, -54, -65, 38, -122, 115, 91, 69, 92, -72, -117,
            49, -114, 111, -15, -108, -58, 10, -105, -93, -87, -83, 22, 12, 108, 36, 32, -88, 71, -118, 73, 70, -86, 125, -47, 6, -86, 48, -98,
            24, -119, -20, 7, -53, 118, 41, -3, 111, -53, -34};

    private int[] PowerValues6 = {875728802,606547565,-1477976318,-1963103975,1165325453,1590240370,917219108,1675285933,
            115631060,-197397851,341271237,-826615326,376097642,-2115860280,533557405,199116530,188655108,231459812,956894877,
            -252867224,1320937546,-769602791};

    private byte[] temporaryStream7 = {78, 26, 98, 22, 96, 36, 112, 102, -11, -102, -42, -2, -86, 73, -9, 81, 79, -85,
            67, -65, 115, 9, -52, 46, 30, 3, -94, -43, -79, 120, 50, 64, -13, -91, -125, -38, -46, 11, 20, 69, -87, -108,
            91, -126, 127, 25, 50, 65, -45, 20, -117, -73, -126, 88, -92, 34, -25, 36, -111, 2, 8, 56, 38, -96, 10, 36, 96,
            41, -76, 31, 6, -119, -25, 13, 10, -122};

    private int[] PowerValues7 = {35515720,426650120,1451488649,-312242862,1249271016,-1369626411,1128484228,1759888194,
            1180175194,1837903109,-1350289231,-572400071,1387234105,-1544628711,2095464149,498843460,1060584802,107774098,
            1484080637};

    @Test
    void constructionFailsOnNegativeSize(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PowerWindow(stream, -2);
        });
    }

    @Test
    void constrcutionFailsOnTooBigValues(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PowerWindow(stream, (1<<18));
        });
    }


/**
    @Test
    void returnsTheCorrectNumbers(){ //test de la méthode get()
        try (InputStream stream = new FileInputStream("resources/samples.bin");) {
            PowerWindow powerWindow = new PowerWindow(stream, 1);
            for(int i = 0; i < 160; ++i){
                assertEquals(First160Values[i], powerWindow.get(0));
                assertEquals(i, powerWindow.position());
                powerWindow.advance();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
**/

    @Test
    void TestsIfWindowIsFull(){
        try {
            System.out.println(stream.available()/4);

            PowerWindow powerWindow = new PowerWindow(stream, 8);
            assertEquals(true, powerWindow.isFull());
            powerWindow.advanceBy(1199);
            assertEquals(false, powerWindow.isFull());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void TestsAdvancesBy(){
        try {
            PowerWindow powerWindow = new PowerWindow(stream, 8);
            powerWindow.advanceBy(3);
            assertEquals(First160Values[3], powerWindow.get(0));
            assertEquals(3, powerWindow.position());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void advance() {
        try(InputStream stream = new FileInputStream("resources/samples.bin");
            InputStream stream1 = new FileInputStream("resources/samples.bin");
        ) {

            int[] batchTest = new int [1 << 16];
            int[] curBatchTest = new int [8];
            PowerComputer test = new PowerComputer(stream, 8);

            for(int i = 0; i < 100; ++i) {
                test.readBatch(curBatchTest);
                System.arraycopy(curBatchTest, 0, batchTest, 8*i, 8);
            }

            int windowSize = 8;
            PowerWindow powerWindow = new PowerWindow(stream1, windowSize);
            for(int i = 0; i < 790; ++i) {
                for(int j = 0; j < windowSize; ++j) {
                    int expected = batchTest[i + j];
                    int valueGot = powerWindow.get(j);
                    assertEquals(batchTest[i + j], powerWindow.get(j));
                }
                powerWindow.advance();
            }

        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void advanceFor24Bytes() {
        try(InputStream streamOne = new FileInputStream("resources/samples.bin");
            InputStream streamTwo = new FileInputStream("resources/samples.bin");
        ) {
            // Test of 24 bytes
            byte[] array = new byte[96];
            streamOne.readNBytes(array, 0, 96);
            InputStream streamOneNew = new ByteArrayInputStream(array);
            InputStream streamTwoNew = new ByteArrayInputStream(array);

            int[] batchTest = new int [24];
            int[] curBatchTest = new int [8];
            PowerComputer test = new PowerComputer(streamTwoNew, 8);

            for(int i = 0; i < 3; ++i) {
                test.readBatch(curBatchTest);
                System.arraycopy(curBatchTest, 0, batchTest, 8*i, 8);
            }

            int windowSize = 8;
            PowerWindow powerWindow = new PowerWindow(streamOneNew, windowSize);
            for(int i = 0; i < 16; ++i) {
                for(int j = 0; j < windowSize; ++j) {
                    int expected = batchTest[i + j];
                    int actual = powerWindow.get(j);
                    assertEquals(actual, expected);
                }
                powerWindow.advance();
            }

            for(int i = 0; i < 4; ++i) {
                powerWindow.advance();
                assertEquals(false, powerWindow.isFull());
            }


        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void advanceFor8Bytes() {
        try(InputStream streamOne = new FileInputStream("resources/samples.bin");
            InputStream streamTwo = new FileInputStream("resources/samples.bin");
        ) {
            // Test of 8 bytes
            byte[] array = new byte[32];
            streamOne.readNBytes(array, 0, 32);
            InputStream streamOneNew = new ByteArrayInputStream(array);
            InputStream streamTwoNew = new ByteArrayInputStream(array);

            int[] batchTest = new int [8];
            int[] curBatchTest = new int [8];
            PowerComputer test = new PowerComputer(streamTwoNew, 8);

            for(int i = 0; i < 1; ++i) {
                test.readBatch(curBatchTest);
                System.arraycopy(curBatchTest, 0, batchTest, 8*i, 8);
            }

            int windowSize = 8;
            PowerWindow powerWindow = new PowerWindow(streamOneNew, windowSize);
            for(int i = 0; i < 1; ++i) {
                for(int j = 0; j < windowSize; ++j) {
                    int expected = batchTest[i + j];
                    int actual = powerWindow.get(j);
                    assertEquals(actual, expected);
                }
                powerWindow.advance();
            }

            for(int i = 0; i < 4; ++i) {
                powerWindow.advance();
                assertEquals(false, powerWindow.isFull());
            }


        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void advanceThree() {
        try(InputStream streamOne = new FileInputStream("resources/samples.bin");
            InputStream streamTwo = new FileInputStream("resources/samples.bin");
        ) {
            byte[] array = new byte[64];
            streamOne.readNBytes(array, 0, 64);
            InputStream streamOneNew = new ByteArrayInputStream(array);
            InputStream streamTwoNew = new ByteArrayInputStream(array);

            int[] batchTest = new int [16];
            int[] curBatchTest = new int [8];
            PowerComputer test = new PowerComputer(streamTwoNew, 8);

            for(int i = 0; i < 2; ++i) {
                test.readBatch(curBatchTest);
                System.arraycopy(curBatchTest, 0, batchTest, 8*i, 8);
            }

            int windowSize = 3;
            PowerWindow powerWindow = new PowerWindow(streamOneNew, windowSize);
            for(int i = 0; i < 14; ++i) {
                for(int j = 0; j < windowSize; ++j) {
                    int expected = batchTest[i + j];
                    int actual = powerWindow.get(j);

                    assertEquals(expected, actual);
                }
                powerWindow.advance();
            }


            powerWindow.advanceBy(4);
            assertEquals(false, powerWindow.isFull());

        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void advanceFour() {
        try(InputStream streamOne = new FileInputStream("resources/samples.bin");
            InputStream streamTwo = new FileInputStream("resources/samples.bin");
        ) {
            byte[] array = new byte[640];
            streamOne.readNBytes(array, 0, 640);
            InputStream streamOneNew = new ByteArrayInputStream(array);
            InputStream streamTwoNew = new ByteArrayInputStream(array);

            int[] batchTest = new int [160];
            int[] curBatchTest = new int [8];
            PowerComputer test = new PowerComputer(streamTwoNew, 8);

            for(int i = 0; i < 20; ++i) {
                test.readBatch(curBatchTest);
                System.arraycopy(curBatchTest, 0, batchTest, 8*i, 8);
            }

            int windowSize = 16;
            PowerWindow powerWindow = new PowerWindow(streamOneNew, windowSize);
            for(int i = 0; i < 144; ++i) {
                for(int j = 0; j < windowSize; ++j) {
                    int expected = batchTest[i + j];
                    int actual = 0;

                    if(i == 1 && j == 15) {
                        System.out.println();
                    }

                    actual = powerWindow.get(j);

                    assertEquals(expected, actual);
                }
                powerWindow.advance();
            }


            powerWindow.advanceBy(4);
            assertEquals(false, powerWindow.isFull());

        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void advanceFive() {
        try(InputStream streamOne = new FileInputStream("resources/samples.bin");
            InputStream streamTwo = new FileInputStream("resources/samples.bin");
        ) {
            byte[] array = new byte[80];
            streamOne.readNBytes(array, 0, 80);
            InputStream streamOneNew = new ByteArrayInputStream(array);
            InputStream streamTwoNew = new ByteArrayInputStream(array);

            int[] batchTest = new int [20];
            int[] curBatchTest = new int [8];
            PowerComputer test = new PowerComputer(streamTwoNew, 8);

            for(int i = 0; i < 2; ++i) {
                test.readBatch(curBatchTest);
                System.arraycopy(curBatchTest, 0, batchTest, 8*i, 8);
            }

            test.readBatch(curBatchTest);
            System.arraycopy(curBatchTest, 0, batchTest, 16, 4);

            int windowSize = 16;
            PowerWindow powerWindow = new PowerWindow(streamOneNew, windowSize);
            for(int i = 0; i < 4; ++i) {
                for(int j = 0; j < windowSize; ++j) {
                    int expected = batchTest[i + j];
                    int actual = powerWindow.get(j);

                    assertEquals(expected, actual);
                }
                powerWindow.advance();
            }


            powerWindow.advanceBy(4);
            assertEquals(false, powerWindow.isFull());

        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void advanceSix() {
        try(InputStream streamOne = new FileInputStream("resources/samples.bin");
            InputStream streamTwo = new FileInputStream("resources/samples.bin");
        ) {
            byte[] array = {-97, -99, 53, 7, 33, 123, -56, -31, 123, 29,
                    -85, -82, -128, 46, -115, 98, 102, -35, -79, 14,
                    -40, 68, 112, 50, 101, 81, -126, 21, -86, -15, 43,
                    -109, -40, -49, 114, -82, -100, -27, 45, 62, 0, -89,
                    42, -69, -57, -99, 42, -87, 66, 45, 121, -46, 22, -54,
                    -8, -120, -67, -94, -30, 5, -90, -24, 52, -101, 24, 116,
                    -101, -78, -98, 68, 119, 126, -20, 41, 52, -76, 98, 102, 1, -121};


            InputStream streamOneNew = new ByteArrayInputStream(array);
            InputStream streamTwoNew = new ByteArrayInputStream(array);

            int[] batchTest = new int [20];
            int[] curBatchTest = new int [8];
            PowerComputer test = new PowerComputer(streamTwoNew, 8);

            for(int i = 0; i < 2; ++i) {
                test.readBatch(curBatchTest);
                System.arraycopy(curBatchTest, 0, batchTest, 8*i, 8);
            }

            test.readBatch(curBatchTest);
            System.arraycopy(curBatchTest, 0, batchTest, 16, 4);

            int windowSize = 8;
            PowerWindow powerWindow = new PowerWindow(streamOneNew, windowSize);
            for(int i = 0; i < 12; ++i) {
                for(int j = 0; j < windowSize; ++j) {
                    int expected = batchTest[i + j];
                    int actual = powerWindow.get(j);

                    assertEquals(expected, actual);
                }
                powerWindow.advance();
            }

            System.out.println(Arrays.toString(batchTest));
            System.out.println(powerWindow.get(7));
            powerWindow.advance();
            System.out.println(powerWindow.get(6));
            powerWindow.advance();

            System.out.println(powerWindow.isFull());

            System.out.println(powerWindow.get(5));
            powerWindow.advance();
            System.out.println(powerWindow.get(4));
            powerWindow.advance();
            System.out.println(powerWindow.get(3));
            powerWindow.advance();
            System.out.println(powerWindow.get(2));
            powerWindow.advance();
            System.out.println(powerWindow.get(1));
            powerWindow.advance();
            powerWindow.advance();
            powerWindow.advance();

        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    void advancesCorrectlyWithArraysThatAreNotMultiplesOfInitialSizeWhereSizeIs5(){ //refait
        InputStream ohio = new ByteArrayInputStream(temporaryStream6);
        try{
            PowerWindow window = new PowerWindow(ohio, 5);
            for(int i = 0; i<17; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues6[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 5;
            for(int i = 17; i<22; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues6[i+j], window.get(j));
                }
                if(i>17) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Test
    void advancesCorrectlyWithArraysThatAreNotMultiplesOfInitialSizeBisWhereSizeIs5(){
        InputStream ohio = new ByteArrayInputStream(temporaryStream7);
        try{
            PowerWindow window = new PowerWindow(ohio, 5);
            for(int i = 0; i<14; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues7[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 5;
            for(int i = 14; i<19; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues7[i+j], window.get(j));
                }
                if(i>14) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Test
    void advancesCorrectlyWithArraysThatAreMultiplesOfInitialSize1WhereSizeIs5(){ // fait avec size = 5 et stream4
        InputStream ohio = new ByteArrayInputStream(temporaryStream4);
        try{
            PowerWindow window = new PowerWindow(ohio, 5);
            for(int i = 0; i<1; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues4[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 4;
            for(int i = 1; i<5; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues4[i+j], window.get(j));
                }
                if(i>=1) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Test
    void advancesCorrectlyWithArraysThatAreMultiplesOfInitialSize3WhereSizeIs5(){ // refait avec size = 5
        InputStream ohio = new ByteArrayInputStream(temporaryStream5);
        try{
            PowerWindow window = new PowerWindow(ohio, 5);
            for(int i = 0; i<11; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues5[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 4;
            for(int i = 11; i<15; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues5[i+j], window.get(j));
                }
                if(i>11) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }


    //Size = 8

    @Test
    void advancesCorrectlyWithArraysThatAreNotMultiplesOfInitialSize(){
        InputStream ohio = new ByteArrayInputStream(temporaryStream);
        try{
            PowerWindow window = new PowerWindow(ohio, 8);
            for(int i = 0; i<12; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 8;
            for(int i = 12; i<20; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues[i+j], window.get(j));
                }
                if(i>12) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Test
    void advancesCorrectlyWithArraysThatAreNotMultiplesOfInitialSizeBis(){
        InputStream ohio = new ByteArrayInputStream(temporaryStream3);
        try{
            PowerWindow window = new PowerWindow(ohio, 8);
            for(int i = 0; i<2; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues3[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 8;
            for(int i = 2; i<10; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues3[i+j], window.get(j));
                }
                if(i>2) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Test
    void advancesCorrectlyWithArraysThatAreMultiplesOfInitialSize1(){
        InputStream ohio = new ByteArrayInputStream(temporaryStream1);
        try{
            PowerWindow window = new PowerWindow(ohio, 8);
            for(int i = 0; i<1; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues1[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 7;
            for(int i = 1; i<8; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues1[i+j], window.get(j));
                }
                if(i>=1) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    @Test
    void advancesCorrectlyWithArraysThatAreMultiplesOfInitialSize4(){
        InputStream ohio = new ByteArrayInputStream(temporaryStream2);
        try{
            PowerWindow window = new PowerWindow(ohio, 8);
            for(int i = 0; i<16; ++i){
                for(int j = 0; j< window.size(); ++j){
                    assertEquals(PowerValues2[i+j], window.get(j));
                }
                assertEquals(true, window.isFull());
                window.advance();
            }

            int artificialWindowSize = 8;
            for(int i = 16; i<24; ++i){
                for(int j = 0; j< artificialWindowSize; ++j){
                    assertEquals(PowerValues2[i+j], window.get(j));
                }
                if(i>16) {
                    assertEquals(false, window.isFull());
                }
                --artificialWindowSize;

                window.advance();
            }

        }catch(Exception e){
            throw new RuntimeException();
        }
    }
}