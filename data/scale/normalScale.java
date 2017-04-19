package neurogear.data.scale;

import neurogear.data.dataset.DataSet;
import neurogear.data.datum.Datum;

/**
 * Normal implementation of Scale.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: normalScale.java
 * Created: 04/18/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Min-max implementation (normal) of
 * Scale. Rescales values to be between 0.0 and 1.0.
 */
public final class normalScale implements Scale {
    
    // MEMBER VARIABLES.
    
    // Minimums for all data elements.
    private final Double minimums[];
    // Maximums for all data elements.
    private final Double maximums[];
    
    // MEMBER METHODS.
    
    /**
     * Construct a normalScale with passed scaling factors.
     * @param minimumsP respective minimums of all data elements
     * @param maximumsP respective maximums of all data elements
     */
    normalScale(Double minimumsP[], Double maximumsP[]) {
    
        minimums = minimumsP.clone();
        maximums = maximumsP.clone();
    }
    
    normalScale(DataSet dataSet, boolean forLabel) {
    
        
    }
}
