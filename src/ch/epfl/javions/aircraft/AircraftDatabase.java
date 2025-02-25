package ch.epfl.javions.aircraft;

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipFile;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * AircraftDataBase
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public final class AircraftDatabase {
    private final String mictronicsDataBase;

    /**
     * Constructor of AircraftDatabase returns an object representing the mictronics database,
     * stored in the given file name, or throws a NullPointerException if it is null.
     *
     * @param fileName (String) : string soring the name of the input file
     * @throws NullPointerException : if the argument is null
     */
    public AircraftDatabase(String fileName){
        mictronicsDataBase = Objects.requireNonNull(fileName);
    }

    /**
     * This function returns the data of the airplane given an ICAO adress. If the data does not exist un the database it
     * returns null.
     *
     * @param address the given ICAO address
     * @return the data of the aircraft whose ICAO address is the given one,
      or null if no entry exists in the database for this address;
     * @throws IOException in case of an input/output error.
     */
    public AircraftData get(IcaoAddress address) throws IOException{
        String adressIcao = address.string();

        String fileName = adressIcao.substring(4,6);
        String ans = "";
        String[] result;

        try (ZipFile zipFile = new ZipFile(mictronicsDataBase);
             InputStream stream = zipFile.getInputStream(zipFile.getEntry(fileName + ".csv"));
             Reader reader = new InputStreamReader(stream, UTF_8);
             BufferedReader buffer = new BufferedReader(reader)
        )
        {
            ans = getString(adressIcao, ans, buffer);
            if (ans == null) return null;
        }
        if(adressIcao.compareTo(ans.substring(0, 6)) != 0) {
            return null;
        }

        result = ans.split(",", -1);

        return new AircraftData(new AircraftRegistration(result[1]),
                new AircraftTypeDesignator(result[2]), result[3], new AircraftDescription(result[4]),
                 WakeTurbulenceCategory.of(result[5]));

    }

    //Tu peux commenter cette méthode je suis pas sur de ce qu'elle fait
    private static String getString(String adressIcao, String ans, BufferedReader buffer) throws IOException {
        String l = "";
        while ((l = buffer.readLine()) != null) {
            String message = l.substring(0, 6);
            if(message.compareTo(adressIcao) >= 0) {
                ans = l;
                break;
            }
        }
        if(l == null) {
            return null;
        }
        return ans;
    }
}