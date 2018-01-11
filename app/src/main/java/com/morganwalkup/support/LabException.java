package com.morganwalkup.support;

/**
 * Generic exception class for the lab exceptions we may generate
 *
 * @author Morgan Walkup
 * @version 1.0
 * @since 1/11/18
 */
public class LabException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor for the LabException class
     * @param errorMessage - The message string of the exception
     */
    public LabException(String errorMessage){
        super(errorMessage);
    }
}

