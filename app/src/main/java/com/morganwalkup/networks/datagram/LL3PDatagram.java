package com.morganwalkup.networks.datagram;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagramFields.DatagramPayloadField;
import com.morganwalkup.networks.datagramFields.HeaderField;
import com.morganwalkup.networks.datagramFields.LL3PAddressField;
import com.morganwalkup.networks.datagramFields.LL3PChecksum;
import com.morganwalkup.networks.datagramFields.LL3PIdentifierField;
import com.morganwalkup.networks.datagramFields.LL3PTTLField;
import com.morganwalkup.networks.datagramFields.LL3PTypeField;
import com.morganwalkup.support.HeaderFieldFactory;

/**
 * Created by morganwalkup on 4/1/18.
 * Datagram for transmitting layer 3 data
 */
public class LL3PDatagram implements Datagram {

    /** Source address of this ll3p datagram */
    private LL3PAddressField sourceAddress;
    public LL3PAddressField getSourceAddress() { return this.sourceAddress; }
    /** Destination address of this ll3p datagram */
    private LL3PAddressField destinationAddress;
    public LL3PAddressField getDestinationAddress() { return this.destinationAddress; }
    /** The time to live of this datagram. 1 byte field with max value of 15 */
    private LL3PTTLField ttl;
    public LL3PTTLField getTTL() { return this.ttl; }
    /** 2 byte field with a value cycling from 0 to 65,535 */
    private LL3PIdentifierField identifier;
    public LL3PIdentifierField getIdentifier() { return this.identifier; }
    /** Only valid type is 0x8001 - text payload */
    private LL3PTypeField type;
    public LL3PTypeField getType() { return this.type; }
    /** Placeholder - Put 4 hex characters in this field */
    private LL3PChecksum checksum;
    public LL3PChecksum getChecksum() { return this.checksum; }
    /** Payload of the LL3PDatagram */
    private DatagramPayloadField datagramPayload;
    public DatagramPayloadField getDatagramPayload() { return this.datagramPayload; }


    /**
     * Constructor accepting byte array
     * @param data - LL3P Datagram data arranged in a byte array according to LL3 Protocol
     */
    public LL3PDatagram(byte[] data) {
        // Call data string constructor
        this(new String(data));
    }

    /**
     * Constructor accepting string of data
     * @param dataString - String of ll3p datagram content
     */
    public LL3PDatagram(String dataString) {
        int cursor; //Tracks current location in the dataString for parsing

        String sourceLL3PString = dataString.substring(
                0,
                Constants.LL3P_ADDR_FIELD_LENGTH * 2);
        cursor = Constants.LL3P_ADDR_FIELD_LENGTH * 2;
        this.sourceAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_SOURCE_ADDRESS, sourceLL3PString);

        String destLL3PString = dataString.substring(
                cursor,
                cursor + Constants.LL3P_ADDR_FIELD_LENGTH * 2);
        cursor += Constants.LL3P_ADDR_FIELD_LENGTH * 2;
        this.destinationAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_DESTINATION_ADDRESS, destLL3PString);

        String typeString = dataString.substring(
                cursor,
                cursor + Constants.LL3P_TYPE_FIELD_LENGTH * 2);
        cursor += Constants.LL3P_TYPE_FIELD_LENGTH * 2;
        this.type = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_TYPE, typeString);

        String identifierString = dataString.substring(
                cursor,
                cursor + Constants.LL3P_IDENTIFIER_FIELD_LENGTH * 2);
        cursor += Constants.LL3P_IDENTIFIER_FIELD_LENGTH * 2;
        this.identifier = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_IDENTIFIER, identifierString);

        String ttlString = dataString.substring(
                cursor,
                cursor + Constants.LL3P_TTL_FIELD_LENGTH * 2);
        cursor += Constants.LL3P_TTL_FIELD_LENGTH * 2;
        this.ttl = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_TTL, ttlString);

        String payloadString = dataString.substring(
                cursor,
                dataString.length() - Constants.LL3P_CHECKSUM_FIELD_LENGTH*2);
        this.datagramPayload = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_PAYLOAD_FIELD, Constants.LL2P_TYPE_IS_TEXT + payloadString);

        String checksumString = dataString.substring(
                dataString.length() - Constants.LL3P_CHECKSUM_FIELD_LENGTH*2);
        this.checksum = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_CHECKSUM, checksumString);

    }

    /**
     * Constructor accepting all fields
     * @param sourceAddress - Source address for this datagram
     * @param destinationAddress - Destination address for this datagram
     * @param ttl - Time to live of this datagram
     * @param identifier - Unique identifier for this datagram
     * @param type - Type of the payload - Only valid type is 0x8001 text payload
     * @param checksum - Placeholder hex value
     * @param payload - The payload of this datagram
     */
    public LL3PDatagram(LL3PAddressField sourceAddress, LL3PAddressField destinationAddress, LL3PTTLField ttl, LL3PIdentifierField identifier, LL3PTypeField type, LL3PChecksum checksum, DatagramPayloadField payload) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.ttl = ttl;
        this.identifier = identifier;
        this.type = type;
        this.checksum = checksum;
        this.datagramPayload = payload;
    }

    /**
     * Returns string suitable for transmission, appropriately displaying contents
     * @return transmission string
     */
    public String toString() {
        return toTransmissionString();
    }

    /**
     * Returns string of the contents in the hex format
     * If the field is ASCII data, then the hex characters representing ASCII data are returned instead
     * @return the hex string
     */
    public String toHexString() {
        String hexString = sourceAddress.toHexString() +
                destinationAddress.toHexString() +
                type.toHexString() +
                identifier.toHexString() +
                ttl.toHexString() +
                datagramPayload.toHexString() +
                checksum.toHexString();
        return hexString;
    }

    /**
     * Returns full explanation of the datagram and all its fields
     * If this datagram contains other datagrams, also include their protocol explanation strings
     * @return the protocol explanation string
     */
    public String toProtocolExplanationString() {
        String explanationString = sourceAddress.explainSelf() + "\n" +
                destinationAddress.explainSelf() + "\n" +
                type.explainSelf() + "\n" +
                identifier.explainSelf() + "\n" +
                ttl.explainSelf() + "\n" +
                datagramPayload.explainSelf() + "\n" +
                checksum.explainSelf();
        return explanationString;
    }

    /**
     * Returns one-line string that displays the top level protocol and info about that protocol
     * @return the protocol summary string
     */
    public String toSummaryString() {
        return "LL3P Packet: Blah";
    }

    /**
     * Returns string suitable for transmission in the LAB format
     * All numbers are transmitted as hex characters in the appropriate field width
     * and all letters are transmitted as ASCII
     * @return transmission string
     */
    public String toTransmissionString() {
        String transString = sourceAddress.toTransmissionString() +
                destinationAddress.toTransmissionString() +
                type.toTransmissionString() +
                identifier.toTransmissionString() +
                ttl.toTransmissionString() +
                datagramPayload.toTransmissionString() +
                checksum.toTransmissionString();
        return transString;
    }

    /**
     * Decrements this datagram's TTL field
     */
    public void decrementTTL() {
        ttl.decrementTTL();
    }

}
