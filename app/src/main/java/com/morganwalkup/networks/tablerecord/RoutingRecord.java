package com.morganwalkup.networks.tablerecord;

import com.morganwalkup.networks.datagramFields.NetworkDistancePair;

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
}
