package com.morganwalkup.networks.daemons;

import android.util.Log;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.ARPDatagram;
import com.morganwalkup.networks.table.TimedTable;
import com.morganwalkup.networks.tablerecord.ARPRecord;
import com.morganwalkup.networks.tablerecord.TableRecord;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.DatagramFactory;
import com.morganwalkup.support.LabException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Daemon responsible for ARP processes
 * Created by morganwalkup on 2/23/18.
 */

public class ARPDaemon implements Observer, Runnable {

    /** The one and only instance of this class */
    private static final ARPDaemon ourInstance = new ARPDaemon();
    public static ARPDaemon getInstance() {
        return ourInstance;
    }

    /** Contains the ARP table */
    private TimedTable arpTable;
    public TimedTable getARPTable(){ return this.arpTable; }

    /** Reference to the ll2Daemon */
    private LL2Daemon ll2Daemon;

    /**
     * Default constructor
     */
    ARPDaemon() {
        arpTable = new TimedTable();
    }

    /**
     * Called when observed entities change
     * @param observable - The observed object
     * @param object - Object containing updated state
     */
    @Override
    public void update(Observable observable, Object object) {
        if(observable instanceof Bootloader) {
            this.ll2Daemon = LL2Daemon.getInstance();
        }
    }

    /**
     * Returns the ARP table as a list of Table Records
     * @return The list of table records in the ARP table
     */
    public List<TableRecord> getARPTableAsList() {
        return this.arpTable.getTableAsList();
    }

    /**
     * Creates an ARP record and adds it to the table
     */
    private void addARPEntry(Integer ll2pAddress, Integer ll3pAddress) {
        ARPRecord arpRecord = new ARPRecord(ll2pAddress, ll3pAddress);
        this.arpTable.addItem(arpRecord);
    }

    /**
     * Touches the ARP record with the LL3PAddress, or does nothing if that record doesn't exist
     * @param ll3pAddress
     */
    private void updateARPEntry(Integer ll3pAddress) {
        try {
            ARPRecord arpRecord = (ARPRecord)this.arpTable.getItem(ll3pAddress);
            this.arpTable.touch(ll3pAddress);
        } catch(LabException e) {
            Log.i(Constants.LOG_TAG, e.getMessage());
        }
    }

    /**
     * Tests functions in the ARP daemon
     */
    public void testARP() {
        Integer ll2pAddress = Integer.parseInt("FA1AF1", Constants.HEX_BASE);
        Integer ll3pAddress = Integer.parseInt("E0E0", Constants.HEX_BASE);
        addARPEntry(ll2pAddress, ll3pAddress);
        Log.i(Constants.LOG_TAG, "Added ARP Entry");
        this.arpTable.expireRecords(500);
        Log.i(Constants.LOG_TAG, "Expired ARP Entry?");
        this.arpTable.expireRecords(5);
        Log.i(Constants.LOG_TAG, "Expired ARP Entry for real");
    }

    /**
     * Called whenever a scheduler wants to spin off a thread for an ARP daemon job
     */
    public void run() {
        Log.i(Constants.LOG_TAG, "Run() called to expire ARP Entries");
        this.arpTable.expireRecords(Constants.ARP_RECORD_MAX_AGE);
    }

    /**
     * Retrieves the corresponding LL2PAddress for the given ll3pAddress
     * @param ll3pAddress - The LL3PAddress of the ARP Record
     * @return the LL2PAddress Integer corresponding to the LL3PAddress
     */
    public Integer getMACAddress(Integer ll3pAddress) throws LabException {
        try {
            ARPRecord arpRecord = (ARPRecord)this.arpTable.getItem(ll3pAddress);
            return arpRecord.getLL2PAddress();
        } catch(LabException e) {
            Log.i(Constants.LOG_TAG, e.getMessage());
            throw(e);
        }
    }

    /**
     * Retrieves the LL3P Addresses contained in the ARP Table
     * @return - A list of Integer LL3P addresses
     */
    public List<Integer> getAttachedNodes() {
        List<Integer> ll3pList = new ArrayList<Integer>();
        List<TableRecord> arpList = this.arpTable.getTableAsList();
        for(int i = 0; i < arpList.size(); i++) {
            ARPRecord arpRecord = (ARPRecord)arpList.get(i);
            Integer ll3pAddress = arpRecord.getLL3PAddress();
            ll3pList.add(ll3pAddress);
        }

        return ll3pList;
    }

    /**
     * Processes an ARP Reply packet and adds/updates an ARPRecord in the arp table
     * @param ll2pAddress - LL2PAddress for the ARP record
     * @param arpDatagram - The ARP Datagram received
     */
    public void processARPReply(Integer ll2pAddress, ARPDatagram arpDatagram) {
        Integer ll3pAddress = arpDatagram.getLL3PAddressInteger();
        this.addARPEntry(ll2pAddress, ll3pAddress);
        this.updateARPEntry(ll3pAddress);
    }

    /**
     * Processes an ARP Request packet, updates the arp table, and sends an ARP reply
     * @param ll2pAddress
     * @param arpDatagram
     */
    public void processARPRequest(Integer ll2pAddress, ARPDatagram arpDatagram) {
        Integer ll3pAddress = arpDatagram.getLL3PAddressInteger();
        this.addARPEntry(ll2pAddress, ll3pAddress);
        this.updateARPEntry(ll3pAddress);
    }

    /**
     * Sends an ARP request to the given ll2pAddress
     * @param ll2pAddress
     */
    public void sendARPRequest(Integer ll2pAddress) {
        ll2Daemon.sendARPRequest(ll2pAddress);
    }
}
