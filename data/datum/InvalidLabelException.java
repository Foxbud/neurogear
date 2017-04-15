package neurogear.data.datum;

/**
 * Exception generated when a LabeledDatum 
 * receives an invalid label array.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidLabelException.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * LabeledDatum receives an invalid label array.
 */
public final class InvalidLabelException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidLabelException(String message) {
    
        super(message);
    }
}
