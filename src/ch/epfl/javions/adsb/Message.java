package ch.epfl.javions.adsb;

import ch.epfl.javions.aircraft.IcaoAddress;

/**
 * The Message
 *
 * @author Yassine El graoui (361984)
 * @author Alexandre Raybaut (355794)
 */
public interface Message {

    /**
     * This function returns the time (in nanosecond) when we receive the message.
     * @return (long) : this function returns the time (in nanosecond) when we receive the message.
     */
    long timeStampNs();

    /**
     * This function returns the IcaoAddress of the message
     * @return (IcaoAddress) : this function returns the IcaoAddress of the message.
     */
    IcaoAddress icaoAddress();
}
