package com.morganwalkup.support;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.tablerecord.AdjacencyRecord;
import com.morganwalkup.networks.tablerecord.TableRecord;

import java.net.InetAddress;

/**
 * Factory generating Table Records
 * Created by morganwalkup on 1/25/18.
 */

public class TableRecordFactory implements Factory<TableRecord,String> {

    /** The one and only instance of the TableRecordFactory */
    private static final TableRecordFactory ourInstance = new TableRecordFactory();

    public static TableRecordFactory getInstance() {
        return ourInstance;
    }

    /**
     * Empty constructor
     */
    private TableRecordFactory() {
    }

    /**
     * Generates an instance of the class type specified, using the given data
     * @param type - The type of the object to create
     * @param inputData - The data used to create the object
     * @param <U> - The type of the created object (must extend TableRecord)
     * @return The newly created object
     */
    public <U extends TableRecord> U getItem(int type, String inputData) {
        switch(type) {
            case Constants.ADJACENCY_RECORD:
                return (U) new AdjacencyRecord(inputData);
            default:
                return null;
        }
    }
}
