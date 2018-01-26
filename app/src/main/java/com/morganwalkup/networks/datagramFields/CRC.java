package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.headerFields.HeaderField;
import com.morganwalkup.support.Utilities;

/**
 * Class representing the CRC field inside LL2P packets and frames
 * Created by morganwalkup on 1/24/18.
 */

public class CRC implements HeaderField {

    /** Fake string value of the address */
    private String crcValue;

    /**
     * Constructor taking a string of the type value
     */
    public CRC(String typeValueString) {
        this.crcValue = typeValueString.substring(0, 4);
    }

    /***
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return crcValue.toString();
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
        return this.crcValue;
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
     * @return The explanation string
     */
    public String explainSelf() {
        return this.crcValue;
    }
}
