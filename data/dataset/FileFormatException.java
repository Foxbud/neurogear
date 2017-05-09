package neurogear.data.dataset;

/**
 * Exception generated when a DataSet
 * tries to load fom an incorrectly 
 * formatted file.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: FileFormatException.java
 * Created: 05/09/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Exception generated when a 
 * DataSet tries to load fom an incorrectly 
 * formatted file.
 */
public final class FileFormatException extends RuntimeException {
    
    /**
     * Construct an exception with a given message.
     * @param message exception message
     */
    public FileFormatException(String message) {
    
        super(message);
    }
}
