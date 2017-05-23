package neurogear.utilities.kernel;

/**
 * Exception generated when a Kernel's
 * Connections fall out of bounds.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: ConnectionOutOfBoundsException.java
 * Created: 05/22/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Kernel's Connections fall out of bounds.
 */
public final class ConnectionOutOfBoundsException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public ConnectionOutOfBoundsException(String message) {
    
        super(message);
    }
}
