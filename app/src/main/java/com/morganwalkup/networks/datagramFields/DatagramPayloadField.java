package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.Datagram;
import com.morganwalkup.support.DatagramFactory;
import com.morganwalkup.support.Utilities;

/**
 * Adapter class adapting a datagram packet into a LL2P field
 * Created by morganwalkup on 1/24/18.
 */

public class DatagramPayloadField implements HeaderField {

    /** Generic datagram */
    private Datagram packet;
    /** String holding payload summary */
    private String summary;

    /**
     * Constructor accepting a datagram packet
     * @param pkt The datagram packet holding data
     */
    public DatagramPayloadField(Datagram pkt) {
        this.packet = pkt;
    }

    /**
     * Constructor accepting a string of payload data in the form "<PAYLOAD_TYPE><PAYLOAD_CONTENT>"
     * @param payloadData String data used to construct a datagram
     */
    public DatagramPayloadField(String payloadData) {
        String payloadType = payloadData.substring(0, Constants.LL2P_TYPE_FIELD_LENGTH*2);
        String payloadContent = payloadData.substring(Constants.LL2P_TYPE_FIELD_LENGTH*2);
        switch(payloadType) {
            case Constants.LL2P_TYPE_IS_TEXT:
                this.packet = DatagramFactory.getInstance().getItem(Constants.TEXT_DATAGRAM, payloadContent);
                this.summary = payloadContent;
                break;
            case Constants.LL2P_TYPE_IS_ECHO_REQUEST:
                this.packet = DatagramFactory.getInstance().getItem(Constants.TEXT_DATAGRAM, payloadContent);
                this.summary = "Echo Request";
                break;
            case Constants.LL2P_TYPE_IS_ECHO_REPLY:
                this.packet = DatagramFactory.getInstance().getItem(Constants.TEXT_DATAGRAM, payloadContent);
                this.summary = "Echo Reply";
                break;
            case Constants.LL2P_TYPE_IS_ARP_REQUEST:
                this.packet = DatagramFactory.getInstance().getItem(Constants.ARP_DATAGRAM, payloadContent);
                this.summary = "ARP Request";
                break;
            case Constants.LL2P_TYPE_IS_ARP_REPLY:
                this.packet = DatagramFactory.getInstance().getItem(Constants.ARP_DATAGRAM, payloadContent);
                this.summary = "ARP Reply";
                break;
            case Constants.LL2P_TYPE_IS_LRP:
                this.packet = DatagramFactory.getInstance().getItem(Constants.LRP_PACKET, payloadContent);
                this.summary = "LRP Update";
                break;
            case Constants.LL2P_TYPE_IS_LL3P:
                this.packet = DatagramFactory.getInstance().getItem(Constants.LL3P_DATAGRAM, payloadContent);
                String ll3pPayloadContent = payloadContent.substring(
                        Constants.LL3P_PAYLOAD_FIELD_BEGIN_INDEX*2,
                        payloadContent.length() - Constants.LL3P_CHECKSUM_FIELD_LENGTH*2);
                this.summary = "LL3: " + ll3pPayloadContent;
                break;
            default:
                break;
        }
    }

    /**
     * Returns the packet data
     * @return the Datagram packet
     */
    public Datagram getPayload() {
        return this.packet;
    }

    /***
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return packet.toString();
    }

    /**
     * Returns the appropriate string to be embedded in a lab packet or frame
     * @return The string to be embedded
     */
    public String toTransmissionString() {
        return packet.toTransmissionString();
    }

    /**
     * Returns hex representation of the packet or frame contents
     * @return Hex representation
     */
    public String toHexString() {
        return packet.toHexString();
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
        return packet.toProtocolExplanationString();
    }

    /**
     * Returns summary of payload content
     * @return Summary string of payload
     */
    public String toSummaryString() { return this.summary; }
}
