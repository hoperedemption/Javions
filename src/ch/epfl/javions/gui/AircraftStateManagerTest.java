package ch.epfl.javions.gui;


import ch.epfl.javions.ByteString;
import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.*;
import ch.epfl.javions.aircraft.AircraftDatabase;

import java.io.*;
import java.util.*;

public class AircraftStateManagerTest {
    public final static Map<Integer, String> MAP_PI4_TO_ARROWS= new HashMap<>(Map.of(0, " ↑ ",
            1, " ↗ ",
            2, " → ",
            3, " ↘ ",
            4, " ↓ ",
            5, " ↙ ",
            6, " ← ",
            7, " ↖ "));
    public final static AddressComparator comparator = new AddressComparator();

    public static void main(String[] args) throws IOException, InterruptedException {
        String mictronicsDatabase = "resources/aircraft.zip";
        AircraftDatabase database = new AircraftDatabase(mictronicsDatabase);
        AircraftStateManager manager = new AircraftStateManager(database);
        Set<ObservableAircraftState> states;

        try (DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream("resources/messages_20230318_0915.bin")))){
            byte[] bytes = new byte[RawMessage.LENGTH];

            long startTime = System.nanoTime();

            while (true) {
                long currentTime = System.nanoTime();

                String CSI = "\u001B[";
                String CLEAR_SCREEN = CSI + "2J";
                System.out.print(CLEAR_SCREEN);

                System.out.format("+------+--------------+-------------+--------------------+--------------------+--------------------+----------+----------+---+%n");
                System.out.format("| OACI |  Indicatif   |   Immat.    |       Modèle       |      Longitude     |      Latitude      |   Alt.   |   Vit.   |   |%n");
                System.out.format("+------+--------------+-------------+--------------------+--------------------+--------------------+----------+----------+---+%n");

                long timeStampNs = s.readLong();
                int bytesRead = s.readNBytes(bytes, 0, bytes.length);
                assert bytesRead == RawMessage.LENGTH;

                ByteString message = new ByteString(bytes);
                RawMessage rawMessage = RawMessage.of(timeStampNs, bytes);
                Message mainMessage;

                if(!Objects.isNull(rawMessage) && !Objects.isNull(mainMessage = MessageParser.parse(rawMessage))) {
                    manager.updateWithMessage(mainMessage);
                }
                // && System.nanoTime()-startTime>timeStampNs
                //timeStamp :: 674426700

                states = manager.states();
                List<ObservableAircraftState> statesList = new ArrayList<>(states);
                statesList.sort(comparator);

                for(ObservableAircraftState state : statesList) {
                    GeoPos position = state.getPosition();

                    double angle = state.getTrackOrHeading();
                    int key = ((int) Math.rint(angle/(Math.PI/4))) % 8;

                    if(currentTime - startTime < timeStampNs) {
                        long timeToWait = (timeStampNs - (currentTime - startTime))/1000000;
                        Thread.sleep(timeToWait);
                    }
                    printLine(state.getIcaoAddress().string(), Objects.isNull(state.getCallSign()) ? " " : state.getCallSign().string(),
                            state.getAircraftData().registration().string(), state.getAircraftData().model(),
                            (float) Units.convertTo(position.longitude(), Units.Angle.DEGREE), (float) Units.convertTo(position.latitude(), Units.Angle.DEGREE), (float) state.getAltitude(),
                            (float) Units.convertTo(state.getVelocity(), Units.Speed.KILOMETER_PER_HOUR), MAP_PI4_TO_ARROWS.get(key));
                    System.out.println();
                }
                manager.purge();
            }
        } catch (EOFException e) {

        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void printLine(String icao, String callSign, String immat, String model,
                                 float longitude, float latitude, float altitude, float speed,
                                 String orientation){
        String alignFormat = "|%-6s|%-14s|%-13s|%-20.20s|%-20.5f|%-20.5f|%-10.0f|%-10.0f|%-3s| ";
        System.out.format(alignFormat, icao, callSign, immat,  model, longitude, latitude, altitude, speed, orientation);
    }

    private static class AddressComparator
            implements Comparator<ObservableAircraftState> {
        @Override
        public int compare(ObservableAircraftState o1,
                           ObservableAircraftState o2) {
            String s1 = o1.getIcaoAddress().string();
            String s2 = o2.getIcaoAddress().string();
            return s1.compareTo(s2);
        }
    }
}
