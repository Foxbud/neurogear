package neurogear.utilities.kernel;

/**
 * Exception generated when a Kernel tries
 * to override its existing input Nodes.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InputOverrideException.java
 * Created: 05/22/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a Kernel 
 * tries to override its existing input Nodes.
 */
public final class InputOverrideException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InputOverrideException(String message) {
    
        super(message);
    }
}
