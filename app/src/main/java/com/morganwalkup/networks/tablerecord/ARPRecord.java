package com.morganwalkup.networks.tablerecord;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Utilities;

/**
 * Holds data for an ARP record within the ARP table
 * Created by morganwalkup on 2/22/18.
 */

public class ARPRecord extends TableRecordBase {

    /** The layer 2 address of the neighbor, also the key for this record */
    private Integer ll2pAddress;
    public Integer getLL2PAddress() { return this.ll2pAddress; }
    public void setLL2PAddress(Integer ll2pAddress) { this.ll2pAddress = ll2pAddress; }
    /** The layer 3 address of the neighbor */
    private Integer ll3pAddress;
    public Integer getLL3PAddress() { return this.ll3pAddress; }
    public void setLL3PAddress( Integer ll3pAddress) { this.ll3pAddress = ll3pAddress; }

    /**
     * Default constructor
     */
    public ARPRecord() {
        super();
    }

    /**
     * Constructor accepting string of parameters
     * @param ll2pAndLL3PAddress - String in the format "<ll2pAddress><ll3pAddress>"
     */
    public ARPRecord(String ll2pAndLL3PAddress) {
        super();
        String ll2pString = ll2pAndLL3PAddress.substring(0,6);
        String ll3pString = ll2pAndLL3PAddress.substring(7);
        ll2pAddress = Integer.parseInt(ll2pString, Constants.HEX_BASE);
        ll3pAddress = Integer.parseInt(ll3pString, Constants.HEX_BASE);
    }

    /**
     * Constructor accepting Integer arguments
     * @param ll2pAddress - Integer Layer 2 address of the neighbor
     * @param ll3pAddress - Integer Layer 3 address of the neighbor
     */
    public ARPRecord(Integer ll2pAddress, Integer ll3pAddress) {
        super();
        this.ll2pAddress = ll2pAddress;
        this.ll3pAddress = ll3pAddress;
    }

    /**
     * Returns the key for this record
     * @return - the ll2pAddress of this ARP record
     */
    @Override
    public Integer getKey() {
        return this.ll3pAddress;
    }

    /**
     * Returns string representation of the ARP record
     * @return - String representation of the ARP record
     */
    public String toString() {
        return "LL2P Address: " + Integer.toHexString(this.ll2pAddress) +
                ", LL3P Address: " + Utilities.padHexString(Integer.toHexString(this.ll3pAddress), Constants.LL3P_ADDR_FIELD_LENGTH);
    }

}
