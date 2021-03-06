package com.fairburn.neurogear.base.connection;

/**
 * Exception generated when a Connection uses
 * an invalid input.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidInputException.java
 * Created: 04/04/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Connection uses an invalid input.
 */
public final class InvalidInputException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidInputException(String message) {
    
        super(message);
    }
}
