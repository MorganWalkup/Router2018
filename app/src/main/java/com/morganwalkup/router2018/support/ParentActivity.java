package com.morganwalkup.router2018.support;

import android.app.Activity;

/**
 * Created by morganwalkup on 1/9/18.
 */

// Static shell class providing controlled access to the main activity of the router
public class ParentActivity {

    //The main activity of the router
    private static Activity parentActivity;

    public static Activity getParentActivity() {
        return parentActivity;
    }

    public static void setParentActivity(Activity _parentActivity) {
        parentActivity = _parentActivity;
    }
}
