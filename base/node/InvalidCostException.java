package neurogear.base.node;

/**
 * Exception generated when a Node uses
 * an invalid cost function.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidCostException.java
 * Created: 04/05/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a Node uses an invalid cost function.
 */
public final class InvalidCostException extends NodeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidCostException(String message) {
    
        super(message);
    }
}
