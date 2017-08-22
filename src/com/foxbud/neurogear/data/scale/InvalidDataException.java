package com.foxbud.neurogear.data.scale;

/**
 * Exception generated when a Scale receives
 * invalid data.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidDataException.java
 * Created: 05/11/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a Scale receives invalid data.
 */
public final class InvalidDataException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidDataException(String message) {
    
        super(message);
    }
}
