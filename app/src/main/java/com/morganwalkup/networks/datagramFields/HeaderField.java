package com.morganwalkup.networks.datagramFields;

/**
 * Interface for all header fields in LL2P packets and frames
 * Created by morganwalkup on 1/24/18.
 */

public interface HeaderField {

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    String toString();

    /**
     * Returns the appropriate string to be embedded in a lab packet or frame
     * @return The string to be embedded
     */
    String toTransmissionString();

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    String toHexString();

    /**
     * Returns ASCII string where each hex byte is converted to corresponding ASCII character
     * @return ASCII string
     */
    String toASCIIString();

    /**
     * Returns formatted string displaying the content and meaning of the field
     * @return The formatted string
     */
    String explainSelf();

}
