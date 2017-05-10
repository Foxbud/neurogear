package neurogear.data.scale;

/**
 * Normal implementation of Scale.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NormalScale.java
 * Created: 04/18/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Min-max implementation (normal) of
 * Scale. Rescales values to be between 0.0 and 1.0.
 */
public final class NormalScale implements Scale {
    
    // MEMBER VARIABLES.
    
    // Minimums for all data elements.
    private final Double minimums[];
    // Maximums for all data elements.
    private final Double maximums[];
    
    // MEMBER METHODS.
    
    /**
     * Construct a NormalScale with passed scaling factors.
     * @param minimumsP respective minimums of all data elements
     * @param maximumsP respective maximums of all data elements
     */
    NormalScale(Double minimumsP[], Double maximumsP[]) {
    
        minimums = minimumsP.clone();
        maximums = maximumsP.clone();
    }
    
    /**
     * Construct a NormalScale by computing the scaling
     * factors of a formatted set of data.
     * @param data formatted data
     */
    NormalScale(Double data[][]) {
    
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
    
    /**
     * Return this Scale's scaling factors.
     * @return scaling factors where row 0 is mins and row 1 is maxes
     */
    @Override
    public Double[][] getScalingFactors() {
    
        // Temporary array for holding return values.
        Double tempFactors[][] = new Double[2][minimums.length];
        
        tempFactors[0] = minimums.clone();
        tempFactors[1] = maximums.clone();
        
        return tempFactors;
    }
    
    /**
     * Scale data down to the interval [0.0, 1.0].
     * @param data data to be scaled
     * @return scaled data
     */
    @Override
    public Double[] scaleDown(Double data[]) {
    
        // Temporary array for holding return values.
        Double tempData[] = data.clone();
        
        // Scale values to the interval [0.0, 1.0].
        for (int i = 0; i < tempData.length; i++) {
        
            tempData[i] = (tempData[i] - minimums[i]) / (maximums[i] - minimums[i]);
        }
        
        return tempData;
    }
    
    /**
     * Scale data up beyond the interval [0.0, 1.0].
     * @param data data to be scaled
     * @return scaled data
     */
    @Override
    public Double[] scaleUp(Double data[]) {
    
        // Temporary array for holding return values.
        Double tempData[] = data.clone();
        
        // Scale values beyond the interval [0.0, 1.0].
        for (int i = 0; i < tempData.length; i++) {
        
            tempData[i] = tempData[i] * (maximums[i] - minimums[i]) + minimums[i];
        }
        
        return tempData;
    }
}
