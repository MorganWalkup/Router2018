package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Created by morganwalkup on 4/4/18.
 * 2 byte field with value cycling from 0 to 65,535
 */

public class LL3PIdentifierField implements HeaderField {

    /** Integer value from 0 to 65,535 */
    private Integer identifier;
    public Integer getIdentifier() { return identifier; }

    /**
     * Constructor accepting a string
     * @param dataString - String holding construction data
     */
    public LL3PIdentifierField(String dataString) {
        identifier = Integer.parseInt(dataString, Constants.HEX_BASE);
    }

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return identifier.toString();
    }

    /**
     * Returns the appropriate string to be embedded in a lab packet or frame
     * @return The string to be embedded
     */
    public String toTransmissionString() {
        return Utilities.padHexString(toHexString(), Constants.LL3P_IDENTIFIER_FIELD_LENGTH);
    }

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    public String toHexString() {
        return Integer.toHexString(identifier);
    }

    /**
     * Returns ASCII string where each hex byte is converted to corresponding ASCII character
     * @return ASCII string
     */
    public String toASCIIString() {
        return identifier.toString();
    }

    /**
     * Returns formatted string displaying the content and meaning of the field
     * @return The formatted string
     */
    public String explainSelf() {
        return "Identifier: " + toString();
    }
}
