package com.morganwalkup.UI;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morganwalkup.networks.table.Table;
import com.morganwalkup.networks.table.TableInterface;
import com.morganwalkup.networks.tablerecord.TableRecord;
import com.morganwalkup.router2018.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by morganwalkup on 2/8/18.
 * Manages the UI for a single table in the router application
 */

public class SingleTableUI implements Observer {

    /** The app parent activity */
    protected Activity parentActivity;
    /** The Table holding table data */
    protected Table tableToDisplay;
    /** A list of table records that will be displayed on screen */
    protected List<TableRecord> tableRecordList;
    /** Refers to the screen ListView widget */
    protected ListView tableListViewWidget;
    /** Adapter converting TableInterface data into the ListView-compatible data */
    private ArrayAdapter arrayAdapter;

    /**
     * Constructor requiring activity, view, and tableInterface
     * @param parentActivity - Activity holding the app parent activity
     * @param view - View for displaying the table
     * @param tableInterface - TableInterface holding the table data
     */
    public SingleTableUI(Activity parentActivity, int view, TableInterface tableInterface) {
        this.parentActivity = parentActivity;
        tableToDisplay = (Table)tableInterface;
        arrayAdapter = new ArrayAdapter(parentActivity.getBaseContext(),
                R.layout.table_list_item, tableInterface.getTableAsList());
        //ArrayList arrayList = new ArrayList(arrayAdapter);
        //Connect list view to adapter
        tableListViewWidget = (ListView) parentActivity.findViewById(view);
        tableListViewWidget.setAdapter(arrayAdapter);
        //Observe changes in table object
        tableToDisplay.addObserver(this);
    }

    /**
     * Updates SingleTableUI view on the UI thread
     */
    public void updateView(){
        // Force all our work here to be on the UI thread!
        parentActivity.runOnUiThread(new Runnable() {
            @Override // this is a mini-Runnable class’s run method!
            public void run() {
            // notify the OS that the dataset has changed. It will update screen!
            arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Called when the observable's data changes
     * @param observable - Object observed by SingleTableUI
     * @param o - Payload object delivered by observable
     */
    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof Table) {
            updateView();
        }
    }
}
