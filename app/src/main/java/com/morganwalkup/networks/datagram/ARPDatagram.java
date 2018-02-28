package com.morganwalkup.networks.datagram;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagramFields.LL3PAddressField;
import com.morganwalkup.support.HeaderFieldFactory;

/**
 *
 * Created by morganwalkup on 2/22/18.
 */

public class ARPDatagram implements Datagram {

    /** DatagramHeaderField containing ll3p address */
    private LL3PAddressField ll3pAddress;

    /**
     * Constructor accepting string
     * @param ll3pAddress - String of the ll3pAddress
     */
    public ARPDatagram(String ll3pAddress) {
        this.ll3pAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_SOURCE_ADDRESS, ll3pAddress);
    }

    /**
     * Returns string suitable for transmission, appropriately displaying contents
     * @return transmission string
     */
    public String toString() {
        return this.ll3pAddress.toString();
    }

    /**
     * Returns string of the contents in the hex format
     * If the field is ASCII data, then the hex characters representing ASCII data are returned instead
     * @return the hex string
     */
    public String toHexString() {
        return this.ll3pAddress.toHexString();
    }

    /**
     * Returns full explanation of the datagram and all its fields
     * If this datagram contains other datagrams, also include their protocol explanation strings
     * @return the protocol explanation string
     */
    public String toProtocolExplanationString() {
        return this.ll3pAddress.explainSelf();
    }

    /**
     * Returns one-line string that displays the top level protocol and info about that protocol
     * @return the protocol summary string
     */
    public String toSummaryString() {
        return "ARP Address: " + this.toProtocolExplanationString();
    }

    /**
     * Returns string suitable for transmission in the LAB format
     * All numbers are transmitted as hex characters in the appropriate field width
     * and all letters are transmitted as ASCII
     * @return transmission string
     */
    public String toTransmissionString() {
        return this.ll3pAddress.toTransmissionString();
    }


}
