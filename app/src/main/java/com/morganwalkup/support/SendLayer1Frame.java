package com.morganwalkup.support;

import android.os.AsyncTask;
import android.util.Log;
import com.morganwalkup.networks.Constants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by morganwalkup on 2/1/18.
 * Creates asynchronous task to send a layer 1 frame
 */
public class SendLayer1Frame extends AsyncTask<DatagramPacket, Void, Void> {
    private static DatagramSocket sendSocket;

    @Override
    protected Void doInBackground(DatagramPacket... arg0) {
        // open a socket if it's not been opened before.
        if (sendSocket == null){
            try {
                sendSocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        DatagramPacket packet = arg0[0];
        try {
            sendSocket.send(packet);
            Log.i(Constants.LOG_TAG, ">>>>>>>>>Sent frame: " + new String(packet.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
