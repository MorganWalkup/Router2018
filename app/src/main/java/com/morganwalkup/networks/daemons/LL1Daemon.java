package com.morganwalkup.networks.daemons;

import android.os.AsyncTask;
import android.util.Log;

import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.LL2PFrame;
import com.morganwalkup.networks.datagramFields.LL2PTypeField;
import com.morganwalkup.networks.table.Table;
import com.morganwalkup.networks.tablerecord.AdjacencyRecord;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.DatagramFactory;
import com.morganwalkup.support.FrameLogger;
import com.morganwalkup.support.GetIPAddress;
import com.morganwalkup.support.LabException;
import com.morganwalkup.support.ReceiveLayer1Frame;
import com.morganwalkup.support.SendLayer1Frame;
import com.morganwalkup.support.TableRecordFactory;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by morganwalkup on 2/1/18.
 * The Layer 1 Daemon serving as the interface between layer 1 and layer 2 data
 */

public class LL1Daemon extends Observable implements Observer {

    /** The one and only instance of this class */
    private static final LL1Daemon ourInstance = new LL1Daemon();
    public static LL1Daemon getInstance() {
        return ourInstance;
    }

    /** Maintains adjacency relationships between LL2P addresses and their matching InetAddress (IP address) objects. */
    private Table adjacencyTable;
    public Table getAdjacencyTable() { return adjacencyTable; }

    /** Translates IP address string objects to valid InetAddress objects. */
    private GetIPAddress nameserver;

    /** Reference to the high level UI manager for displaying messages on the screen */
    private UIManager uiManager;

    /** Local private reference to the singleton TableRecordFactory */
    private TableRecordFactory factory;

    /** Instance of the class that can send a UDP packet */
    private SendLayer1Frame frameTransmitter;

    /** Reference to the singleton daemon class that handles layer 2 processing */
    private LL2Daemon ll2Daemon;

    /** Create the instance of the LL1Daemon */
    private LL1Daemon() {
        adjacencyTable = new Table();
    }

    /**
     * Receives updates from classes observed by the frame logger
     * @param observable
     * @param o
     */
    public void update(Observable observable, Object o) {
        if(observable instanceof Bootloader) {
            nameserver = GetIPAddress.getInstance();
            factory = TableRecordFactory.getInstance();
            addObserver(FrameLogger.getInstance());
            uiManager = UIManager.getInstance();
            ll2Daemon = LL2Daemon.getInstance();
            addObserver(ll2Daemon);
            frameTransmitter = new SendLayer1Frame();
            //Spin off thread for UDP frame reception
            new ReceiveLayer1Frame().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /**
     * Removes the specified record from the adjacency table
     * @param recordToRemove The record to remove
     */
    public void removeAdjacencyRecord(AdjacencyRecord recordToRemove) {
        adjacencyTable.removeItem(recordToRemove.getKey());
    }


    /**
     * Creates an adjacency record and adds it to the table
     * @param ipAddress IPAddress of the adjacency record
     * @param LL2PAddress LL2PAddress of the adjacency record
     */
    public void addAdjacency(String ipAddress, String LL2PAddress) {
        String inputData = ipAddress + LL2PAddress;
        AdjacencyRecord adjacencyRecord = factory.getItem(Constants.ADJACENCY_RECORD, inputData);
        adjacencyTable.addItem(adjacencyRecord);
        ll2Daemon.sendARPRequest(Integer.parseInt(LL2PAddress, Constants.HEX_BASE));
        // Notify observers of change
        setChanged();
        notifyObservers(adjacencyRecord);
    }

    /**
     * Receives an LL2P frame and spins off a thread to transmit the frame
     * @param ll2p The LL2P frame to transmit
     */
    public void sendFrame(LL2PFrame ll2p) {
        byte[] packetData = ll2p.toTransmissionString().getBytes();
        int packetLength = ll2p.toTransmissionString().length();
        try {
            // Get the destination IP address
            Integer destinationLL2PAddress = ll2p.getDestinationAddress().getAddress();
            AdjacencyRecord packetAdjacencyRecord = (AdjacencyRecord)this.getAdjacencyTable().getItem(destinationLL2PAddress);
            InetAddress destinationAddress = packetAdjacencyRecord.getIpAddress();
            // Create packet
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetLength, destinationAddress, Constants.UDP_PORT);
            // Transmit the packet
            SendLayer1Frame frameSender = new SendLayer1Frame();
            frameSender.execute(sendPacket);
        } catch (LabException e) {
            Log.i(Constants.LOG_TAG, e.getMessage());
        }
        // Notify observers of change
        setChanged();
        notifyObservers(ll2p);
    }

    /**
     * Called by ReceiveLayer1Frame when UDP packets are received by the router
     * Creates a layer 2 frame from the layer 1 frame byte array
     * @param frame Layer 1 frame data stored in a byte array
     */
    public void processLayer1FrameBytes(byte[] frame) {
        LL2PFrame ll2pFrame = DatagramFactory.getInstance().getItem(Constants.LL2P_FRAME, new String(frame));
        LL2Daemon.getInstance().processLL2PFrame(ll2pFrame);
        //Notify observers
        setChanged();
        notifyObservers(ll2pFrame);
    }

}
