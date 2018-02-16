package com.morganwalkup.UI;

import java.util.Observer;
import java.util.Observable;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.morganwalkup.support.Bootloader;
import com.morganwalkup.support.ParentActivity;

/**
 * Provides overall control of the system UI and delegates specific
 * UI management to lower level UI classes.
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/18
 */
public class UIManager implements Observer {

    // Self reference for the singleton pattern
    private static final UIManager ourInstance = new UIManager();
    // Main activity of the router application
    private Activity parentActivity;
    // Provides access to widgets
    private Context context;
    // Manager for all table ui
    private TableUI tableUI;
    public TableUI getTableUI() { return this.tableUI; }
    // Manager for sniffer ui
    private SnifferUI snifferUI;
    public SnifferUI getSnifferUI() { return this.snifferUI; }

    /**
     * Constructor for the UIManager class
     */
    private UIManager() {

        tableUI = new TableUI();
        snifferUI = new SnifferUI();
    }

    /**
     * Getter for static final UIManager instance
     * @return - The single UIManager instance
     */
    public static UIManager getInstance() {
        return ourInstance;
    }

    /**
     * Displays a toast message on the screen for the specified time
     * @param message - the message to display
     * @param displayTime - the length of time to display the message
     */
    public void displayMessage(String message, int displayTime) {
        Toast.makeText(context, message, displayTime).show();
    }

    /**
     * Displays a toast message on the screen for the specified time
     * @param message - the message to display
     */
    public void displayMessage(String message) {
        displayMessage(message, Toast.LENGTH_LONG);
    }

    /**
     * Used to access any onscreen widgets
     */
    private void setUpWidgets() {

    }

    /**
     * Observing the Bootloader
     * @param observable - Reference to the Bootloader
     * @param object - An argument passed by the Bootloader
     */
    @Override
    public void update(Observable observable, Object object) {
        // Initialize UI on update from Bootloader
        if(observable instanceof Bootloader) {
            parentActivity = ParentActivity.getParentActivity();
            context = parentActivity.getBaseContext();
            setUpWidgets();
        }
    }

}
