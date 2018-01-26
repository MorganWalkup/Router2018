package com.morganwalkup.networks.datagram;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * A simple datagram containing only text
 * Created by morganwalkup on 1/26/18.
 */
public class TextDatagram implements Datagram {

    /** The text payload of this datagram */
    private String text;

    /**
     * Constructor taking a string argument
     * @param text - The text contained in this datagram
     */
    public TextDatagram(String text) {
        this.text = text;
    }

    /**
     * Returns string suitable for transmission, appropriately displaying contents
     * @return transmission string
     */
    public String toString() {
        return this.text;
    }

    /**
     * Returns string of the contents in the hex format
     * If the field is ASCII data, then the hex characters representing ASCII data are returned instead
     * @return the hex string
     */
    public String toHexString() {
        return Utilities.convertASCIIToHEX(this.text);
    }

    /**
     * Returns full explanation of the datagram and all its fields
     * If this datagram contains other datagrams, also include their protocol explanation strings
     * @return the protocol explanation string
     */
    public String toProtocolExplanationString() {
        return ("Text payload: " + this.toString());
    }

    /**
     * Returns one-line string that displays the top level protocol and info about that protocol
     * @return the protocol summary string
     */
    public String toSummaryString() {
        return (this.text.substring(0, Constants.SUMMARY_LENGTH) + "...");
    }

    /**
     * Returns string suitable for transmission in the LAB format
     * All numbers are transmitted as hex characters in the appropriate field width
     * and all letters are transmitted as ASCII
     * @return transmission string
     */
    public String toTransmissionString() {
        return this.toString();
    }

}
