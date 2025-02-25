package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;

/**
 * The state of the aircraft
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public class AircraftState implements AircraftStateSetter {

    /**
     * This function displays the time stamp of the aircraft.
     * @param timeStampNs (long) :the last time stamp that we received.
     */
    @Override
    public void setLastMessageTimeStampNs(long timeStampNs) {
       //System.out.println("timeStampNs : " + timeStampNs);
    }

    /**
     * this function displays the category of the airplane
     * @param category (int) : the given category
     */
    @Override
    public void setCategory(int category) {
        //System.out.println("category : " + category);
    }

    /**
     * This function displays the call sign of the plane
     * @param callSign (CallSign) : the given call sign
     */
    @Override
    public void setCallSign(CallSign callSign) {
        System.out.println("indicatif : " + callSign);
        //System.out.println("callSign : " + callSign);
    }

    /**
     * This function displays the geographic position of the plane
     * @param position (GeoPos) : the given geographic position
     */
    @Override
    public void setPosition(GeoPos position) {
        System.out.println("position : " + position);
        //System.out.println("position : " + position);
    }

    /**
     * This function displays the altitude of the plane
     * @param altitude (double) : the altitude that is given.
     */
    @Override
    public void setAltitude(double altitude) {
        //System.out.println("Altitude : " + altitude);
    }

    /**
     * This function displays the velocity of the plane
     * @param velocity : (double) : the new velocity of the aircraft.
     */
    @Override
    public void setVelocity(double velocity) {
        //System.out.println("velocity : " + velocity);
    }

    /**
     * This function displays the direction of the plane
     * @param trackOrHeading (double) : the new direction of the plane.
     */
    @Override
    public void setTrackOrHeading(double trackOrHeading) {
        //System.out.println("trackOrHeading : " + trackOrHeading);
    }
}
