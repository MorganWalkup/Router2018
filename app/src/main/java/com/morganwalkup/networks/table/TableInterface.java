package com.morganwalkup.networks.table;

import com.morganwalkup.networks.tablerecord.TableRecord;
import com.morganwalkup.support.LabException;

import java.util.List;

/**
 * Created by morganwalkup on 2/1/18.
 * Interface for all tables in the router
 */
public interface TableInterface {

    /**
     * Returns the table as a list of table records
     * @return A list of table records in the table
     */
    List<TableRecord> getTableAsList();

    /**
     * Adds a table record to the table
     * @return The table record added
     */
    TableRecord addItem(TableRecord tableRecord);

    /**
     * Returns a table record matching the tableRecord specified
     * @param tableRecord The table record to find
     * @return The table record found
     * @throws LabException thrown if the tableRecord is not found
     */
    TableRecord getItem(TableRecord tableRecord) throws LabException;

    /**
     * Returns a table record with the given key
     * @param key The key of the table record to find
     * @return The table record found
     * @throws LabException thrown if tableRecord is not found
     */
    TableRecord getItem(Integer key) throws LabException;

    /**
     * Removes a table record with the given key
     * @param key The key of the table record to remove
     * @return The table record removed, or null if none was removed
     */
    void removeItem(Integer key);

    /**
     * Clears all records in the table
     */
    void Clear();


}
