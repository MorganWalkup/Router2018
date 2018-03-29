package com.morganwalkup.networks.datagramFields;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Decorator class holding a pairing of a remote network number and its distance from the router
 * Created by morganwalkup on 3/8/18.
 */

public class NetworkDistancePair implements HeaderField {

    /** Network number of the remote network */
    private Integer network;
    public Integer getNetwork() { return this.network; }
    /** Distance to the remote network (in hops) */
    private Integer distance;
    public Integer getDistance() { return this.distance; }

    /**
     * Constructor for NetworkDistancePair accepting a data string
     * @param dataString - 4 character hex string in the format "<Network><Distance>"
     */
    public NetworkDistancePair(String dataString) {
        String networkString = dataString.substring(0, Constants.LRP_NETWORK_FIELD_LENGTH * 2);
        String distanceString = dataString.substring(Constants.LRP_NETWORK_FIELD_LENGTH * 2);

        this.network = Integer.parseInt(networkString, Constants.HEX_BASE);
        this.distance = Integer.parseInt(distanceString, Constants.HEX_BASE);
    }

    /**
     * Constructor for NetworkDistancePair accepting all fields
     * @param network - The number of the remote network
     * @param distance - Distance in hops to the remote network
     */
    public NetworkDistancePair(Integer network, Integer distance) {
        this.network = network;
        this.distance = distance;
    }

    /**
     * Increments this NDPair's distance by 1
     * Used when receiving LRP records from neighbors
     */
    public void incrementDistance() {
        this.distance = this.distance + 1;
    }

    /**
     * Returns a string representation of the header field
     * @return String representation of the header field
     */
    public String toString() {
        return this.network.toString() + this.distance.toString();
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
        String networkHexString = Integer.toHexString(this.network);
        networkHexString = Utilities.padHexString(networkHexString, Constants.LRP_NETWORK_FIELD_LENGTH);
        String distanceHexString = Integer.toHexString(this.distance);
        distanceHexString = Utilities.padHexString(distanceHexString, Constants.LRP_DISTANCE_FIELD_LENGTH);
        return networkHexString + distanceHexString;
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
     * @return The formatted string
     */
    public String explainSelf() {
        return "Network: " + Integer.toHexString(this.network) +
                " Distance: " + Integer.toHexString(this.distance);
    }

}
