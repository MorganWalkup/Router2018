package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Decorator class holding 4-bit LRP sequence number
 * Created by morganwalkup on 3/8/18.
 */

public class LRPSequenceNumber implements HeaderField {

    /** Unique sequence number for this routing update. Numbers wrap from 15 to zero */
    private Integer sequenceNumber;

    /**
     * Constructor for LRP Sequence Numbers
     * @param dataString - 1 Character hex string
     */
    public LRPSequenceNumber(String dataString) {
        this.sequenceNumber = Integer.parseInt(dataString, Constants.HEX_BASE);
    }

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return this.sequenceNumber.toString();
    }

    /**
     * Returns the appropriate string to be embedded in a lab packet or frame
     * @return The string to be embedded
     */
    public String toTransmissionString() {
        return this.toHexString();
    }

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    public String toHexString() {
        return Integer.toHexString(this.sequenceNumber);
    }

    /**
     * Returns ASCII string where each hex byte is converted to corresponding ASCII character
     * @return ASCII string
     */
    public String toASCIIString() {
        return Utilities.convertHexToASCII(this.toHexString());
    }

    /**
     * Returns formatted string displaying the content and meaning of the field
     * @return The formatted string
     */
    public String explainSelf() {
        return "Seq. Number: 0x" + this.toHexString();
    }

}
