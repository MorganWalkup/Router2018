package com.morganwalkup.UI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.morganwalkup.networks.datagram.LL2PFrame;
import com.morganwalkup.router2018.R;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.FrameLogger;
import com.morganwalkup.support.ParentActivity;
import com.morganwalkup.support.Utilities;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Observable;
import java.util.Observer;

/**
 * UI display for Frames intercepted by the FrameLogger
 * Created by morganwalkup on 2/15/18.
 */

public class SnifferUI implements Observer {

    /** Main activity of the router */
    private Activity parentActivity;
    /** Context object taken from the activity's getBaseContext(); */
    private Context context;
    /** Refers to the singleton FrameLogger instance */
    private FrameLogger frameLogger;
    /** Local custom extension of ArrayAdapter class for displaying text lines on the SnifferUI */
    private SnifferFrameListAdapter frameListAdapter;
    /** Matches with ListView in SnifferUI relative Layout */
    private ListView frameListView;
    /** Points to middle window in SnifferUI */
    private TextView protocolBreakoutTextView;
    /** Points to lower window in SnifferUI */
    private TextView frameBytesTextView;


    /** Empty constructor */
    public SnifferUI() {};

    /**
     * Handles observer updates from BootLoader and FrameLogger
     * @param observable
     * @param object
     */
    public void update(Observable observable, Object object) {
        if(observable instanceof Bootloader) {
            parentActivity = ParentActivity.getParentActivity();
            context = parentActivity.getBaseContext();
            frameLogger = FrameLogger.getInstance();
            frameLogger.addObserver(this);
            connectWidgets();
        }
        else if(observable instanceof FrameLogger) {
            frameListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Sets up the adapter, ListView, and TextView objects
     * Declares the onClickListener for ListView clicks
     */
    private void connectWidgets() {
        frameListView = (ListView) parentActivity.findViewById(R.id.packets_list);
        frameListAdapter = new SnifferFrameListAdapter(context, frameLogger.getFrameList());
        frameListView.setAdapter(frameListAdapter);
        protocolBreakoutTextView = (TextView) parentActivity.findViewById((R.id.protocols));
        frameBytesTextView = (TextView) parentActivity.findViewById((R.id.hex_packets));
        frameListView.setOnItemClickListener(showThisFrame());
    }

    /**
     * Handles click events on the ListView
     * @return The click listener
     */
    //private ListView.OnClickListener showUpdatedFrameList() {
    //
    //}

    /**
     * Handles click events on ListView items
     * Assigns the selected frame's protocol explanation and hex bytes strings to the lower windows
     * @return The click listener
     */
    private AdapterView.OnItemClickListener showThisFrame() {
        return new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                // Get data from clicked item
                LL2PFrame clickedFrame = (LL2PFrame) frameListView.getItemAtPosition(position);
                protocolBreakoutTextView.setText(clickedFrame.toProtocolExplanationString());
                String hexString = clickedFrame.toHexString();
                hexString = formatHexString(hexString);
                frameBytesTextView.setText(hexString);
            }
        };
    }

    /**
     * Helper method to format a frame's hex string for display
     */
    private String formatHexString(String hexString) {
        StringBuilder formattedHexString = new StringBuilder();

        // Divide hex string into 8 byte rows
        hexString = hexString.replaceAll("................", "$0\n");
        // Split hex bytes into lines
        String hexLines[] = hexString.split("\n");
        for(int i = 0; i < hexLines.length; i++) {
            Formatter formatter = new Formatter();
            formatter.format(
                    "%04d    %-24s    %s%n",
                    i*8,
                    hexLines[i].replaceAll("..", "$0 "),
                    Utilities.convertHexToASCII(hexLines[i])
                );
            formattedHexString.append(formatter);
        }

        return formattedHexString.toString();
    }

    /**
     * This class is a holder. It holds widgets (views) that make
     * up a single row in the sniffer top window.
     */
    private static class ViewHolder {
        TextView packetNumber;
        TextView packetSummaryString;
    }


    /**
     * SnifferFrameListAdapter is a private adapter to display numbered rows from a ListView
     * object which contains all frames transmitted or received.
     * <p>
     * It is instantiated above and note that the constructor passes the context as well as
     * the frameList.
     */
    private class SnifferFrameListAdapter extends ArrayAdapter<LL2PFrame> {
        // this is the ArrayList that will be displayed in the rows on the ListView.
        private ArrayList<LL2PFrame> frameList;

        /**
        *  The constructor is passed the context and the arrayList.
        *  the arrayList is assigned to the local variable so its contents can be
        *  adapted to the listView.
        */
        public SnifferFrameListAdapter(Context context, ArrayList<LL2PFrame> frames) {
            super(context, 0, frames);
            frameList = frames;
        }

        /**
         * Here is where the work is performed to adapt a specific row in the arrayList to
         * a row on the screen.
         *
         * @param position    - position in the array we're working with
         * @param convertView - a row View that passed in – has a view to use or a null object
         * @param parent      - the main view that contains the rows.  Note that is is the ListView object.
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // First retrieve a frame object from the arrayList at the position we're working on
            LL2PFrame ll2PFrame = getItem(position);
            // declare a viewHolder - this simply is a single object to hold a two widgets
            ViewHolder viewHolder;

            /**
             * If convertView is null then we didn't get a recycled View, we have to create from scratch.
             * We do that here.
             */
            if (convertView == null) {
                // inflate the view defined in the layout xml file using an inflater we create here.
                LayoutInflater inflator = LayoutInflater.from(context);
                convertView = inflator.inflate(R.layout.sniffer_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.packetNumber = (TextView) convertView.findViewById(R.id.snifferFrameNumberTextView);
                viewHolder.packetSummaryString = (TextView) convertView.findViewById(R.id.snifferItemTextView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.packetNumber.setText(Integer.toString(position));
            viewHolder.packetSummaryString.setText(frameList.get(position).toSummaryString());
            return convertView;
        }
    }
}
