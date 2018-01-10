package com.morganwalkup.router2018.support;

/**
 * Created by morganwalkup on 1/9/18.
 */

/**
 * This is a generic exception class for the lab exceptions we may generate.
 */
public class LabException extends Exception {

    private static final long serialVersionUID = 1L;

    public LabException(String errorMessage){
        super(errorMessage);
    }
}

