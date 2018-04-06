package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Created by morganwalkup on 4/4/18.
 * Type of the LL3PDatagram. Only valid value is 0x8001 - text payload
 */
public class LL3PTypeField implements HeaderField {

    /** Integer representation of the type */
    private Integer type;
    public Integer getType() { return type; }

    /**
     * Constructor accepting string
     * @param dataString - Construction data
     */
    public LL3PTypeField(String dataString) {
        type = Integer.parseInt(dataString, Constants.HEX_BASE);
    }

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return type.toString();
    }

    /**
     * Returns the appropriate string to be embedded in a lab packet or frame
     * @return The string to be embedded
     */
    public String toTransmissionString() {
        return Utilities.padHexString(toHexString(), Constants.LL3P_TYPE_FIELD_LENGTH);
    }

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    public String toHexString() {
        return Integer.toHexString(type);
    }

    /**
     * Returns ASCII string where each hex byte is converted to corresponding ASCII character
     * @return ASCII string
     */
    public String toASCIIString() {
        return type.toString();
    }

    /**
     * Returns formatted string displaying the content and meaning of the field
     * @return The formatted string
     */
    public String explainSelf() {
        String typeHexString = this.toHexString();
        String typeName = "Unknown";
        switch(typeHexString) {
            case Constants.LL2P_TYPE_IS_LL3P:
                typeName = "LL3P Packet";
                break;
            case Constants.LL2P_TYPE_IS_TEXT:
                typeName = "Text";
                break;
            default:
                break;
        }
        return "LL3P type: " + typeName;
    }
}
