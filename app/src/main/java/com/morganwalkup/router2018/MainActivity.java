package com.morganwalkup.router2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.morganwalkup.support.Bootloader;

/**
 * Represents the main activity of the router application
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/2018
 */
public class MainActivity extends AppCompatActivity {

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
}
