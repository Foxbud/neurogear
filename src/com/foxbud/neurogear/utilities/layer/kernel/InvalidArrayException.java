package com.foxbud.neurogear.utilities.layer.kernel;

/**
 * Exception generated when a Kernel 
 * receives and invalid array.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidArrayException.java
 * Created: 05/28/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a Kernel receives and invalid array.
 */
public final class InvalidArrayException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidArrayException(String message) {
    
        super(message);
    }
}
