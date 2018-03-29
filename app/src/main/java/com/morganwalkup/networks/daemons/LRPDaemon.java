package com.morganwalkup.networks.daemons;

import android.net.Network;
import android.net.NetworkRequest;
import android.util.Log;

import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.LRPPacket;
import com.morganwalkup.networks.datagramFields.LL3PAddressField;
import com.morganwalkup.networks.datagramFields.NetworkDistancePair;
import com.morganwalkup.networks.table.RoutingTable;
import com.morganwalkup.networks.table.Table;
import com.morganwalkup.networks.table.TimedTable;
import com.morganwalkup.networks.tablerecord.ARPRecord;
import com.morganwalkup.networks.tablerecord.RoutingRecord;
import com.morganwalkup.networks.tablerecord.TableRecord;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.DatagramFactory;
import com.morganwalkup.support.LabException;
import com.morganwalkup.support.ParentActivity;
import com.morganwalkup.support.TableRecordFactory;
import com.morganwalkup.support.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Daemon responsible for all LRP functions
 * Created by morganwalkup on 3/12/18.
 */

public class LRPDaemon implements Observer, Runnable {

    /** The one and only instance of this class */
    private static final LRPDaemon ourInstance = new LRPDaemon();
    public static LRPDaemon getInstance() { return ourInstance; }

    /** Reference for the ARP Daemon */
    public ARPDaemon arpDaemon;
    /** This is the table that contains all known routes */
    private RoutingTable routeTable;
    public RoutingTable getRouteTable() { return this.routeTable; }
    public Table getRouteTableAsTable() { return (Table)this.routeTable; }
    /** This is the table containing best routes only */
    private RoutingTable forwardingTable;
    public RoutingTable getForwardingTable() { return this.forwardingTable; }
    public Table getForwardingTableAsTable() { return (Table)this.forwardingTable; }
    /** Reference to the layer 2 daemon */
    private LL2Daemon layer2Daemon;
    /** Each time a new LRP is transmitted, this number increases up to 15 then resets */
    private int sequenceNumber;

