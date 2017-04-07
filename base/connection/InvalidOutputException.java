package neurogear.base.connection;

/**
 * Exception generated when a Connection uses
 * an invalid output.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidOutputException.java
 * Created: 04/04/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Connection uses an invalid output.
 */
public final class InvalidOutputException extends ConnectionException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidOutputException(String message) {
    
        super(message);
    }
}
