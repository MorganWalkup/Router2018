package com.morganwalkup.networks.table;

import com.morganwalkup.networks.tablerecord.TableRecord;
import com.morganwalkup.support.LabException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

/**
 * Created by morganwalkup on 2/1/18.
 * Base class for all tables in the router
 */

public class Table extends Observable implements TableInterface {

    /** The list of table records managed by this class */
    protected List<TableRecord> table;

    /**
     * Initializes the table with an empty list of record types
     */
    public Table() {
        table = Collections.synchronizedList(new ArrayList<TableRecord>());
    }

    /**
     * Notifies observers that the underlying data model has changed
     */
    public void updateDisplay() {
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the table as a list of table records
     * @return A list of table records in the table
     */
    public List<TableRecord> getTableAsList() {
        return table;
    }

    /**
     * Adds a table record to the table
     * @return The table record added
     */
    public TableRecord addItem(TableRecord tableRecord) {
        table.add(tableRecord);
        return tableRecord;
    }

    /**
     * Returns a table record matching the tableRecord specified
     * @param tableRecord The table record to find
     * @return The table record found
     * @throws LabException thrown if the tableRecord is not found
     */
    public TableRecord getItem(TableRecord tableRecord) throws LabException {
        //Scan the table for given tableRecord
        for(TableRecord record : table) {
            if(record == tableRecord) {
                return record;
            }
        }
        //Throw exception if tableRecord was not found
        throw new LabException("Table record not found");
    }

    /**
     * Returns a table record with the given key
     * @param key The key of the table record to find
     * @return The table record found
     * @throws LabException thrown if tableRecord is not found
     */
    public TableRecord getItem(Integer key) throws LabException {
        //Scan the table for given tableRecord
        for(TableRecord record : table) {
            if(record.getKey().equals(key)) {
                return record;
            }
        }
        //Throw exception if tableRecord was not found
        throw new LabException("Table record not found");
    }

    /**
     * Removes a table record with the given key
     * @param key The key of the table record to remove
     */
    public void removeItem(Integer key) {
        //Scan the table for given tableRecord
        for(int i = 0; i < table.size(); i++) {
            if (table.get(i).getKey() == key) {
                table.remove(i);
            }
        }
    }

    /**
     * Clears all records in the table
     */
    public void Clear() {
        table.clear();
    }

}
