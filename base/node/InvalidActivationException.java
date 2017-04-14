package neurogear.base.node;

/**
 * Exception generated when a Node uses
 * an invalid activation funciton.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidActivationException.java
 * Created: 04/05/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Node uses an invalid activation funciton.
 */
public final class InvalidActivationException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidActivationException(String message) {
    
        super(message);
    }
}
