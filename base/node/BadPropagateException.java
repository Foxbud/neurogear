package neurogear.base.node;

/**
 * Exception generated when a Node uses
 * the incorrect propagate method.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: BadPropagateException.java
 * Created: 04/05/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Node uses the incorrect propagate method.
 */
public final class BadPropagateException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public BadPropagateException(String message) {
    
        super(message);
    }
}