    /**
     * Default constructor
     */
    public LRPDaemon() {
        this.sequenceNumber = 0;
    }

    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof Bootloader) {
            this.arpDaemon = ARPDaemon.getInstance();
            this.layer2Daemon = LL2Daemon.getInstance();
            this.routeTable = new RoutingTable();
            this.forwardingTable = new RoutingTable();
        }
        else if (observable instanceof ARPDaemon) {
            //this.routeTable.removeRoutesFrom(); //Remove routes based on ARP update
        }
    }

    @Override
    public void run() {
        ParentActivity.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateRoutes();
            }
        });
    }

    /**
     * Updates routes in the routing and forwarding tables
     */
    private void updateRoutes() {
        //1. Expire all routes in both tables
        this.routeTable.expireRecords(Constants.ROUTING_RECORD_MAX_AGE);
        this.forwardingTable.expireRecords(Constants.ROUTING_RECORD_MAX_AGE);

        //2. Add a route to the routing table (or reset its timer) for this router’s network, at a distance of zero.
        String routingRecordData = Constants.MY_LL3P_NETWORK_STRING + Constants.MY_LL3P_SOURCE_ADDRESS + 0;
        RoutingRecord myRecord = TableRecordFactory.getInstance().getItem(Constants.ROUTING_RECORD, routingRecordData);
        this.routeTable.addNewRoute(myRecord);

        //3. Add a route to the routing table for each adjacent router’s network, at a distance of one. This information is gained from the ARP table and your router is considered the source for this information. The neighbor is, of course, the next hop.
        List<Integer> neighborAddresses = arpDaemon.getAttachedNodes();
        for (int i = 0; i < neighborAddresses.size(); i++) {
            Integer ll3pAddress = neighborAddresses.get(i);
            String ll3pAddressString = Utilities.padHexString(Integer.toHexString(ll3pAddress), Constants.LL3P_ADDR_FIELD_LENGTH);
            String ll3pNetworkString = ll3pAddressString.substring(0, Constants.LL3P_SRC_ADDR_FIELD_LENGTH * 2);

            String neighborRecordData = ll3pNetworkString + ll3pAddressString + 1;
            RoutingRecord neighborRecord = TableRecordFactory.getInstance().getItem(Constants.ROUTING_RECORD, neighborRecordData);
            this.routeTable.addNewRoute(neighborRecord);
        }

        //4. Get the list of best routes from the routing table and hand it to the forwarding table. The forwarding table will use this list to replace, update, or add routes.
        List<RoutingRecord> bestRoutes = this.routeTable.getBestRoutes();
        this.forwardingTable.clear();
        this.forwardingTable.addRoutes(bestRoutes);

        //5. Using the list of adjacent nodes from step 3 above, send a routing update to every known neighbor. You will exclude routes learned from that router in this update in order to avoid the count-to-infinity problem that exists with Distance-Vector routing protocols.
        for (int i = 0; i < neighborAddresses.size(); i++) {
            // Get neighbor's ll3p network number
            Integer ll3pAddress = neighborAddresses.get(i);
            String ll3pAddressString = Integer.toHexString(ll3pAddress);
            String ll3pNetworkString = ll3pAddressString.substring(0, Constants.LL3P_SRC_ADDR_FIELD_LENGTH * 2);
            Integer ll3pNetworkNumber = Integer.parseInt(ll3pNetworkString, Constants.HEX_BASE);
            // Send routing update excluding routes from neighbor
            List<RoutingRecord> recordsToSend = this.forwardingTable.getRouteListExcluding(ll3pNetworkNumber);
            String networkDistancePairs = "";
            for (int j = 0; j < recordsToSend.size(); j++) {
                RoutingRecord recordToSend = recordsToSend.get(j);
                networkDistancePairs += recordToSend.getNetworkDistancePair().toTransmissionString();
            }
            String routingUpdateData = Constants.MY_LL3P_SOURCE_ADDRESS
                    + Integer.toHexString(this.sequenceNumber)
                    + Integer.toHexString(recordsToSend.size())
                    + networkDistancePairs;
            LRPPacket routingUpdate = DatagramFactory.getInstance().getItem(Constants.LRP_PACKET, routingUpdateData);
            sendUpdate(routingUpdate, ll3pAddress);
        }

        //6. Increment sequence number
        this.sequenceNumber = (this.sequenceNumber + 1) % 16;
    }

    /**
     * Returns the routing table of this LRP Daemon as a List
     * @return the routing table of this LRP Daemon as a List
     */
    public List<TableRecord> getRoutingTableAsList() {
        return this.routeTable.getTableAsList();
    }

    /**
     * Returns the forwarding table of this LRP Daemon as a List
     * @return the forwarding table of this LRP Daemon as a List
     */
    public List<TableRecord> getForwardingTableAsList() {
        return this.forwardingTable.getTableAsList();
    }

    /**
     * Called by the LL2Daemon when a LRP packet is received
     * @param lrpPacket - The data of the lrp packet
     * @param ll2pSource - Source address of the LRP packet
     */
    public void receiveNewLRP(byte[] lrpPacket, Integer ll2pSource) {
        // TODO: Touch the ARP Entry that contains the LL3P address that sent us this LRP packet
        // Process lrpPacket
        processLRP(lrpPacket);
    }

    /**
     * Called by the LL2Daemon when a LRP packet is received
     * @param lrpPacket - The new lrp packet
     * @param ll2pSource - LL2P source address of the LRP packet
     */
    public void receiveNewLRP(LRPPacket lrpPacket, Integer ll2pSource) {
        // Touch the ARP Entry that contains the LL3P address that sent us this LRP packet
        arpDaemon.getARPTable().touch(lrpPacket.getLL3PAddressField().getAddress());
        // Process lrpPacket
        processLRPPacket(lrpPacket);
    }

    /**
     * Processes received LRP packet data
     * @param lrp - The data of the lrp packet
     */
    public void processLRP(byte[] lrp) {
        String lrpString = new String(lrp);
        LRPPacket lrpPacket = DatagramFactory.getInstance().getItem(Constants.LRP_PACKET, lrpString);
        processLRPPacket(lrpPacket);
    }

    /**
     * Processes a received LRP packet, typically called by LL2Daemon
     * @param lrp - The lrp packet to process
     */
    public void processLRPPacket(LRPPacket lrp) {
        // Update routing table
        List<NetworkDistancePair> networkDistancePairs = lrp.getRoutes();
        List<RoutingRecord> newRoutingRecords = new ArrayList<>();
        for(int i = 0; i < networkDistancePairs.size(); i++) {
            NetworkDistancePair ndPair = networkDistancePairs.get(i);
            ndPair.incrementDistance();
            RoutingRecord record = new RoutingRecord(
                    ndPair.getNetwork(),
                    ndPair.getDistance(),
                    lrp.getLL3PAddressField().getNetworkNumber()
            );
            newRoutingRecords.add(record);
        }
        this.routeTable.addRoutes(newRoutingRecords);
        // Update forwarding table
        List<RoutingRecord> bestRoutes = this.routeTable.getBestRoutes();
        this.forwardingTable.clear();
        this.forwardingTable.addRoutes(bestRoutes);
    }

    /**
     * Gets LL2P address of neighbor and use LL2Daemon to send LRP packet
     * @param packet - The LRP packet to send
     * @param ll3pAddress - Integer of this router's LL3P address
     */
    private void sendUpdate(LRPPacket packet, Integer ll3pAddress) {
        try {
            // Get LL2P address of neighbor from the ARP daemon
            Integer ll2pAddress = arpDaemon.getMACAddress(ll3pAddress);
            // Use LL2Daemon to send LRP packet
            layer2Daemon.sendLRPUpdate(packet, ll2pAddress);
        } catch(LabException e) {
            Log.i(Constants.LOG_TAG, e.getMessage());
        }
    }


}
