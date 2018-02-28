package com.morganwalkup.networks.daemons;

import android.util.Log;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.table.TimedTable;
import com.morganwalkup.networks.tablerecord.ARPRecord;
import com.morganwalkup.support.Bootloader;

import java.util.Observable;
import java.util.Observer;

/**
 * Daemon responsible for ARP processes
 * Created by morganwalkup on 2/23/18.
 */

public class ARPDaemon implements Observer {

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
     * Creates an ARP record and adds it to the table
     */
    private void addARPEntry(Integer ll2pAddress, Integer ll3pAddress) {
        ARPRecord arpRecord = new ARPRecord(ll2pAddress, ll3pAddress);
        this.arpTable.addItem(arpRecord);
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

    }
}
