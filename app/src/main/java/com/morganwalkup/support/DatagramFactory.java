package com.morganwalkup.support;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagram.ARPDatagram;
import com.morganwalkup.networks.datagram.Datagram;
import com.morganwalkup.networks.datagram.LL2PFrame;
import com.morganwalkup.networks.datagram.LL3PDatagram;
import com.morganwalkup.networks.datagram.LRPPacket;
import com.morganwalkup.networks.datagram.TextDatagram;

/**
 * Factory generating Datagrams
 * Created by morganwalkup on 1/26/18.
 */
public class DatagramFactory implements Factory<Datagram, String> {

    /** The one and only instance of the DatagramFactory */
    private static final DatagramFactory ourInstance = new DatagramFactory();

    public static DatagramFactory getInstance() {
        return ourInstance;
    }

    /**
     * Empty constructor
     */
    private DatagramFactory() {
    }

    /**
     * Generates an instance of the class type specified, using the given data
     * @param type - The type of the object to create
     * @param inputData - The data used to create the object
     * @param <U> - The type of the created object (must extend Datagram)
     * @return The newly created object
     */
    public <U extends Datagram> U getItem(int type, String inputData) {
        switch(type) {
            case Constants.TEXT_DATAGRAM:
                return (U) new TextDatagram(inputData);
            case Constants.LL2P_FRAME:
                return (U) new LL2PFrame(inputData);
            case Constants.ARP_DATAGRAM:
                return (U) new ARPDatagram(inputData);
            case Constants.LRP_PACKET:
                return (U) new LRPPacket(inputData);
            case Constants.LL3P_DATAGRAM:
                return (U) new LL3PDatagram(inputData);
            default:
                return null;
        }
    }

}
