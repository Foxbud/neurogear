package neurogear.base.connection;

/**
 * Exception generated when a Connection uses
 * an invalid form regularization.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidRegularizationException.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Connection uses an invalid form regularization.
 */
public final class InvalidRegularizationException extends ConnectionException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidRegularizationException(String message) {
    
        super(message);
    }
}
