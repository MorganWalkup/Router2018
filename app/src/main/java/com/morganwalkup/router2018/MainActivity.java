package com.morganwalkup.router2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.morganwalkup.router2018.support.Bootloader;

//Main activity for the router app
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bootloader bootLoader = new Bootloader(this);
    }
}
