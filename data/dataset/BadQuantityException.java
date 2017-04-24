package neurogear.data.dataset;

/**
 * Exception generated when a DataSet
 * checks for a buffer quantity less
 * than 1.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: BadQuantityException.java
 * Created: 04/23/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * DataSet checks for a buffer quantity less
 * than 1.
 */
public final class BadQuantityException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public BadQuantityException(String message) {
    
        super(message);
    }
}
