package neurogear.base.connection;

/**
 * Exception generated when a Connection tries
 * to override its existing output.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: OutputOverrideException.java
 * Created: 04/10/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Connection tries to override its existing output.
 */
public final class OutputOverrideException extends ConnectionException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public OutputOverrideException(String message) {
    
        super(message);
    }
}
