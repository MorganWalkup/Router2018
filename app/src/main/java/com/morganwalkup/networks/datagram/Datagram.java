package com.morganwalkup.networks.datagram;

/**
 * Interface providing consistent translation of all datagrams into various text formats
 * Created by morganwalkup on 1/24/18.
 */

public interface Datagram {

    /**
     * Returns string suitable for transmission, appropriately displaying contents
     * @return transmission string
     */
    String toString();

    /**
     * Returns string of the contents in the hex format
     * If the field is ASCII data, then the hex characters representing ASCII data are returned instead
     * @return the hex string
     */
    String toHexString();

    /**
     * Returns full explanation of the datagram and all its fields
     * If this datagram contains other datagrams, also include their protocol explanation strings
     * @return the protocol explanation string
     */
    String toProtocolExplanationString();

    /**
     * Returns one-line string that displays the top level protocol and info about that protocol
     * @return the protocol summary string
     */
    String toSummaryString();

    /**
     * Returns string suitable for transmission in the LAB format
     * All numbers are transmitted as hex characters in the appropriate field width
     * and all letters are transmitted as ASCII
     * @return transmission string
     */
    String toTransmissionString();

}
