package com.morganwalkup.UI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.daemons.LL1Daemon;
import com.morganwalkup.networks.daemons.LL2Daemon;
import com.morganwalkup.networks.datagram.LL2PFrame;
import com.morganwalkup.networks.table.TableInterface;
import com.morganwalkup.networks.tablerecord.AdjacencyRecord;
import com.morganwalkup.support.DatagramFactory;

/**
 * Created by morganwalkup on 2/8/18.
 * Displays and handles the Adjacency Table displayed in the UI
 */

public class AdjacencyTableUI extends SingleTableUI {

    /** A reference to the LL1Daemon */
    private LL1Daemon ll1Daemon;

    /**
     * Constructor
     * @param activity - Main activity of the app
     * @param view - View rendering the Adjacency Table UI
     * @param tableInterface - The table data
     * @param ll1Daemon - The table manager (LL1Daemon in this case)
     */
    AdjacencyTableUI(Activity activity, int view, TableInterface tableInterface, LL1Daemon ll1Daemon) {
        super(activity, view, tableInterface);
        this.ll1Daemon = ll1Daemon;
        // Add click listeners to view
        this.tableListViewWidget.setOnItemClickListener(sendEchoRequest());
        this.tableListViewWidget.setOnItemLongClickListener(removeAdjacency());
    }

    /**
     * Creates a listener that sends an echo request to a clicked adjacency record
     * @return An OnItemClickListener that sends an echo request
     */
    private AdapterView.OnItemClickListener sendEchoRequest() {
        return new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                // Get data from clicked record
                AdjacencyRecord clickedRecord = (AdjacencyRecord)tableListViewWidget.getItemAtPosition(position);
                Integer destinationAddress = clickedRecord.getLL2PAddress();
                // Transmit Dummy LL2P Frame
                LL2Daemon.getInstance().sendEchoRequest(destinationAddress);
            }
        };
    }

    /**
     * Creates a listener that removes an adjacency record from the table on long click
     * @return An OnItemLongClickListener that remove the record from the adjacency table
     */
    private AdapterView.OnItemLongClickListener removeAdjacency() {
        return new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                // Remove clicked record from adjacency table
                AdjacencyRecord clickedRecord = (AdjacencyRecord)tableListViewWidget.getItemAtPosition(position);
                ll1Daemon.removeAdjacencyRecord(clickedRecord);
                return true;
            }
        };
    }

}
