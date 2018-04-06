package com.morganwalkup.support;

import java.net.InetAddress;
import java.util.List;
import java.util.Observable;
import android.app.Activity;
import android.util.Log;

import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.daemons.ARPDaemon;
import com.morganwalkup.networks.daemons.LL1Daemon;
import com.morganwalkup.networks.daemons.LL2Daemon;
import com.morganwalkup.networks.daemons.LL3Daemon;
import com.morganwalkup.networks.daemons.LRPDaemon;
import com.morganwalkup.networks.daemons.Scheduler;
import com.morganwalkup.networks.datagram.LL2PFrame;
import com.morganwalkup.networks.datagramFields.CRC;
import com.morganwalkup.networks.datagramFields.DatagramPayloadField;
import com.morganwalkup.networks.datagramFields.LL2PAddressField;
import com.morganwalkup.networks.datagramFields.LL2PTypeField;
import com.morganwalkup.networks.table.RoutingTable;
import com.morganwalkup.networks.table.Table;
import com.morganwalkup.networks.tablerecord.AdjacencyRecord;
import com.morganwalkup.networks.tablerecord.RoutingRecord;
import com.morganwalkup.networks.tablerecord.TableRecord;

/**
 * Responsible for booting the router
 * Creates classes and declares them as observers
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/18
 */
public class Bootloader extends Observable {

    /**
     * Constructor for the BootLoader class
     * @param parentActivity - The main activity of the router application
     */
    public Bootloader(Activity parentActivity) {
        bootRouter(parentActivity);
    }

    /**
     * Boots the router by instantiating high-level router classes
     * @param parentActivity - The main activity of the router application
     */
    private void bootRouter(Activity parentActivity) {
        // Initialize ParentActivity
        ParentActivity.setParentActivity(parentActivity);
        // Add all observers
        addObserver(UIManager.getInstance());
        addObserver(UIManager.getInstance().getTableUI());
        addObserver(UIManager.getInstance().getSnifferUI());
        addObserver(FrameLogger.getInstance());
        //FrameLogger.getInstance().addObserver(UIManager.getInstance().getSnifferUI());
        addObserver(LL1Daemon.getInstance());
        addObserver(LL2Daemon.getInstance());
        addObserver(LL3Daemon.getInstance());
        addObserver(ARPDaemon.getInstance());
        addObserver(LRPDaemon.getInstance());
        addObserver(Scheduler.getInstance());
        // Finish setup work
        // Notify observers that we're ready to go
        setChanged(); // this lets Java know something has changed!
        notifyObservers(); // this calls the update(…) method in observers

        test();
    }

