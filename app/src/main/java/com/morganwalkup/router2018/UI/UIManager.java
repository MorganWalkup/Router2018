package com.morganwalkup.router2018.UI;

import java.util.Observer;
import java.util.Observable;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.morganwalkup.router2018.support.Bootloader;
import com.morganwalkup.router2018.support.ParentActivity;

// Provides overall control of the system UI and delegates specific UI management to lower level UI classes.
public class UIManager implements Observer {

    // Self reference for the singleton pattern
    private static final UIManager ourInstance = new UIManager();
    // Main activity of the router application
    private Activity parentActivity;
    // Provides access to widgets
    private Context context;

    // Constructor for UIManager
    private UIManager() {

    }

    public static UIManager getInstance() {
        return ourInstance;
    }

    // Displays a toast message on the screen for the specified time
    public void displayMessage(String message, int displayTime) {
        Toast.makeText(context, message, displayTime).show();
    }

    // Displays a toast message on the screen
    public void displayMessage(String message) {
        displayMessage(message, Toast.LENGTH_LONG);
    }

    // Used to access any on-screen widgets
    private void setUpWidgets() {

    }

    // Required implementation for Observers
    public void update(Observable observable, Object object) {
        // Initialize UI on update from Bootloader
        if(observable instanceof Bootloader) {
            parentActivity = ParentActivity.getParentActivity();
            context = parentActivity.getBaseContext();
            setUpWidgets();
        }
    }

}
