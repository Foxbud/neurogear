package neurogear.base.cost;

import neurogear.NeuroGearException;

/**
 * Abstract parent class for Cost exceptions.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: CostException.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Specifies requirements for a
 * Cost exception.
 */
public abstract class CostException extends NeuroGearException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public CostException(String message) {
    
        super(message);
    }
}
