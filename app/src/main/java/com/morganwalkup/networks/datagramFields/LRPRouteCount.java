package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Decorator class holding the number of routes in the corresponding LRP update
 * Created by morganwalkup on 3/8/18.
 */

public class LRPRouteCount implements HeaderField {

    /** 4-bit number of routes. Maximum number of routes is 15 in any update */
    private Integer routeCount;
    public Integer getRouteCount() { return this.routeCount; }

    /**
     * Constructor for LRPRouteCount
     * @param dataString - 1 character hex string
     */
    public LRPRouteCount(String dataString) {
        this.routeCount = Integer.parseInt(dataString, Constants.HEX_BASE);
    }

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return this.routeCount.toString();
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
        return Integer.toHexString(this.routeCount);
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
        return "Route Count: " + this.toString();
    }
}
