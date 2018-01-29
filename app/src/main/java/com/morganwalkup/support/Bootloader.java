package com.morganwalkup.support;

import java.net.InetAddress;
import java.util.Observable;
import android.app.Activity;
import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.LL2PFrame;
import com.morganwalkup.networks.datagramFields.CRC;
import com.morganwalkup.networks.datagramFields.DatagramPayloadField;
import com.morganwalkup.networks.datagramFields.LL2PAddressField;
import com.morganwalkup.networks.datagramFields.LL2PTypeField;
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
        UIManager.getInstance().displayMessage(Constants.ROUTER_NAME + " is up and Running!");
        UIManager.getInstance().displayMessage("IP Address is " + Constants.IP_ADDRESS);

        // Create LL2P Frame and display protocol explanation string for testing
        LL2PAddressField destinationAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_DESTINATION_ADDRESS, Constants.TEST_DESTINATION_ADDRESS);
        LL2PAddressField sourceAddress = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_SOURCE_ADDRESS, Constants.MY_SOURCE_ADDRESS);
        LL2PTypeField typeField = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_TYPE, Constants.LL2P_TYPE_IS_TEXT);
        DatagramPayloadField payload = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_PAYLOAD, Constants.TEST_PAYLOAD);
        CRC crc = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_CRC, Constants.TEST_CRC_CODE);
        LL2PFrame ll2pFrame = new LL2PFrame(destinationAddress, sourceAddress, typeField, payload, crc);
        UIManager.getInstance().displayMessage(ll2pFrame.toProtocolExplanationString());

        // Create TableRecord and display the record string for testing
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(Constants.IP_ADDRESS);
        } catch(Exception e) {
            return;
        }
        Integer ll2paddress = Integer.parseInt(Constants.TEST_DESTINATION_ADDRESS, Constants.HEX_BASE);
        TableRecord tableRecord = TableRecordFactory.getInstance().getItem(Constants.ADJACENCY_RECORD, ipAddress, ll2paddress);
        UIManager.getInstance().displayMessage(tableRecord.toString());
    }
}
