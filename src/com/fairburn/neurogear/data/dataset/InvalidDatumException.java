package com.fairburn.neurogear.data.dataset;

/**
 * Exception generated when a DataSet
 * receives and invalid Datum.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidDatumException.java
 * Created: 04/23/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a DataSet receives and invalid Datum.
 */
public final class InvalidDatumException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidDatumException(String message) {
    
        super(message);
    }
}
