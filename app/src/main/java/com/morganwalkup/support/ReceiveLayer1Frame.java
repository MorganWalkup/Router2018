package com.morganwalkup.support;

import android.os.AsyncTask;
import android.util.Log;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.daemons.LL1Daemon;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by morganwalkup on 2/1/18.
 * Creates asynchronous task to receive a layer 1 frame
 */

public class ReceiveLayer1Frame extends AsyncTask<Void, Void, byte[]> {

    private static DatagramSocket receiveSocket;

    @Override
    protected byte[] doInBackground(Void... nothingToSeeHere) {

        byte[] receiveData = new byte[1024];
        //   completely hide this function from the layer 1 daemon by placing all socket stuff here.
        if (receiveSocket==null){
            try {
                receiveSocket = new DatagramSocket(Constants.UDP_PORT); // receive port defined above.
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        // create a datagram packet to receive the UPD data.
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        Log.d(Constants.LOG_TAG, "Inside rx unicast Thread");
        try {
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int bytesReceived = receivePacket.getLength ();
        byte[] frameBytes = new String(receivePacket.getData()).substring(0,bytesReceived).getBytes();

        Log.d(Constants.LOG_TAG, "Received bytes: "+ new String(frameBytes));
        return frameBytes;
    }

    @Override
    protected void onPostExecute(byte[] frameBytes) {
        LL1Daemon.getInstance().processLayer1FrameBytes(frameBytes);
        new ReceiveLayer1Frame().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
