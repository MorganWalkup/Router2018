package com.morganwalkup.support;

import android.util.Log;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.daemons.LL1Daemon;
import com.morganwalkup.networks.datagram.LL2PFrame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by morganwalkup on 2/1/18.
 * Keeps a list of LL2P frames and notifies the sniffer UI when that list changes
 */

public class FrameLogger extends Observable implements Observer {

    /** The one and only instance of this class */
    private static final FrameLogger ourInstance = new FrameLogger();
    public static FrameLogger getInstance() {
        return ourInstance;
    }

    /** A list of LL2P frames observed by the FrameLogger */
    private ArrayList<LL2PFrame> frameList;
    public ArrayList<LL2PFrame> getFrameList() { return frameList; }

    /** Initializes an empty frameList */
    private FrameLogger() {
        frameList = new ArrayList<LL2PFrame>();
    }

    /**
     * Receives updates from classes observed by the frame logger
     * @param observable
     * @param o
     */
    public void update(Observable observable, Object o) {
        // Initialize FrameLogger on update from Bootloader
        if(observable instanceof Bootloader) {
            //TODO Lab 5: implement Sniffer UI
            //addObserver(SnifferUI.getInstance());
        }
        else if (observable instanceof LL1Daemon)
        {
            if(o instanceof LL2PFrame) {
                frameList.add((LL2PFrame) o); // recast the object and add it.
                Log.i(Constants.LOG_TAG, ((LL2PFrame) o).toProtocolExplanationString());
                setChanged();
                notifyObservers(); // notify the snifferUI there is a new frame.
            }
        }
    }
}
