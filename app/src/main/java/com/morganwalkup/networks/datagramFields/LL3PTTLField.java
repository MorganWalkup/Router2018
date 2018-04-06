package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Created by morganwalkup on 4/4/18.
 * 1 byte field with max value of 15
 */
public class LL3PTTLField implements HeaderField {

    /** Time to live - number of hops before this packet is discarded */
    private Integer ttl;
    public Integer getTTL() { return this.ttl; }

    /**
     * Constructor accepting string
     * @param inputData - String holding construction data
     */
    public LL3PTTLField(String inputData) {
        ttl = Integer.parseInt(inputData, Constants.HEX_BASE);
    }


    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return toTransmissionString();
    }

    /**
     * Returns the appropriate string to be embedded in a lab packet or frame
     * @return The string to be embedded
     */
    public String toTransmissionString() {
        return toHexString();
    }

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    public String toHexString() {
        return Utilities.padHexString(Integer.toHexString(ttl), Constants.LL3P_TTL_FIELD_LENGTH);
    }

    /**
     * Returns ASCII string where each hex byte is converted to corresponding ASCII character
     * @return ASCII string
     */
    public String toASCIIString() {
        return ttl.toString();
    }

    /**
     * Returns formatted string displaying the content and meaning of the field
     * @return The formatted string
     */
    public String explainSelf() {
        return "TTL: " + this.ttl.toString();
    }

    /**
     * Decrement the TTL field by 1
     */
    public void decrementTTL() {
        ttl = ttl - 1;
    }

}
