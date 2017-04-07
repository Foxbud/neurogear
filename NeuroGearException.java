package neurogear;

/**
 * Abstract parent class for all exceptions.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NeuroGearException.java
 * Created: 04/04/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Specifies requirements for 
 * all exceptions.
 */
public abstract class NeuroGearException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public NeuroGearException(String message) {
    
        super(message);
    }
}
