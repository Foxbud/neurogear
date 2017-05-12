package neurogear.data.datum;

/**
 * Abstract parent class for datum types.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Datum.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Specifies requirements for an object
 * that holds information.
 */
public abstract class Datum {
    
    // MEMBER VARIABLES.
    
    // Raw datum values.
    protected final Double raw[];
    
    // MEMBER METHODS.
    
    /**
     * Construct a Datum with copy of passed information.
     * @param rawP raw datum values
     * @throws InvalidRawException if parameter 'rawP' is of incorrect type or is empty
     */
    public Datum(Double rawP[]) {
    
        // Test for exceptions.
        if (!(rawP instanceof Double[])) {
        
            throw new InvalidRawException("'rawP' must be of type 'Double[]'");
        }
        else if (rawP.length == 0) {
        
            throw new InvalidRawException("'rawP' must not be empty");
        }
        else {
        
            raw = rawP.clone();
        }
    }
    
    /**
     * Return copy of this Datum's raw datum values.
     * @return raw datum values
     */
    public Double[] getRaw() {
    
        return raw.clone();
    }
}
