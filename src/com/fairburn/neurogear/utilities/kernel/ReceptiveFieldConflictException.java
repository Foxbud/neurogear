package com.fairburn.neurogear.utilities.kernel;

/**
 * Exception generated when a Kernel's
 * receptive field is incompatible with
 * current input and output.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: ReceptiveFieldConflictException.java
 * Created: 05/28/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Kernel's receptive field is incompatible 
 * with current input and output.
 */
public final class ReceptiveFieldConflictException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public ReceptiveFieldConflictException(String message) {
    
        super(message);
    }
}
