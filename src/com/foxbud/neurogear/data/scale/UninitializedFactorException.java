package com.foxbud.neurogear.data.scale;

/**
 * Exception generated when a Scale tries
 * to scale data without initialized scaling
 * factors.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: UninitializedFactorException.java
 * Created: 05/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a Scale 
 * tries to scale data without initialized scaling
 * factors.
 */
public final class UninitializedFactorException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public UninitializedFactorException(String message) {
    
        super(message);
    }
}
