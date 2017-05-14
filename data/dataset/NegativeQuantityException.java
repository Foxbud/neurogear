package neurogear.data.dataset;

/**
 * Exception generated when a DataSet
 * tries to test for a negative quantity.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NegativeQuantityException.java
 * Created: 05/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a DataSet
 * tries to test for a negative quantity.
 */
public final class NegativeQuantityException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public NegativeQuantityException(String message) {
    
        super(message);
    }
}
