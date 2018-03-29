package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Holds a two-byte integer LL3P address in the format XX.YY
 * Where XX is the network number and YY is the host number
 * Created by morganwalkup on 2/23/18.
 */

public class LL3PAddressField implements HeaderField {

    /** The address is stored here */
    private Integer address;
    public Integer getAddress() { return this.address; }
    /** The network part of the address is saved here */
    private Integer networkNumber;
    public Integer getNetworkNumber() { return this.networkNumber; }
    /** The host portion of the address is saved here */
    private Integer hostNumber;
    public Integer getHostNumber() { return this.hostNumber; }
    /** True if this object stores a source address */
    private Boolean isSourceAddress;
    /** Contains an explanation string. Something like "LL3P Source Address 14.12 (0x0E0C)" */
    private String explanationString;

    /**
     * Constructor expecting data string and boolean of source address-ness
     * @param dataString - String of address
     * @param isSourceAddress - Whether or not this is a source address
     */
    public LL3PAddressField(String dataString, Boolean isSourceAddress) {
        this.address = Integer.parseInt(dataString, Constants.HEX_BASE);

        String networkString = dataString.substring(0,Constants.LL3P_SRC_ADDR_FIELD_LENGTH * 2);
        this.networkNumber = Integer.parseInt(networkString, Constants.HEX_BASE);

        String hostString = dataString.substring(Constants.LL3P_SRC_ADDR_FIELD_LENGTH * 2);
        this.hostNumber = Integer.parseInt(hostString, Constants.HEX_BASE);

        this.isSourceAddress = isSourceAddress;

        if(this.isSourceAddress) {
            this.explanationString = "LL3P Source Address: "
                    + networkNumber + "." + hostNumber
                    + " (0x" + dataString + ")";
        } else {
            this.explanationString = "LL3P Destination Address: "
                    + networkNumber + "." + hostNumber
                    + " (0x" + dataString + ")";
        }
    }

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return networkNumber + "." + hostNumber;
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
        return Utilities.padHexString(Integer.toHexString(address), Constants.LL3P_ADDR_FIELD_LENGTH);
    }

    /**
     * Returns ASCII string where each hex byte is converted to corresponding ASCII character
     * @return ASCII string
     */
    public String toASCIIString() {
        return Utilities.convertHexToASCII(toHexString());
    }

    /**
     * Returns formatted string displaying the content and meaning of the field
     * @return The formatted string
     */
    public String explainSelf() {
        return this.explanationString;
    }

}
