package neurogear.base.cost;

/**
 * Exception generated when the cross-entropy
 * cost function recieves values outside the
 * range of 0.0 to 1.0.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: CrossEntropyException.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when the 
 * cross-entropy cost function recieves values 
 * outside the range of 0.0 to 1.0.
 */
public final class CrossEntropyException extends CostException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public CrossEntropyException(String message) {
    
        super(message);
    }
}
