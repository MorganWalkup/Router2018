package com.morganwalkup.support;

import android.app.Activity;

/**
 * Static shell class providing controlled access to the main activity of the router
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/18
 */
public class ParentActivity {

    //The main activity of the router
    private static Activity parentActivity;

    /**
     * Getter for parent activity
     * @return - the parent activity
     */
    public static Activity getParentActivity() {
        return parentActivity;
    }

    /**
     * Setter for the parent activity
     * @param _parentActivity - the new value for parent activity
     */
    public static void setParentActivity(Activity _parentActivity) {
        parentActivity = _parentActivity;
    }
}
