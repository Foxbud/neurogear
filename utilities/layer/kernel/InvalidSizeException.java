package neurogear.utilities.layer.kernel;

/**
 * Exception generated when a Kernel receives
 * an invalid size.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InvalidSizeException.java
 * Created: 05/22/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when 
 * a Kernel receives an invalid size.
 */
public final class InvalidSizeException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InvalidSizeException(String message) {
    
        super(message);
    }
}
