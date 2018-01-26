package com.morganwalkup.networks.tablerecord;

/**
 * Interface representing records in the router's tables
 * Created by morganwalkup on 1/25/18.
 */

public interface TableRecord {

    /**
     * Returns the key in the record
     * @return the key in the record
     */
    Integer getKey();

    /**
     * Return time passed in seconds since this record was last referenced
     * If no age variable is present in the record, the function will return "0"
     * @return the seconds passed since this record was referenced
     */
    Integer getAgeInSeconds();


}
