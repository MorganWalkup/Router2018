package com.morganwalkup.UI;

import android.app.Activity;
import android.content.Context;

import com.morganwalkup.networks.daemons.ARPDaemon;
import com.morganwalkup.networks.daemons.LL1Daemon;
import com.morganwalkup.router2018.R;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.ParentActivity;

import java.util.Observable;
import java.util.Observer;
import java.lang.Runnable;

/**
 * Created by morganwalkup on 2/8/18.
 * Holds references for all other Table UI objects, each of which manages a table
 */

public class TableUI implements Runnable, Observer {

    /** UI for the adjacency table */
    private SingleTableUI adjacencyUI;
    /** UI for the ARP table */
    private SingleTableUI arpTableUI;
    /** UI for the routing table */
    private SingleTableUI routingTableUI;
    /** UI for the forwarding table */
    private SingleTableUI forwardingUI;

    /**
     * Default constructor
     */
    public TableUI() {

    }

    /**
     * Updates when the observable object is changed
     * @param observable - object observed by the TableUI
     * @param o - object payload passed by the observable object
     */
    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof Bootloader) {
            Activity activity = ParentActivity.getParentActivity();
            Context baseContext = activity.getBaseContext();
            adjacencyUI = new AdjacencyTableUI(
                    activity,
                    R.id.adjacency_list,
                    LL1Daemon.getInstance().getAdjacencyTable(),
                    LL1Daemon.getInstance()
                    );
            arpTableUI = new SingleTableUI(activity,
                    R.id.arp_list,
                    ARPDaemon.getInstance().getARPTable());
            //TODO: In later labs, create other table ui's
        }
    }

    /**
     * Runs every second to keep displays current
     */
    @Override
    public void run() {
        //TODO update tables when run is called
        arpTableUI.updateView();
    }
}
