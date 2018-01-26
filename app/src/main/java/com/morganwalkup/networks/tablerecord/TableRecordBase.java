package com.morganwalkup.networks.tablerecord;

/**
 * Base class for all router table records
 * Created by morganwalkup on 1/25/18.
 */

public class TableRecordBase implements TableRecord {

    /** Value in seconds of the last time the record was referenced (touched) or installed in the table */
    private int lastTimeTouched;

    public TableRecordBase() {
        this.updateTime();
    }

    /**
     * Get difference between current system time and last time touched
     * @return Difference (in seconds) between system time and last time touched
     */
    public Integer getAgeInSeconds() {
        int currentSeconds = (int)System.currentTimeMillis()/1000;
        return currentSeconds - this.lastTimeTouched;
    }

    /**
     * Returns the key in the record
     * @return the key in the record
     */
    public Integer getKey() {
        return null;
    }

    /**
     * Updates the last time touched to the current time
     */
    public void updateTime() {
        this.lastTimeTouched = (int)System.currentTimeMillis()/1000;
    }

    /**
     * Allows records to compare themselves to each other
     * @param tableRecord - The table record to compare this record with
     * @return A negative number if this is less than tableRecord, zero if equal, and positive if greater
     */
    public int compareTo(TableRecord tableRecord) {
        return getKey().compareTo(tableRecord.getKey());
    }

}
