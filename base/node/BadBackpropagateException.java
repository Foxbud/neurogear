package neurogear.base.node;

/**
 * Exception generated when a Node uses
 * the incorrect backpropagate method.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: BadBackpropagateException.java
 * Created: 04/05/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Node uses the incorrect backpropagate method.
 */
public final class BadBackpropagateException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public BadBackpropagateException(String message) {
    
        super(message);
    }
}
