package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Created by morganwalkup on 4/4/18.
 * Very end of LL3 packet. Used for error checking
 */

public class LL3PChecksum implements HeaderField {

    /** Integer representation of the checksum */
    private Integer checksum;
    public Integer getChecksum() { return checksum; }

    /**
     * Constructor accepting a string
     * @param dataString - String holding construction data
     */
    public LL3PChecksum(String dataString) {
        checksum = Integer.parseInt(dataString, Constants.HEX_BASE);
    }

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return checksum.toString();
    }

    /**
     * Returns the appropriate string to be embedded in a lab packet or frame
     * @return The string to be embedded
     */
    public String toTransmissionString() {
        return Utilities.padHexString(toHexString(), Constants.LL3P_CHECKSUM_FIELD_LENGTH);
    }

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    public String toHexString() {
        return Integer.toHexString(checksum);
    }

    /**
     * Returns ASCII string where each hex byte is converted to corresponding ASCII character
     * @return ASCII string
     */
    public String toASCIIString() {
        return checksum.toString();
    }

    /**
     * Returns formatted string displaying the content and meaning of the field
     * @return The formatted string
     */
    public String explainSelf() {
        return "Checksum: " + toString();
    }
}
