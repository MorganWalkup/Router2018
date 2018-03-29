package com.morganwalkup.networks.table;

import com.morganwalkup.networks.tablerecord.RoutingRecord;
import com.morganwalkup.networks.tablerecord.TableRecord;
import com.morganwalkup.support.LabException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acts as both routing and forwarding table
 * Created by morganwalkup on 3/11/18.
 */

public class RoutingTable extends TimedTable {
    /**
     * Calls super class constructor
     */
    public RoutingTable() {
        super();
    }

    /**
     * Add newEntry to the RoutingTable
     * If a record with the same key as newEntry already exists, replace it
     * @param newEntry - The RoutingRecord entry to add
     */
    public void addNewRoute(TableRecord newEntry) {
        removeItem(newEntry); //Removes the route record if it exists
        addItem(newEntry); //Adds the new route record to the table
    }

    /**
     * Remove this record from the table if it exists
     * @param newEntry - The RoutingRecord entry to remove
     */
    public void removeItem(TableRecord newEntry) {
        removeItem(newEntry.getKey());
        updateDisplay();
    }

    /**
     * Remove the record with the given key from the table if it exists
     * @param key - The key of the record to remove
     */
    @Override
    public void removeItem(Integer key) {
        //Scan the table for given tableRecord
        for(int i = 0; i < this.table.size(); i++) {
            RoutingRecord record = (RoutingRecord)table.get(i);
            if (record.getKey().equals(key)) {
                table.remove(i);
            }
        }
        //Notify observers of change
        updateDisplay();
    }

    /**
     * Returns the next hop for the given network
     * @param network - A remote network number
     * @return - The LL3P address of the next hop,
     * or -1 if no record is found with the remote network number
     */
    public Integer getNextHop(Integer network) {
        for(int i = 0; i < this.table.size(); i++) {
            RoutingRecord record = (RoutingRecord)this.table.get(i);
            if(record.getNetwork() == network) {
                return record.getNextHop();
            }
        }

        return -1;
    }

    /**
     * Returns list of all records except those learned from the given ll3pAddress
     * Used in the Forwarding Table when sending LRP updates
     * @param ll3pAddress - The LL3PAddress of records to exclude
     * @return - The routing table as a list, excluding records of the given ll3pAddress
     */
    public List<RoutingRecord> getRouteListExcluding(Integer ll3pAddress) {
        //The list of routes excluding the given ll3pAddress
        List<RoutingRecord> newRouteList = new ArrayList<RoutingRecord>();

        //Loop through table and add non-excluded records to newRouteList
        for(int i = 0; i < this.table.size(); i++) {
            RoutingRecord record = (RoutingRecord)this.table.get(i);
            if(record.getNextHop() != ll3pAddress) {
                newRouteList.add(record);
            }
        }

        return newRouteList;
    }

    /**
     * Used when a neighbor dies in the ARP table.
     * Removes all records where the next hop field matches the given ll3pAddress
     * @param ll3pAddress - The LL3PAddress of the dead neighbor
     */
    public void removeRoutesFrom(Integer ll3pAddress) {
        //The list of routes excluding the given ll3pAddress
        List<TableRecord> newRouteList = new ArrayList<TableRecord>();

        //Loop through table and add non-excluded records to newRouteList
        for(int i = 0; i < this.table.size(); i++) {
            RoutingRecord record = (RoutingRecord)this.table.get(i);
            if(!record.getNextHop().equals(ll3pAddress)) {
                newRouteList.add(record);
            }
        }

        this.table = newRouteList;

        updateDisplay();
    }

    /**
     * Returns the best route to each known remote network
     * @return A list of the best routes to each known remote network
     */
    public List<RoutingRecord> getBestRoutes() {
        // Initialize map of network-record pairs
        Map<Integer,RoutingRecord> networkRecordMap = new HashMap<Integer, RoutingRecord>();

        // Loop through table and add best routes to networkRecordMap
        for(int i = 0; i < this.table.size(); i++) {
            RoutingRecord record = (RoutingRecord)this.table.get(i);
            Integer network = record.getNetwork();
            Boolean isRepeatNetwork = networkRecordMap.containsKey(network);
            if(isRepeatNetwork) {
                RoutingRecord oldRecord = networkRecordMap.get(network);
                if(oldRecord.getDistance() > record.getDistance())
                    networkRecordMap.put(network, record);
            } else {
                networkRecordMap.put(network, record);
            }
        }

        // Convert map values to list and return
        return new ArrayList<RoutingRecord>(networkRecordMap.values());
    }

    /**
     * Return the best route (shortest distance) for the given network
     * @param network - The network to find the best route for
     * @returnThe best route for the given network
     */
    public RoutingRecord getBestRoute(Integer network) throws LabException {
        // Trackers for best route
        int shortestDistance = -1;
        RoutingRecord bestRoute = (RoutingRecord)table.get(0);

        // Loop through table to find best route for the network
        for(int i = 0; i < this.table.size(); i++) {
            RoutingRecord record = (RoutingRecord)this.table.get(i);
            if(record.getNetwork() == network) {
                if (shortestDistance == -1 || record.getDistance() < shortestDistance) {
                    bestRoute = record;
                    shortestDistance = record.getDistance();
                }
            }
        }

        // Return best route or throw exception
        if(shortestDistance == -1)
            throw new LabException("No route found containing the given network");
        else
            return bestRoute;
    }

    /**
     * Adds given routes to the RoutingTable
     * @param routes - List of routes to add to the RoutingTable
     */
    public void addRoutes(List<RoutingRecord> routes) {
        if(this.table.size() == 0) {
            this.table.addAll(routes);
        } else {
            for (int i = 0; i < routes.size(); i++) {
                this.addNewRoute(routes.get(i));
            }
        }

        updateDisplay();
    }

    /**
     * Clears all records in the RoutingTable
     */
    public void clear() {
        this.table.clear();
        updateDisplay();
    }
}
