package neurogear.data.dataset;

/**
 * Exception generated when a DataSet
 * requests an item from an empty
 * shuffle buffer.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: EmptyBufferException.java
 * Created: 04/23/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a DataSet requests an item from an empty
 * shuffle buffer.
 */
public final class EmptyBufferException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public EmptyBufferException(String message) {
    
        super(message);
    }
}
