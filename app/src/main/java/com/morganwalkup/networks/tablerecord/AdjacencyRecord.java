package com.morganwalkup.networks.tablerecord;

import android.util.Log;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.GetIPAddress;

import java.net.InetAddress;

/**
 * Represents an adjacency record in the router tables
 * Created by morganwalkup on 1/25/18.
 */

public class AdjacencyRecord extends TableRecordBase {

    /** The key for this record */
    private Integer ll2pAddress;
    /** Address used in transmitting data over a UDP port */
    private InetAddress ipAddress;

    /**
     * Constructor accepting all fields
     * @param ipAddress - The ip address used by this record
     * @param ll2pAddress - the LL2P address serving as key for this record
     */
    public AdjacencyRecord(InetAddress ipAddress, Integer ll2pAddress) {
        super();
        this.ipAddress = ipAddress;
        this.ll2pAddress = ll2pAddress;
    }

    /**
     * Constructor accepting a string holding record data
     * @param ipAndLL2PAddress - Parseable string of record data in the form "<ipAddress><ll2pAddress>"
     */
    public AdjacencyRecord(String ipAndLL2PAddress) {
        super();
        String ll2pAddressString = ipAndLL2PAddress.substring(ipAndLL2PAddress.length() - Constants.LL2P_ADDR_FIELD_LENGTH * 2);
        try {
            this.setLL2PAddress(Integer.parseInt(ll2pAddressString, Constants.HEX_BASE));
        } catch (Exception e){
            Log.i(Constants.LOG_TAG, e.getMessage());
        }
        String ipAddressString = ipAndLL2PAddress.substring(0,ipAndLL2PAddress.length() - Constants.LL2P_ADDR_FIELD_LENGTH * 2);
        this.setIpAddress(GetIPAddress.getInstance().getInetAddress(ipAddressString));
    }

    public Integer getLL2PAddress() { return this.ll2pAddress; }
    public void setLL2PAddress(Integer ll2pAddress) { this.ll2pAddress = ll2pAddress; }

    public InetAddress getIpAddress() { return this.ipAddress; }
    public void setIpAddress(InetAddress ipAddress) { this.ipAddress = ipAddress; }

    /**
     * Adjacency records have no age, so this will return 0
     * @return 0
     */
    @Override
    public Integer getAgeInSeconds() {
        return 0;
    }

    /**
     * Returns the ll2pAddress as the key
     * @return the key in the record
     */
    public Integer getKey() {
        return ll2pAddress;
    }

    /**
     * Returns string representation of this record
     * @return string representation
     */
    @Override
    public String toString() {
        return "LL2P Address: " + Integer.toHexString(this.ll2pAddress) + "; IP Address: " + this.ipAddress;
    }



}
