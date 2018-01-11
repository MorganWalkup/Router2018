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
        //String address= null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = intf.getInetAddresses(); enumIpAddress.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
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

