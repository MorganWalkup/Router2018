package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Class representing the source and destination address fields within
 * LL2P packets and frames
 * Created by morganwalkup on 1/24/18.
 */

public class LL2PAddressField implements HeaderField {

    /** Integer value of the address */
    private Integer address;
    /** True if the contained address is a source address */
    private Boolean isSourceAddress;
    /** Contains the explanation of the contents of this field */
    private String explanation;

    /**
     * Constructor accepting an integer address
     * @param address Integer representation of the source/destination address
     * @param isSource True if the address is a source address
     */
    public LL2PAddressField(Integer address, boolean isSource) {
        this.address = address;
        this.isSourceAddress = isSource;
        this.setExplanation();
    }

    /**
     * Constructor accepting a string address
     * @param address Hex string representation of the source/destination address
     * @param isSource True if the address is a source address
     */
    public LL2PAddressField(String address, boolean isSource) {
        this.address = Integer.parseInt(address, Constants.HEX_BASE);
        this.isSourceAddress = isSource;
        this.setExplanation();
    }

    /***
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return address.toString();
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
        String hexString = Integer.toHexString(this.address);
        hexString = Utilities.padHexString(hexString, Constants.LL2P_ADDR_FIELD_LENGTH);
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
        String explanationString = (this.isSourceAddress) ? "Source LL2P Address: " : "Destination LL2P Address: ";
        explanationString += this.address.toString();
        this.explanation = explanationString;
    }

    /**
     * Returns true if this object is an address field
     * @return True if this object is an adress field, false if it's a destination field
     */
    public Boolean isSourceAddressField() {
        return this.isSourceAddress;
    }
}
