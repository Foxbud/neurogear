package neurogear.data.datum;

/**
 * Exception generated when an UnlabeledDatum
 * uses an invalid PRNG.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidRandomException.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * an UnlabeledDatum uses an invalid PRNG.
 */
public final class InvalidRandomException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidRandomException(String message) {
    
        super(message);
    }
}
