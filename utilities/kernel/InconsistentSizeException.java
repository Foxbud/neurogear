package neurogear.utilities.kernel;

/**
 * Exception generated when a Kernel receives
 * conflicting array sizes.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: InconsistentSizeException.java
 * Created: 05/22/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Kernel receives conflicting array sizes.
 */
public final class InconsistentSizeException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public InconsistentSizeException(String message) {
    
        super(message);
    }
}
