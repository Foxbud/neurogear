package neurogear.base.connection;

/**
 * Exception generated when a Connection does
 * not support a particular method.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: BadUsageException.java
 * Created: 05/12/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Connection does not support a particular method.
 */
public final class BadUsageException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public BadUsageException(String message) {
    
        super(message);
    }
}
