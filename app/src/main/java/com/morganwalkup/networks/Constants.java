package com.morganwalkup.networks;

import android.util.Log;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.SocketException;

/**
 * Contains references to constant, global values for the router application
 * such as the router's IP address
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/18
 */
public class Constants {

    // The IP address of this system will be stored here in dotted decimal notation
    public static final String IP_ADDRESS;
    // String containing my router's name
    public static final String ROUTER_NAME = "WalkieTalkup";
    // Tag used to search log files from the debugger
    public static final String LOG_TAG = "WALKIETALKUP: ";
    // Hex code identifying this router
    public static final String MY_SOURCE_ADDRESS = "FA1AF1";
    // Hex code identifying the UDP port used for transmission
    public static final int UDP_PORT = 49999;

    // Constants for test LL2P frame and table records
    public static final String TEST_DESTINATION_ADDRESS = "C0FFEE";
    public static final String TEST_PAYLOAD = "Hello, world!";
    public static final String TEST_CRC_CODE = "7777";

    // Unique integer constant for Text Datagrams
    public static final int TEXT_DATAGRAM = 8;
    // Unique integer constant for LL2P frame
    public static final int LL2P_FRAME = 9;
    // Unique integer constant for LL2P destination address
    public static final int LL2P_DESTINATION_ADDRESS = 10;
    // Unique integer constant for LL2P source address
    public static final int LL2P_SOURCE_ADDRESS = 11;
    // Unique integer constant for LL2P type field
    public static final int LL2P_TYPE = 12;
    // Unique integer constant for LL2P payload field
    public static final int LL2P_PAYLOAD = 13;
    // Unique integer constant for LL2P CRC field
    public static final int LL2P_CRC = 14;
    // Unique integer constant for Adjacency Record
    public static final int ADJACENCY_RECORD = 15;
    // Unique integer constant for LL3P destination address
    public static final int LL3P_DESTINATION_ADDRESS = 16;
    // Unique integer constant for LL3P source address
    public static final int LL3P_SOURCE_ADDRESS = 17;
    // Unique integer constant for ARP Datagram
    public static final int ARP_DATAGRAM = 18;

    // The length of the LL2P frame address field in bytes
    public static final int LL2P_ADDR_FIELD_LENGTH = 3;
    // The offset of the LL2P frame destination address field in bytes
    public static final int LL2P_DEST_FIELD_OFFSET = 0;
    // The offset of the LL2P frame destination address field in bytes
    public static final int LL2P_SRC_FIELD_OFFSET = 3;
    // The length of the LL2P frame type field in bytes
    public static final int LL2P_TYPE_FIELD_LENGTH = 2;
    // The offset of the LL2P frame type field in bytes
    public static final int LL2P_TYPE_FIELD_OFFSET = 6;
    // The offset of the LL2P frame payload field in bytes
    public static final int LL2P_PAYLOAD_FIELD_OFFSET = 8;
    // The length of the LL2P frame CRC field in bytes
    public static final int LL2P_CRC_FIELD_LENGTH = 2;
    // The length of the LL3P address field in bytes
    public static final int LL3P_ADDR_FIELD_LENGTH = 2;
    // The length of the LL3P source address field in bytes
    public static final int LL3P_SRC_ADDR_FIELD_LENGTH = 1;
    // The length of the LL3P destination address field in bytes
    public static final int LL3P_DEST_ADDR_FIELD_LENGTH = 1;

    //LL2P Payload types
    public static final String LL2P_TYPE_IS_LL3P = "8001";
    public static final String LL2P_TYPE_IS_RESERVED = "8002";
    public static final String LL2P_TYPE_IS_LRP = "8003";
    public static final String LL2P_TYPE_IS_ECHO_REQUEST ="8004";
    public static final String LL2P_TYPE_IS_ECHO_REPLY = "8005";
    public static final String LL2P_TYPE_IS_ARP_REQUEST = "8006";
    public static final String LL2P_TYPE_IS_ARP_REPLY = "8007";
    public static final String LL2P_TYPE_IS_TEXT = "8008";

    // The number base for hex numbers
    public static final int HEX_BASE = 16;
    // The maximum length for a summary string
    public static final int SUMMARY_LENGTH = 20;

    static {
        IP_ADDRESS = getLocalIpAddress();
        Log.i(Constants.LOG_TAG, "IP Address is " + IP_ADDRESS); // this will show up in the log file
    }

    /**
     * Goes through the network interfaces,
     * looking for one that has a valid IP address.
     * Care must be taken to avoid a loopback address and IPv6 Addresses.
     * @return - a string containing the IP address in dotted decimal notation.
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}

