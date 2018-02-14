package com.morganwalkup.router2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.morganwalkup.UI.AddAdjacencyDialog;
import com.morganwalkup.UI.UIManager;
import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.daemons.LL1Daemon;
import com.morganwalkup.support.Bootloader;

/**
 * Represents the main activity of the router application
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/2018
 */
public class MainActivity extends AppCompatActivity implements AddAdjacencyDialog.AdjacencyPairListener {

    /**
     * Initialize app and create Bootloader
     * @param savedInstanceState - previously saved state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bootloader bootLoader = new Bootloader(this);
    }

    /**
     * Links the main menu widget with main menu java code
     * @param menu - the java representation of the main menu
     * @return Calls the onCreateOptionsMenu method in the super class
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Displays IP address in toaster on click of the IP Address menu item
     * @param item - the menu item clicked
     * @return Calls the onOptionsItemSelected method in the super class
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.showIPAddress){
            UIManager.getInstance().displayMessage("Your IP address is "+ Constants.IP_ADDRESS);
        }
        else if (item.getItemId() == R.id.addAdjacency){
            AddAdjacencyDialog dialog = new AddAdjacencyDialog();
            dialog.show(getFragmentManager(), "adjacency_dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user is finished editing fields in the adjacency record dialog
     * Passes the new adjacency record data to the LL1Daemon
     * @param ipAddress - String representation of the IP address entered
     * @param ll2pAddress - String representation of the Hex LL2P address entered
     */
    @Override
    public void onFinishedEditDialog(String ipAddress, String ll2pAddress) {
        LL1Daemon.getInstance().addAdjacency(ipAddress, ll2pAddress);
    }
}
