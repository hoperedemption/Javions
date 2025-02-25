package ch.epfl.javions.demodulation;

import ch.epfl.javions.adsb.*;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrintRawMessages {

    /**
    public static void main(String[] args) throws IOException {
        String f = "resources/samples_20230304_1442.bin";
        try (InputStream s = new FileInputStream(f)) {
            AdsbDemodulator d = new AdsbDemodulator(s);
            RawMessage m;
            long start = System.currentTimeMillis();

            while ((m = d.nextMessage()) != null) {
                System.out.println(m);
            }

            long end = System.currentTimeMillis();
            System.out.println((double)(end - start)/1000);

        }
    }
     **/

    public static void main(String[] args) throws IOException {
        String f = "resources/samples_20230304_1442.bin";
        IcaoAddress expectedAddress = new IcaoAddress("4D2228");
        try (InputStream s = new FileInputStream(f)) {
            AdsbDemodulator d = new AdsbDemodulator(s);
            RawMessage m;
            AircraftStateAccumulator<AircraftState> a =
                    new AircraftStateAccumulator<>(new AircraftState());
            while ((m = d.nextMessage()) != null) {
                if (!m.icaoAddress().equals(expectedAddress)) continue;
                else {
                    Message pm = MessageParser.parse(m);
                    if (pm != null) a.update(pm);
                }
            }
        }
    }
}
