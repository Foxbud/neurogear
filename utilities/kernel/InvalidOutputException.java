package neurogear.utilities.kernel;

/**
 * Exception generated when a Kernel tries
 * to propagate or backpropagate
 * with disconnected output.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidOutputException.java
 * Created: 05/23/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Kernel tries to propagate or backpropagate
 * with disconnected output.
 */
public final class InvalidOutputException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidOutputException(String message) {
    
        super(message);
    }
}
