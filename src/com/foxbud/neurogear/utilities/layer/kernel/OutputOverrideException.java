package com.foxbud.neurogear.utilities.layer.kernel;

/**
 * Exception generated when a Kernel tries
 * to override its existing output Nodes.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: OutputOverrideException.java
 * Created: 05/22/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a Kernel 
 * tries to override its existing output Nodes.
 */
public final class OutputOverrideException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public OutputOverrideException(String message) {
    
        super(message);
    }
}
