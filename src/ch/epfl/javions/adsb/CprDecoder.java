package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;

/**
 * The CPR decoder
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */

public class CprDecoder {

    /**
     * The value of Z_PHI_0
     */
    private static final double Z_PHI_0 = 60;

    /**
     * The value of Z_PHI_1
     */
    private static final double Z_PHI_1 = 59;

    /**
     * The value of DELTA_PHI_0 which is given by the formula : 1/Z_PHI_0
     */
    private static final double DELTA_PHI_0 = 1/(Z_PHI_0);

    /**
     * The value of DELTA_PHI_1 which is given by the formula : 1/Z_PHI_1
     */
    private static final double DELTA_PHI_1 = 1/(Z_PHI_1);

    private CprDecoder() {}

    /**
     * This method decodes the position given the coordinates of two messages with opposite parity.
     * @param x0 (double) : the longitude of the even message
     * @param y0 (double) : the latitude of the even message
     * @param x1 (double) : the longitude od the odd message
     * @param y1 (double) : the latitude od the odd message
     * @param mostRecent (int) : the parity of the most recent message received
     * @return (GeoPos) : this function returns the decoded geographic position of the iarcraft using two recent message with
     * opposite parity
     */
    public static GeoPos decodePosition(double x0, double y0, double x1, double y1, int mostRecent) {
        Preconditions.checkArgument(mostRecent == 0 || mostRecent == 1);

        double z_phi = Math.rint(y0*Z_PHI_1 - y1*Z_PHI_0);

        double z_phi_0 = z_phi_i(z_phi, 0);
        double z_phi_1 = z_phi_i(z_phi, 1);

        double phi_0 = DELTA_PHI_0*(z_phi_0 + y0);
        double phi_1 = DELTA_PHI_1*(z_phi_1 + y1);

        double a0 = calculateA(phi_0);
        double a1 = calculateA(phi_1);

        double Z_LAMBDA_0_PHI_0;
        double Z_LAMBDA_0_PHI_1;

        if(Double.isNaN(a0)) {
            Z_LAMBDA_0_PHI_0 = 1;
        } else {
            Z_LAMBDA_0_PHI_0 = Math.floor((2*Math.PI)/a0);
        }

        if(Double.isNaN(a1)) {
            Z_LAMBDA_0_PHI_1 = 1;
        } else {
            Z_LAMBDA_0_PHI_1 = Math.floor((2*Math.PI)/a1);
        }

        if(Z_LAMBDA_0_PHI_0 != Z_LAMBDA_0_PHI_1) {
            return null;
        }

        double Z_LAMBDA_0 = Z_LAMBDA_0_PHI_0;
        double Z_LAMBDA_1 = Z_LAMBDA_0 - 1;

        double DELTA_LAMBDA_0 = 1/Z_LAMBDA_0;
        double DELTA_LAMBDA_1 = 1/Z_LAMBDA_1;

        double lambda_0 = 0;
        double lambda_1 = 0;

        if(Z_LAMBDA_0 == 1) {
            lambda_0 = x0;
            lambda_1 = x1;
        } else if(Z_LAMBDA_0 > 1) {
            double z_lambda = Math.rint(x0*Z_LAMBDA_1 - x1*Z_LAMBDA_0);

            double z_lambda_0 = z_lambda_i(z_lambda, Z_LAMBDA_0);
            double z_lambda_1 = z_lambda_i(z_lambda, Z_LAMBDA_1);

            lambda_0 = DELTA_LAMBDA_0*(z_lambda_0 + x0);
            lambda_1 = DELTA_LAMBDA_1*(z_lambda_1 + x1);
        }

        double latitude;
        double longitude;

        if(mostRecent == 0) {
            latitude = phi_0;
            longitude = lambda_0;
        } else {
            latitude = phi_1;
            longitude = lambda_1;
        }

        if(latitude >= 0.5) latitude -= 1;
        if(longitude >= 0.5) longitude -= 1;

        latitude = Math.rint(Units.convert(latitude, Units.Angle.TURN, Units.Angle.T32));
        longitude = Math.rint(Units.convert(longitude, Units.Angle.TURN, Units.Angle.T32));

        if(!(GeoPos.isValidLatitudeT32((int)latitude))) {
            return null;
        }

        return new GeoPos((int)(longitude), (int)(latitude));
    }

    /**
     * This function returns the correct value of z_phi_i depending on either we want z_phi0 or z_phi1.
     * @param z_phi (double) : the initial value of z_phi using this formula : Math.rint(y0*Z_PHI_1 - y1*Z_PHI_0)
     * @param i (int) : tells us if we want z_phi0 or z_phi1
     * @return (double) : the correct value of z_phi_i
     */
    private static double z_phi_i(double z_phi, int i) {
        double z_phi_i = i == 1 ? Z_PHI_1 : Z_PHI_0;
        return (z_phi < 0) ? z_phi + z_phi_i : z_phi;
    }

    /**
     * This function returns the correct value of z_phi_i depending on either we want z_lambda0 or z_lambda1.
     * @param z_lambda (double) : the initial value of z_lambda using the following formula : Math.rint(x0*Z_LAMBDA_1 - x1*Z_LAMBDA_0)
     * @param z_lambda_i (double) : depends on which z_lambda_i we want to calculate. If we want z_lambda0 it is Z_LAMBDA_0. Otherwise,
     *                   it is Z_LAMBDA_1
     * @return (double) : the correct value of z_phi_i
     */
    private static double z_lambda_i(double z_lambda, double z_lambda_i) {
        return (z_lambda < 0) ? z_lambda + z_lambda_i : z_lambda;
    }

    /**
     * This function returns the correct value of A using the formula. If the argument given in parameter of the acos() is invalid,
     * it returns NaN. Also, before doing the calculation it converts the parameter given from turns to radians.
     * @param phi (double) : the value of the latitude in turns.
     * @return (double) : the value of a or Nan if the argument of the acos() is invalid.
     */
    private static double calculateA(double phi) {
        phi = Units.convertFrom(phi, Units.Angle.TURN);
        return Math.acos(1 - (1 - Math.cos(2*Math.PI*DELTA_PHI_0))/(Math.cos(phi)*Math.cos(phi)));
    }
}
