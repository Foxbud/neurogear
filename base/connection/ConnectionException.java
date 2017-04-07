package neurogear.base.connection;

import neurogear.NeuroGearException;

/**
 * Abstract parent class for Connection exceptions.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: ConnectionException.java
 * Created: 04/04/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Specifies requirements for a
 * Connection exception.
 */
public abstract class ConnectionException extends NeuroGearException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public ConnectionException(String message) {
    
        super(message);
    }
}
