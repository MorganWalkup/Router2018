package com.morganwalkup.networks.daemons;

import com.morganwalkup.UI.SingleTableUI;
import com.morganwalkup.UI.TableUI;
import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.support.Bootloader;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by morganwalkup on 3/1/18.
 */

public class Scheduler implements Observer {

    /** The one and only instance of this class */
    private static final Scheduler ourInstance = new Scheduler();
    public static Scheduler getInstance() {
        return ourInstance;
    }
    /** Manages the different runnable jobs */
    private ScheduledThreadPoolExecutor threadManager;
    /** Reference to the ARP Daemon */
    private ARPDaemon arpDaemon;
    /** Reference to the LRP Daemon */
    private LRPDaemon lrpDaemon;
    /** Reference to the TableUI runnable, updated every second */
    private TableUI tableUI;

    /**
     * Default constructor
     */
    Scheduler() {}

    /**
     * Called on state changes within observed objects
     * @param observable - The observed object
     * @param o - Object holding state changes
     */
    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof Bootloader) {
            this.tableUI = UIManager.getInstance().getTableUI();
            this.arpDaemon = ARPDaemon.getInstance();
            this.lrpDaemon = LRPDaemon.getInstance();
            threadManager = new ScheduledThreadPoolExecutor(Constants.THREAD_COUNT);
            threadManager.scheduleAtFixedRate(
                    tableUI,
                    Constants.ROUTER_BOOT_TIME,
                    Constants.UI_UPDATE_INTERVAL,
                    TimeUnit.SECONDS);
            threadManager.scheduleAtFixedRate(
                    arpDaemon,
                    Constants.ROUTER_BOOT_TIME,
                    Constants.UI_UPDATE_INTERVAL,
                    TimeUnit.SECONDS);
            threadManager.scheduleAtFixedRate(
                    lrpDaemon,
                    Constants.ROUTER_BOOT_TIME,
                    Constants.LRP_UPDATE_INTERVAL,
                    TimeUnit.SECONDS);
        }
    }
}
