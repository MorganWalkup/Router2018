package com.morganwalkup.router2018.support;

import java.util.Observable;
import android.app.Activity;
import com.morganwalkup.router2018.UI.UIManager;
import com.morganwalkup.router2018.networks.Constants;

/**
 * Created by morganwalkup on 1/9/18.
 */

/*
Responsible for booting the router
Creates classes and declares them as observers
*/
public class Bootloader extends Observable {

    //Constructor for Bootloader
    public Bootloader(Activity parentActivity) {
        bootRouter(parentActivity);
    }

    //Boots the router by instantiating high-level router classes
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

    //Runs basic tests to verify operation of the router
    private void test() {
        UIManager.getInstance().displayMessage(Constants.ROUTER_NAME + " is up and Running!");
    }
}
