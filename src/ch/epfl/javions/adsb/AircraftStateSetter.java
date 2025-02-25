package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;

/**
 * The aircraft state setter
 *
 * @author Yassine El graoui (361984)
 * @author Alexander Raybaut (355794)
 */
public interface AircraftStateSetter {

    /**
     * This function sets the time stamp as the one that is lastly received (in nanoseconds).
     * @param timeStampNs (long) :the last time stamp that we received.
     */
    void setLastMessageTimeStampNs(long timeStampNs);

    /**
     * This function sets the category of the aircraft depending on a parameter that is given
     * @param category (int) : the given category
     */
    void setCategory(int category);

    /**
     * This function changes the call sign of the aircraft depending on a given parameter.
     * @param callSign (CallSign) : the given call sign
     */
    void setCallSign(CallSign callSign);

    /**
     * This function changes the geographic position of the aircraft depending on a given parameter.
     * @param position (GeoPos) : the given geographic position
     */
    void setPosition(GeoPos position);

    /**
     * This function changes the altitude of the aircraft depending on the altitude that is given in parameter.
     * @param altitude (double) : the altitude that is given.
     */
    void setAltitude(double altitude);

    /**
     * This function changes the velocity of the plane according to a certain given parameter
     * @param velocity : (double) : the new velocity of the aircraft.
     */
    void setVelocity(double velocity);

    /**
     * This function changes the direction of the aircraft depending on a certain parameter.
     * @param trackOrHeading (double) : the new direction of the plane.
     */
    void setTrackOrHeading(double trackOrHeading);
}