    /**
     * Runs basic tests to verify operation of the router
     */
    private void test() {
        //UIManager.getInstance().displayMessage(Constants.ROUTER_NAME + " is up and Running!");
        //UIManager.getInstance().displayMessage("IP Address is " + Constants.IP_ADDRESS);

        /*
        // Create LL2P Frame and display protocol explanation string for testing
        LL2PAddressField destinationAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_DESTINATION_ADDRESS, Constants.TEST_DESTINATION_ADDRESS);
        LL2PAddressField sourceAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_SOURCE_ADDRESS, Constants.MY_SOURCE_ADDRESS);
        LL2PTypeField typeField = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_TYPE, Constants.LL2P_TYPE_IS_TEXT);
        DatagramPayloadField payload = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_PAYLOAD, Constants.TEST_PAYLOAD);
        CRC crc = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_CRC, Constants.TEST_CRC_CODE);
        LL2PFrame ll2pFrame = new LL2PFrame(destinationAddress, sourceAddress, typeField, payload, crc);
        UIManager.getInstance().displayMessage(ll2pFrame.toProtocolExplanationString());

        // Create TableRecord and display the record string for testing
        String ipAndLL2PAddress = Constants.IP_ADDRESS + Constants.TEST_DESTINATION_ADDRESS;
        AdjacencyRecord adjacencyRecord = TableRecordFactory.getInstance().getItem(Constants.ADJACENCY_RECORD, ipAndLL2PAddress);
        UIManager.getInstance().displayMessage(adjacencyRecord.toString());
        */

        /*
        // 1. Create adjacency records with various values
        String ipAndLL2PAddress1 = Constants.IP_ADDRESS + Constants.TEST_DESTINATION_ADDRESS;
        AdjacencyRecord adjacencyRecord1 = TableRecordFactory.getInstance().getItem(Constants.ADJACENCY_RECORD, ipAndLL2PAddress1);
        String ipAndLL2PAddress2 = Constants.IP_ADDRESS + "BADCAF";
        AdjacencyRecord adjacencyRecord2 = TableRecordFactory.getInstance().getItem(Constants.ADJACENCY_RECORD, ipAndLL2PAddress2);
        String ipAndLL2PAddress3 = Constants.IP_ADDRESS + "5A17ED";
        AdjacencyRecord adjacencyRecord3 = TableRecordFactory.getInstance().getItem(Constants.ADJACENCY_RECORD, ipAndLL2PAddress3);

        // 2. Create Adjacency Table using Table class. Add and remove records.
        Table adjacencyTable = new Table();
        adjacencyTable.addItem(adjacencyRecord1);
        adjacencyTable.addItem(adjacencyRecord2);
        adjacencyTable.addItem(adjacencyRecord3);
        try {
            AdjacencyRecord record2 = (AdjacencyRecord)adjacencyTable.getItem(adjacencyRecord2);
            adjacencyTable.removeItem(record2.getKey());
            adjacencyTable.addItem(record2);
        } catch (LabException e) {
            Log.i(Constants.LOG_TAG, e.getMessage());
        }

        // 3. Test LL1Daemon
        // 3.1 Test the LL1Daemon by repeating table tests
        LL1Daemon ll1Daemon = LL1Daemon.getInstance();
        ll1Daemon.addAdjacency(Constants.IP_ADDRESS, Constants.TEST_DESTINATION_ADDRESS);
        ll1Daemon.addAdjacency(Constants.IP_ADDRESS, "BADCAF");
        ll1Daemon.addAdjacency("10.30.85.32", "112233");
        Table ll1AdjacencyTable = ll1Daemon.getAdjacencyTable();
        ll1Daemon.removeAdjacencyRecord(adjacencyRecord2);
        ll1Daemon.getAdjacencyTable().addItem(adjacencyRecord2);
        // 3.2 Add lab mirror as adjacent node to adjacency table
        // 3.2.a Create LL2PFrame with mirror's address and ask LL1Daemon to send it to mirror
        LL2PAddressField destinationAddress1 = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_DESTINATION_ADDRESS, "112233");
        LL2PAddressField sourceAddress1 = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_SOURCE_ADDRESS, Constants.MY_SOURCE_ADDRESS);
        LL2PTypeField typeField1 = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_TYPE, Constants.LL2P_TYPE_IS_TEXT);
        DatagramPayloadField payload1 = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_PAYLOAD, Constants.TEST_PAYLOAD);
        CRC crc1 = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_CRC, Constants.TEST_CRC_CODE);
        LL2PFrame ll2pFrame1 = new LL2PFrame(destinationAddress1, sourceAddress1, typeField1, payload1, crc1);
        LL1Daemon.getInstance().sendFrame(ll2pFrame1);
        // 3.2.b Send LL2PFrame to your router. Have LL1Daemon write it to log file or display it on screen
        // This is handled by the LL1Daemon's processLayer1FrameBytes method
        */

        // 4. Test ARPDaemon
        //ARPDaemon.getInstance().testARP();

        /*
        // 5. Test Routing/Forwarding Tables
        RoutingTable routingTable = LRPDaemon.getInstance().getRouteTable();
        RoutingTable forwardingTable = LRPDaemon.getInstance().getForwardingTable();
        // 5.1 Add routes
        Integer networkNumber = 2;
        Integer distance = 2;
        Integer nextHop = Integer.parseInt("0501", Constants.HEX_BASE);
        RoutingRecord route = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(route);
        // 5.2 Expire routes
        routingTable.expireRecords(Constants.ROUTING_RECORD_MAX_AGE);
        // 5.3 Update routes
        networkNumber = 3;
        distance = 3;
        nextHop = Integer.parseInt("0501", Constants.HEX_BASE);
        RoutingRecord firstRoute = new RoutingRecord(networkNumber, distance, nextHop);
        distance = 4;
        RoutingRecord updatedRoute = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(firstRoute);
        routingTable.addNewRoute(updatedRoute);
        // 5.4 Get Best Routes
        networkNumber = 4;
        distance = 3;
        nextHop = Integer.parseInt("0601", Constants.HEX_BASE);
        RoutingRecord record1 = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(record1);
        networkNumber = 4;
        distance = 1;
        nextHop = Integer.parseInt("0701", Constants.HEX_BASE);
        RoutingRecord record2 = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(record2);
        networkNumber = 5;
        distance = 1;
        nextHop = Integer.parseInt("0601", Constants.HEX_BASE);
        RoutingRecord record3 = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(record3);
        networkNumber = 5;
        distance = 7;
        nextHop = Integer.parseInt("0701", Constants.HEX_BASE);
        RoutingRecord record4 = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(record4);
        List<RoutingRecord> betterRecords = routingTable.getBestRoutes();
        // 5.5 Reset Age
        int record4Age = 0;
        int record4Key = record4.getKey();
        try {
            record4Age = routingTable.getItem(record4Key).getAgeInSeconds();
        } catch(LabException e) {
            Log.i(Constants.LOG_TAG, e.getMessage());
        }
        RoutingRecord record4Update = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(record4Update);
        try {
            record4Age = routingTable.getItem(record4Key).getAgeInSeconds();
        } catch(LabException e) {
            Log.i(Constants.LOG_TAG, e.getMessage());
        }
        // 5.6 Remove All Routes from one Source
        networkNumber = 4;
        distance = 1;
        nextHop = Integer.parseInt("B000", Constants.HEX_BASE);
        RoutingRecord badRecord1 = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(badRecord1);
        networkNumber = 5;
        distance = 1;
        nextHop = Integer.parseInt("B000", Constants.HEX_BASE);
        RoutingRecord badRecord2 = new RoutingRecord(networkNumber, distance, nextHop);
        routingTable.addNewRoute(badRecord2);
        routingTable.removeRoutesFrom(nextHop);
        // 5.7 Create Forwarding Table
        Integer betterHop = Integer.parseInt("BBBB", Constants.HEX_BASE);
        networkNumber = 10;
        distance = 2;
        RoutingRecord betterRecord1 = new RoutingRecord(networkNumber, distance, betterHop);
        routingTable.addNewRoute(betterRecord1);
        networkNumber = 11;
        distance = 2;
        RoutingRecord betterRecord2 = new RoutingRecord(networkNumber, distance, betterHop);
        routingTable.addNewRoute(betterRecord2);
        networkNumber = 12;
        distance = 2;
        RoutingRecord betterRecord3 = new RoutingRecord(networkNumber, distance, betterHop);
        routingTable.addNewRoute(betterRecord3);

        Integer worstHop = Integer.parseInt("FFFF", Constants.HEX_BASE);
        networkNumber = 10;
        distance = 3;
        RoutingRecord worstRecord1 = new RoutingRecord(networkNumber, distance, worstHop);
        routingTable.addNewRoute(worstRecord1);
        networkNumber = 11;
        distance = 3;
        RoutingRecord worstRecord2 = new RoutingRecord(networkNumber, distance, worstHop);
        routingTable.addNewRoute(worstRecord2);
        networkNumber = 12;
        distance = 3;
        RoutingRecord worstRecord3 = new RoutingRecord(networkNumber, distance, worstHop);
        routingTable.addNewRoute(worstRecord3);

        List<RoutingRecord> betterRoutes = routingTable.getBestRoutes();
        forwardingTable.addRoutes(betterRoutes);
        // 5.8 Update routing table, then update forwarding table
        Integer bestHop = Integer.parseInt("AAAA", Constants.HEX_BASE);
        networkNumber = 10;
        distance = 1;
        RoutingRecord bestRecord1 = new RoutingRecord(networkNumber, distance, bestHop);
        routingTable.addNewRoute(bestRecord1);
        networkNumber = 11;
        distance = 1;
        RoutingRecord bestRecord2 = new RoutingRecord(networkNumber, distance, bestHop);
        routingTable.addNewRoute(bestRecord2);
        networkNumber = 12;
        distance = 1;
        RoutingRecord bestRecord3 = new RoutingRecord(networkNumber, distance, bestHop);
        routingTable.addNewRoute(bestRecord3);

        List<RoutingRecord> bestRoutes = routingTable.getBestRoutes();
        forwardingTable.clear();
        forwardingTable.addRoutes(bestRoutes);
        */

        // Lab 11: Testing LL3P
        //LL1Daemon.getInstance().addAdjacency("10.30.85.32", "111111");
        //LL1Daemon.getInstance().addAdjacency("10.30.85.32", "222222");
    }
}
