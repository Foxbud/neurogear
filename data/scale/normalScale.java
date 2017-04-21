package neurogear.data.scale;

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
    
    /**
     * Construct a normalScale by computing the scaling
     * factors of a formatted set of data.
     * @param data formatted data
     */
    normalScale(Double data[][]) {
    
        // Temporary arrays for minimums and maximums.
        Double tempMins[] = data[0].clone();
        Double tempMaxes[] = data[0].clone();
        
        // Search for minimums and maximums in all data vectors.
        for (int i = 0; i < data.length; i++) {
        
            // Iterate through all elements in each data vector.
            for (int j = 0; j < data[i].length; j++) {
            
                // Check for new min.
                if (data[i][j] < tempMins[j]) {
                
                    tempMins[j] = data[i][j];
                }
                
                // Check for new max.
                if (data[i][j] > tempMaxes[j]) {
                
                    tempMaxes[j] = data[i][j];
                }
            }
        }
        
        // Set minimums and maximums.
        minimums = tempMins.clone();
        maximums = tempMaxes.clone();
    }
}
