package com.morganwalkup.networks.datagram;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagramFields.LL3PAddressField;
import com.morganwalkup.networks.datagramFields.LRPRouteCount;
import com.morganwalkup.networks.datagramFields.LRPSequenceNumber;
import com.morganwalkup.networks.datagramFields.NetworkDistancePair;
import com.morganwalkup.support.HeaderFieldFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing LRP Packets utilized for routing updates
 * Created by morganwalkup on 3/8/18.
 */

public class LRPPacket implements Datagram {

    /** LL3P Address of the router originating this update */
    private LL3PAddressField sourceLL3P;
    public LL3PAddressField getLL3PAddressField() { return this.sourceLL3P; }
    /** The sequence number of this LRP packet */
    private LRPSequenceNumber sequenceNumber;
    public LRPSequenceNumber getSequenceNumber() { return this.sequenceNumber; }
    /** The number of routes contained in this LRP Packet */
    private LRPRouteCount count;
    public LRPRouteCount getLRPRouteCount() { return this.count; }
    /** Collection of routes represented by network-distance pairs */
    private List<NetworkDistancePair> routes;
    public List<NetworkDistancePair> getRoutes() { return this.routes; }

    /**
     * Constructor for LRP Packet accepting a string
     * @param dataString - String containing routing data in the LRP format
     */
    public LRPPacket(String dataString) {
        int cursor; //Tracks current location in the dataString for parsing

        String sourceLL3PString = dataString.substring(
                0,
                Constants.LL3P_ADDR_FIELD_LENGTH * 2);
        cursor = Constants.LL3P_ADDR_FIELD_LENGTH * 2;
        this.sourceLL3P = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_SOURCE_ADDRESS, sourceLL3PString);

        String sequenceNumString = dataString.substring(
                cursor,
                cursor + Constants.LRP_SEQ_NUM_NIBBLES);
        cursor = cursor + Constants.LRP_SEQ_NUM_NIBBLES;
        this.sequenceNumber = HeaderFieldFactory.getInstance().getItem(Constants.LRP_SEQ_NUM, sequenceNumString);

        String routeCountString = dataString.substring(
                cursor,
                cursor + Constants.LRP_ROUTE_COUNT_NIBBLES);
        cursor = cursor + Constants.LRP_ROUTE_COUNT_NIBBLES;
        this.count = HeaderFieldFactory.getInstance().getItem(Constants.LRP_ROUTE_COUNT, routeCountString);

        this.routes = new ArrayList<>();
        for(int i = 0; i < count.getRouteCount(); i++) {
            String netDistPairString = dataString.substring(
                    cursor,
                    cursor + Constants.LRP_NET_DIST_PAIR_LENGTH * 2);
            cursor = cursor + Constants.LRP_NET_DIST_PAIR_LENGTH * 2;
            NetworkDistancePair netDistPair = HeaderFieldFactory.getInstance().getItem(
                    Constants.LRP_NET_DIST_PAIR, netDistPairString);
            this.routes.add(netDistPair);
        }
    }

    /**
     * Constructor to create LRP packets from this router to adjacent routers
     * @param ll3p - LL3P Address of this router
     * @param sequenceNumber - Sequence number of this LRP Packet
     * @param count - Number of networkDistancePairs being sent
     * @param networkDistancePairs - The network distance pairs to send
     */
    public LRPPacket(Integer ll3p, Integer sequenceNumber, Integer count, List<NetworkDistancePair> networkDistancePairs) {
        String ll3pString = Integer.toHexString(ll3p);
        this.sourceLL3P = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_SOURCE_ADDRESS, ll3pString);

        String sequenceString = Integer.toHexString(sequenceNumber);
        this.sequenceNumber = HeaderFieldFactory.getInstance().getItem(Constants.LRP_SEQ_NUM, sequenceString);

        String countString = Integer.toHexString(count);
        this.count = HeaderFieldFactory.getInstance().getItem(Constants.LRP_ROUTE_COUNT, countString);

        this.routes = networkDistancePairs;
    }

    /**
     * Returns a byte array of the transmission-ready string
     * @return - A byte array of LRP packet data
     */
    public byte[] getBytes() {
        return this.toTransmissionString().getBytes();
    }

    /**
     * Returns the number of routes in this update packet
     * @return The number of routes in this update packet
     */
    public Integer getRouteCount() {
        return this.count.getRouteCount();
    }

    /**
     * Returns string suitable for transmission, appropriately displaying contents
     * @return transmission string
     */
    public String toString() {
        String result = "";
        result += this.sourceLL3P.toString();
        result += this.sequenceNumber.toString();
        result += this.count.toString();

        for(int i = 0; i < this.getRouteCount(); i++) {
            result += this.routes.get(i).toString();
        }

        return result;
    }

    /**
     * Returns string of the contents in the hex format
     * If the field is ASCII data, then the hex characters representing ASCII data are returned instead
     * @return the hex string
     */
    public String toHexString() {
        String hexString = "";
        hexString += this.sourceLL3P.toHexString();
        hexString += this.sequenceNumber.toHexString();
        hexString += this.count.toHexString();

        for(int i = 0; i < this.getRouteCount(); i++) {
            hexString += this.routes.get(i).toHexString();
        }

        return hexString;
    }

    /**
     * Returns full explanation of the datagram and all its fields
     * If this datagram contains other datagrams, also include their protocol explanation strings
     * @return the protocol explanation string
     */
    public String toProtocolExplanationString() {
        String explanation = this.sourceLL3P.explainSelf() + "\n";
        explanation += this.sequenceNumber.explainSelf() + "\n";
        explanation += this.count.explainSelf() + "\n";

        for(int i = 0; i < this.getRouteCount(); i++) {
            explanation += this.routes.get(i).explainSelf() + "\n";
        }

        return explanation;
    }

    /**
     * Returns one-line string that displays the top level protocol and info about that protocol
     * @return the protocol summary string
     */
    public String toSummaryString() {
        //TODO: Write more meaningful summary
        String summaryString = "LRP Packet Summary";
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
        transmissionString += this.sourceLL3P.toTransmissionString();
        transmissionString += this.sequenceNumber.toTransmissionString();
        transmissionString += this.count.toTransmissionString();

        for(int i = 0; i < this.getRouteCount(); i++) {
            transmissionString += this.routes.get(i).toTransmissionString();
        }

        return transmissionString;
    }


}
