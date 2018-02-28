package com.morganwalkup.networks.table;

import com.morganwalkup.networks.tablerecord.TableRecord;
import com.morganwalkup.networks.tablerecord.TableRecordBase;

import java.util.ArrayList;

/**
 * Table with records expiring past a certain age
 * Used by the ARPDaemon for the ARP Table
 * Created by morganwalkup on 2/22/18.
 */

public class TimedTable extends Table {

    /**
     * Default Constructor
     */
    public TimedTable() {
        super();
    }

    /**
     * Removes all records from the table exceeding the given age
     * @param maxAgeAllowed - Integer maximum record age allowed in seconds
     * @return - An array list of the removed records
     */
    public ArrayList<TableRecord> expireRecords(Integer maxAgeAllowed) {
        ArrayList<TableRecord> removedRecords = new ArrayList<TableRecord>();

        for(TableRecord record : this.table ) {
            if(record.getAgeInSeconds() > maxAgeAllowed) {
                this.table.remove(record);
                removedRecords.add(record);
            }
        }

        return removedRecords;
    }

    /**
     * Finds the record specified by key and updates its age to zero
     * @param key - The key of the record to find
     */
    public void touch(Integer key) {
        TableRecordBase record = (TableRecordBase)this.table.get(key);
        record.updateTime();
    }

}
