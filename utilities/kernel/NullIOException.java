package neurogear.utilities.kernel;

/**
 * Exception generated when a Kernel tries
 * to propagate or backpropagate
 * with disconnected input or output.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NullIOException.java
 * Created: 05/22/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * Kernel tries to propagate or backpropagate
 * with disconnected input or output.
 */
public final class NullIOException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public NullIOException(String message) {
    
        super(message);
    }
}
