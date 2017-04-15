package neurogear.data.datum;

/**
 * Exception generated when an UnlabeledDatum
 * uses a noise rate greater than 1.0 or less than
 * or equal to 0.0.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: BadRateException.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when an UnlabeledDatum
 * uses a noise rate greater than 1.0 or less than
 * or equal to 0.0.
 */
public final class BadRateException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public BadRateException(String message) {
    
        super(message);
    }
}
