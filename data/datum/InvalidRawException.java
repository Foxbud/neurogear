package neurogear.data.datum;

/**
 * Exception generated when a Datum receives
 * an invalid raw array.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidRawException.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a Datum receives an invalid raw array.
 */
public final class InvalidRawException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidRawException(String message) {
    
        super(message);
    }
}
