package com.foxbud.neurogear.data.scale;

/**
 * Exception generated when a Scale receives
 * an invalid scaling factor.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidFactorException.java
 * Created: 05/11/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a Scale receives an invalid scaling factor.
 */
public final class InvalidFactorException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidFactorException(String message) {
    
        super(message);
    }
}
