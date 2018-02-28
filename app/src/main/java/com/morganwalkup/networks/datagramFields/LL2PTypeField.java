package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Class representing the type fields within LL2P packets and frames
 * Created by morganwalkup on 1/24/18.
 */

public class LL2PTypeField implements HeaderField {

    /** Integer value of the type of the payload */
    private Integer type;
    /** Explanation of the contents of this field */
    private String explanation;

    /**
     * Constructor accepting an integer type value
     * @param typeValue Integer representation of the type
     */
    public LL2PTypeField(Integer typeValue) {
        this.type = typeValue;
        this.setExplanation();
    }

    /**
     * Constructor accepting a string type value
     * @param typeValue String representation of the type
     */
    public LL2PTypeField(String typeValue) {
        this.type = Integer.parseInt(typeValue, Constants.HEX_BASE);
        this.setExplanation();
    }

    /***
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
        return this.toHexString();
    }

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    public String toHexString() {
        String hexString = Integer.toHexString(this.type);
        hexString = Utilities.padHexString(hexString, Constants.LL2P_TYPE_FIELD_LENGTH);
        return hexString;
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
        return this.explanation;
    }

    /**
     * Constructs an explanation string using the fields of this object
     */
    private void setExplanation() {
        String typeHexString = this.toHexString();
        String typeName = "Unknown";
        switch(typeHexString) {
            case Constants.LL2P_TYPE_IS_LL3P:
                typeName = "LL3P Packet";
                break;
            case Constants.LL2P_TYPE_IS_RESERVED:
                typeName = "Reserved";
                break;
            case Constants.LL2P_TYPE_IS_LRP:
                typeName = "LRP";
                break;
            case Constants.LL2P_TYPE_IS_ECHO_REQUEST:
                typeName = "Echo Request";
                break;
            case Constants.LL2P_TYPE_IS_ECHO_REPLY:
                typeName = "Echo Reply";
                break;
            case Constants.LL2P_TYPE_IS_ARP_REQUEST:
                typeName = "ARP Request";
                break;
            case Constants.LL2P_TYPE_IS_ARP_REPLY:
                typeName = "ARP Reply";
                break;
            case Constants.LL2P_TYPE_IS_TEXT:
                typeName = "Text";
                break;
            default:
                break;
        }
        String explanationString = "LL2P type: " + typeName;
        this.explanation = explanationString;
    }

}
