package neurogear.base.node;

import neurogear.NeuroGearException;

/**
 * Abstract parent class for Node exceptions.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NodeException.java
 * Created: 04/05/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Specifies requirements for a
 * Node exception.
 */
public abstract class NodeException extends NeuroGearException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public NodeException(String message) {
    
        super(message);
    }
}
