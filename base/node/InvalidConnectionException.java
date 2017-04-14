package neurogear.base.node;

/**
 * Exception generated when a Node uses
 * an invalid Connection.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidConnectionException.java
 * Created: 04/05/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a Node uses an invalid Connection.
 */
public final class InvalidConnectionException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidConnectionException(String message) {
    
        super(message);
    }
}
