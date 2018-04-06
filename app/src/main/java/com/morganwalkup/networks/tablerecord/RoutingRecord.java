package com.morganwalkup.networks.tablerecord;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagramFields.NetworkDistancePair;
import com.morganwalkup.support.Utilities;

/**
 * A record in the routing table. Contains destination network, distance, and next hop
 * Created by morganwalkup on 3/11/18.
 */

public class RoutingRecord extends TableRecordBase {
    /** Contains the number of a remote network and the distance to that network */
    private NetworkDistancePair networkDistancePair;
    public NetworkDistancePair getNetworkDistancePair() { return this.networkDistancePair; }
    /** LL3P address of the source of this route, which is also the next hop */
    private Integer nextHop;
    public Integer getNextHop() { return this.nextHop; }
    /** Key of this record, equal to network*256*256 + nextHop */
    private Integer key;
    public Integer getKey() { return this.key; }

    /**
     * Constructor accepting all fields
     * @param networkNumber - The LL3PAddress of the remote network
     * @param distance - The distance between this router and the remote network
     * @param nextHop - The LL3PAddress of the next hop
     */
    public RoutingRecord(Integer networkNumber, Integer distance, Integer nextHop) {
        super();
        this.networkDistancePair = new NetworkDistancePair(networkNumber, distance);
        this.nextHop = nextHop;
        this.key = networkNumber*256*256 + nextHop;
    }

    /**
     * Constructor accepting a string of data in the format <NW><NH><Distance>
     * @param dataString - String of routing record data
     */
    public RoutingRecord(String dataString) {
        super();

        String networkString = dataString.substring(
                0,
                Constants.LL3P_NETWORK_ADDR_FIELD_LENGTH * 2);
        String nextHopString = dataString.substring(
                Constants.LL3P_NETWORK_ADDR_FIELD_LENGTH * 2,
                Constants.LL3P_NETWORK_ADDR_FIELD_LENGTH * 2 + Constants.LL3P_ADDR_FIELD_LENGTH * 2);
        String distanceString = dataString.substring(
                Constants.LL3P_NETWORK_ADDR_FIELD_LENGTH * 2 + Constants.LL3P_ADDR_FIELD_LENGTH * 2);

        Integer networkNumber = Integer.parseInt(networkString, Constants.HEX_BASE);
        Integer nextHop = Integer.parseInt(nextHopString, Constants.HEX_BASE);
        Integer distance = Integer.parseInt(distanceString);

        this.networkDistancePair = new NetworkDistancePair(networkNumber, distance);
        this.nextHop = nextHop;
        this.key = networkNumber*256*256 + nextHop;
    }

    /**
     * Return the distance of this routing record
     * @return The distance of this routing record
     */
    public Integer getDistance() {
        return this.networkDistancePair.getDistance();
    }

    /**
     * Return the network of this routing record
     * @return The network of this routing record
     */
    public Integer getNetwork() {
        return this.networkDistancePair.getNetwork();
    }

    @Override
    public String toString() {
        String resultString = "";
        resultString += "NW: " + networkDistancePair.getNetwork().toString() + " | ";
        resultString += "Dist: " + networkDistancePair.getDistance().toString() + " | ";
        resultString += "NH: " + Utilities.formatLL3PAddress(nextHop) + " | ";
        resultString += "Age: " + this.getAgeInSeconds();
        return resultString;
    }
}
