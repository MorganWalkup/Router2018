package com.morganwalkup.networks.daemons;

import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.LL2PFrame;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.DatagramFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * The Layer 2 Daemon class handles the processing of layer 2 frames
 * Created by morganwalkup on 2/15/18.
 */

public class LL2Daemon implements Observer {

    /** The one and only instance of this class */
    private static final LL2Daemon ourInstance = new LL2Daemon();
    public static LL2Daemon getInstance() {
        return ourInstance;
    }
    /** Reference to the UI Manager */
    private UIManager uiManager;
    /** Reference to the LL1Daemon */
    private LL1Daemon ll1Daemon;

    /** Empty constructor */
    private LL2Daemon() {}

    /**
     * Called on change in state of observed objects
     * @param observable - The observed object
     * @param object - Object containing changed data
     */
    public void update(Observable observable, Object object) {
        if(observable instanceof Bootloader) {
            ll1Daemon = LL1Daemon.getInstance();
            uiManager = UIManager.getInstance();
        }
        else if(observable instanceof LL1Daemon) {
            if(object instanceof LL2PFrame) {
                processLL2PFrame((LL2PFrame) object);
            }
        }
    }

    /**
     * Processes the frame according to payload type,
     * after verifying the destination address
     * @param frame - The frame received
     */
    public void processLL2PFrame(LL2PFrame frame) {
        if(frame.getDestinationAddress().toTransmissionString().equalsIgnoreCase(Constants.MY_SOURCE_ADDRESS)) {
            //TODO: Lab 7: Check CRC
            if(frame.getType().toTransmissionString().equals(Constants.LL2P_TYPE_IS_ECHO_REQUEST)) {
                answerEchoRequest(frame);
            }
        }
    }

    /**
     * Builds an echo reply frame and requests transmission from the LL1Daemon
      * @param frame
     */
    public void answerEchoRequest(LL2PFrame frame) {
        // Construct LL2P echo reply
        String ll2pString = frame.getSourceAddress().toTransmissionString() +
                Constants.MY_SOURCE_ADDRESS +
                Constants.LL2P_TYPE_IS_ECHO_REPLY +
                frame.getPayload().toTransmissionString() +
                Constants.TEST_CRC_CODE;
        LL2PFrame ll2pFrame = DatagramFactory.getInstance().getItem(Constants.LL2P_FRAME, ll2pString);
        // Transmit LL2P Frame
        ll1Daemon.sendFrame(ll2pFrame);
    }

    /**
     * Sends an LL2P frame echo request to the address provided
     * @param LL2PAddress - Destination address for the LL2P Frame
     */
    public void sendEchoRequest(Integer LL2PAddress) {
        String destinationAddress = Integer.toHexString(LL2PAddress);
        // Construct LL2P echo request
        String ll2pString = destinationAddress +
                Constants.MY_SOURCE_ADDRESS +
                Constants.LL2P_TYPE_IS_ECHO_REQUEST +
                Constants.TEST_PAYLOAD +
                Constants.TEST_CRC_CODE;
        LL2PFrame ll2pFrame = DatagramFactory.getInstance().getItem(Constants.LL2P_FRAME, ll2pString);
        // Transmit LL2P Frame
        ll1Daemon.sendFrame(ll2pFrame);
    }

}
