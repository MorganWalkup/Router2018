package com.morganwalkup.networks.datagram;

import android.util.Log;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagramFields.CRC;
import com.morganwalkup.networks.datagramFields.DatagramPayloadField;
import com.morganwalkup.networks.datagramFields.LL2PAddressField;
import com.morganwalkup.networks.datagramFields.LL2PTypeField;
import com.morganwalkup.support.DatagramFactory;
import com.morganwalkup.support.HeaderFieldFactory;

/**
 * Representation of an LL2P Frame with address fields, type field, payload, and crc
 * Created by morganwalkup on 1/25/18.
 */

public class LL2PFrame implements Datagram {

    /** Destination address field of the LL2P frame */
    private LL2PAddressField destinationAddress;
    /** Source address field of the LL2P frame */
    private LL2PAddressField sourceAddress;
    /** Type field of the LL2P frame */
    private LL2PTypeField type;
    /** Payload field of the LL2P frame */
    private DatagramPayloadField payload;
    /** CRC field of the LL2P field */
    private CRC crc;

    /**
     * Constructor with all fields provided
     * @param destAddress The destination address for the LL2P frame
     * @param srcAddress The source address for the LL2P frame
     * @param type The type of the payload in the LL2P frame
     * @param payload The payload of the LL2P frame
     * @param crc The CRC field of the LL2P frame
     */
    public LL2PFrame(LL2PAddressField destAddress, LL2PAddressField srcAddress, LL2PTypeField type, DatagramPayloadField payload, CRC crc) {
        this.destinationAddress = destAddress;
        this.sourceAddress = srcAddress;
        this.type = type;
        this.payload = payload;
        this.crc = crc;
    }

    /**
     * Constructor taking a byte array of the LL2P frame data
     * @param byteArray
     */
    public LL2PFrame(byte[] byteArray) {
        this(new String(byteArray));
    }

    /**
     * Constructor taking a string of the LL2P frame data
     * @param dataString - string holding the data of the LL2PFrame
     */
    public LL2PFrame(String dataString) {
        // Parse and save destination address
        String destAddrString = dataString.substring(2*Constants.LL2P_DEST_FIELD_OFFSET,
                2*Constants.LL2P_ADDR_FIELD_LENGTH);
        this.destinationAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_DESTINATION_ADDRESS, destAddrString);
        // Parse and save source address
        String srcAddrString = dataString.substring(2*Constants.LL2P_SRC_FIELD_OFFSET,
                2*Constants.LL2P_SRC_FIELD_OFFSET + 2*Constants.LL2P_ADDR_FIELD_LENGTH);
        this.sourceAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_SOURCE_ADDRESS, srcAddrString);
        // Parse and save type field
        String typeString = dataString.substring(2*Constants.LL2P_TYPE_FIELD_OFFSET,
                2*Constants.LL2P_TYPE_FIELD_OFFSET + 2*Constants.LL2P_TYPE_FIELD_LENGTH);
        this.type = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_TYPE, typeString);
        // Parse and save payload field
        String payloadString = dataString.substring(2*Constants.LL2P_PAYLOAD_FIELD_OFFSET,
                dataString.length() - 2*Constants.LL2P_CRC_FIELD_LENGTH);
        this.makePayloadField(typeString, payloadString);
        // Parse and save CRC field
        int crcStart = dataString.length() - 2*Constants.LL2P_CRC_FIELD_LENGTH;
        String crcString = dataString.substring(crcStart);
        this.crc = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_CRC, crcString);
    }

    public LL2PAddressField getDestinationAddress() {
        return this.destinationAddress;
    }

    public LL2PAddressField getSourceAddress() {
        return this.sourceAddress;
    }

    public LL2PTypeField getType() {
        return this.type;
    }

    public DatagramPayloadField getPayload() {
        return this.payload;
    }

    public CRC getCRC() {
        return this.crc;
    }

    /**
     * Utility to easily add a payload to the frame
     * @param typeString - The hex string defining the type of the payload
     * @param payloadString - The string containing payload contents
     */
    private void makePayloadField(String typeString, String payloadString) {
        String payloadData = typeString + payloadString;
        this.payload = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_PAYLOAD, payloadData);
        return;
    }

    /**
     * Returns string suitable for transmission, appropriately displaying contents
     * @return transmission string
     */
    public String toString() {
        String displayString = "";
        displayString += this.getDestinationAddress().toString();
        displayString += this.getSourceAddress().toString();
        displayString += this.getType().toString();
        displayString += this.getPayload().toString();
        displayString += this.getCRC().toString();
        return displayString;
    }

    /**
     * Returns string of the contents in the hex format
     * If the field is ASCII data, then the hex characters representing ASCII data are returned instead
     * @return the hex string
     */
    public String toHexString() {
        String hexString = "";
        hexString += this.getDestinationAddress().toHexString();
        hexString += this.getSourceAddress().toHexString();
        hexString += this.getType().toHexString();
        hexString += this.getPayload().toHexString();
        hexString += this.getCRC().toHexString();
        return hexString;
    }

    /**
     * Returns full explanation of the datagram and all its fields
     * If this datagram contains other datagrams, also include their protocol explanation strings
     * @return the protocol explanation string
     */
    public String toProtocolExplanationString() {
        String explanationString = "";
        explanationString += (this.getDestinationAddress().explainSelf() + "\n");
        explanationString += (this.getSourceAddress().explainSelf() + "\n");
        explanationString += (this.getType().explainSelf() + "\n");
        explanationString += (this.getPayload().explainSelf() + "\n");
        explanationString += (this.getCRC().explainSelf());

        return explanationString;
    }

    /**
     * Returns one-line string that displays the top level protocol and info about that protocol
     * @return the protocol summary string
     */
    public String toSummaryString() {
        // TODO: Add payload content summary
        String summaryString = "";
        summaryString += (" | " + this.destinationAddress.toHexString());
        summaryString += (" | " + this.sourceAddress.toHexString());
        summaryString += (" | " + this.getType().toHexString());
        return summaryString;
    }

    /**
     * Returns string suitable for transmission in the LAB format
     * All numbers are transmitted as hex characters in the appropriate field width
     * and all letters are transmitted as ASCII
     * @return transmission string
     */
    public String toTransmissionString() {
        String transmissionString = "";
        transmissionString += this.getDestinationAddress().toTransmissionString();
        transmissionString += this.getSourceAddress().toTransmissionString();
        transmissionString += this.getType().toTransmissionString();
        transmissionString += this.getPayload().toTransmissionString();
        transmissionString += this.getCRC().toTransmissionString();
        return transmissionString;
    }

}
