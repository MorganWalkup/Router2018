package com.morganwalkup.support;

import java.util.Observable;
import android.app.Activity;
import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;

/**
 * Responsible for booting the router
 * Creates classes and declares them as observers
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/18
 */
public class Bootloader extends Observable {

    /**
     * Constructor for the BootLoader class
     * @param parentActivity - The main activity of the router application
     */
    public Bootloader(Activity parentActivity) {
        bootRouter(parentActivity);
    }

    /**
     * Boots the router by instantiating high-level router classes
     * @param parentActivity - The main activity of the router application
     */
    private void bootRouter(Activity parentActivity) {
        // Initialize ParentActivity
        ParentActivity.setParentActivity(parentActivity);
        // Add all observers
        addObserver(UIManager.getInstance());
        // Finish setup work
        // Notify observers that we're ready to go
        setChanged(); // this lets Java know something has changed!
        notifyObservers(); // this calls the update(…) method in observers

        test();
    }

    /**
     * Runs basic tests to verify operation of the router
     */
    private void test() {
        UIManager.getInstance().displayMessage(Constants.ROUTER_NAME + " is up and Running!");
        UIManager.getInstance().displayMessage("IP Address is " + Constants.IP_ADDRESS);
    }
}
