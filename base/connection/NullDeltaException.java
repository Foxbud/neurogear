package neurogear.base.connection;

/**
 * Exception generated when a Connection attempts
 * to compute the average of zero deltas.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: BadCorrectException.java
 * Created: 04/04/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a Connection 
 * attempts to compute the average of zero deltas.
 */
public final class NullDeltaException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public NullDeltaException(String message) {
    
        super(message);
    }
}
