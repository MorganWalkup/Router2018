package com.morganwalkup.networks.daemons;

import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.LL3PDatagram;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.DatagramFactory;
import com.morganwalkup.support.LabException;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by morganwalkup on 4/3/18.
 */

public class LL3Daemon implements Observer {

    /** The one and only instance of this class */
    private static final LL3Daemon ourInstance = new LL3Daemon();
    public static LL3Daemon getInstance() {
        return ourInstance;
    }
    /** Reference to the ARPDaemon instance */
    private ARPDaemon arpDaemon;
    /** Reference to the LL2Daemon instance. LL3 Packets are sent to LL2Daemon for framing and transmission */
    private LL2Daemon ll2Daemon;
    /** Reference to the LRPDaemon instance to get next hop information when forwarding LL3 packets*/
    private LRPDaemon lrpDaemon;

    /** Empty constructor */
    private LL3Daemon() {}

    /**
     * Updates this object when a status change is received from observable
     * @param observable - The object being observed
     * @param o - The object containing observable's changes
     */
    @Override
    public void update(Observable observable, Object o)
    {
        if(observable instanceof Bootloader) {
            arpDaemon = ARPDaemon.getInstance();
            ll2Daemon = LL2Daemon.getInstance();
            lrpDaemon = LRPDaemon.getInstance();
        }
    }

    /**
     * Creates an LL3Datagram using the message and destination address
     * Then uses sendLL3PToNextHop(LL3Datagram) to transmit to the appropriate neighbor
     * @param message
     * @param ll3DestinationAddress
     */
    public void sendPayload(String message, Integer ll3DestinationAddress) {
        // Create LL3P Datagram
        String ll3pDestinationString = Integer.toString(ll3DestinationAddress, Constants.HEX_BASE);
        String ll3pDataString = Constants.MY_LL3P_SOURCE_ADDRESS +
                ll3pDestinationString +
                Constants.LL2P_TYPE_IS_TEXT +
                Constants.TEST_LL3P_IDENTIFIER +
                Constants.TEST_LL3P_TTL +
                message +
                Constants.TEST_LL3P_CHECKSUM;
        LL3PDatagram ll3Datagram = DatagramFactory.getInstance().getItem(Constants.LL3P_DATAGRAM, ll3pDataString);
        // Transmit
        sendLL3PToNextHop(ll3Datagram);
    }

    /**
     * Sends the given LL3PDatagram to the next hop
     * @param ll3Datagram - The datagram to send
     */
    public void sendLL3PToNextHop(LL3PDatagram ll3Datagram) {
        // 1. Get ll3DestinationAddress
        Integer ll3DestinationAddress = ll3Datagram.getDestinationAddress().getAddress();
        // 2. Get next hop from LRPDaemon forwarding table
        Integer ll3NextHop = lrpDaemon.getForwardingTable().getNextHop(ll3DestinationAddress);
        // 3. Get next hop's LL2 address from ARPDaemon
        try {
            Integer ll2NextHop = arpDaemon.getMACAddress(ll3NextHop);
            // 4. Ask LL2Daemon to send this frame to next hop
            ll2Daemon.sendLL3PDatagram(ll3Datagram, ll2NextHop);
        } catch(LabException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives a layer 3 packet, updates the ARP daemon, and displays or passes on the packet
     * @param packet
     * @param layer2Address
     */
    public void processLL3PPacket(LL3PDatagram packet, Integer layer2Address) {
        // Touch arp record for layer2Address
        arpDaemon.getARPTable().touch(layer2Address);
        // If payload is addressed to this router, display text on screen
        if(packet.getDestinationAddress().getAddress() == Constants.MY_LL3P_SOURCE_ADDRESS_INT) {
            UIManager.getInstance().displayMessage(packet.getDatagramPayload().toString());
        }
        // If packet is for a different node, decrement TTL and send to next hop
        else {
            packet.decrementTTL();
            sendLL3PToNextHop(packet);
        }
    }


}
