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
     * @throws InvalidFactorException if parameters 'minimumsP' and 'maximumsP' are not valid
     */
    NormalScale(Double minimumsP[], Double maximumsP[]) {
    
        // Test for exceptions.
        if (!(minimumsP instanceof Double[]) || !(maximumsP instanceof Double[])) {
        
            throw new InvalidFactorException("parameters must be of type 'Double[]'");
        }
        else if (minimumsP.length != maximumsP.length) {
        
            throw new InvalidFactorException("'minimumsP' and 'maximumsP' must have the same length");
        }
        else if (minimumsP.length == 0) {
        
            throw new InvalidFactorException("parameters must have at least one element each");
        }
        else {
        
            // Check each element for a range of zero.
            for (int i = 0; i < minimumsP.length; i++) {
            
                if (minimumsP[i].doubleValue() == maximumsP[i].doubleValue()) {
                
                    throw new InvalidFactorException("each element must have a range greater than 0.0");
                }
            }
        }
        
        minimums = minimumsP.clone();
        maximums = maximumsP.clone();
    }
    
    /**
     * Construct a NormalScale by computing the scaling
     * factors of a formatted set of data.
     * @param data formatted data
     * @throws InvalidDataException if parameter 'data' is not valid
     * @throws InvalidFactorException if generated factors are not valid
     */
    NormalScale(Double data[][]) {
    
        // Test for exceptions.
        if (!(data instanceof Double[][])) {
        
            throw new InvalidDataException("'data' must be of type 'Double[][]'");
        }
        else if (data.length == 0) {
        
            throw new InvalidDataException("'data' must contain at least one array");
        }
        else if (data[0].length == 0) {
        
            throw new InvalidDataException("each array of 'data' must contain at least one element");
        }
        
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
        
        // Test for exception.
        for (int i = 0; i < tempMins.length; i++) {

            if (tempMins[i].doubleValue() == tempMaxes[i].doubleValue()) {

                throw new InvalidFactorException("each element must have a range greater than 0.0");
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
     * @throws InvalidDataException if parameter 'data' is not valid
     */
    @Override
    public Double[] scaleDown(Double data[]) {
    
        // Test for exceptions.
        if (!(data instanceof Double[])) {
        
            throw new InvalidDataException("'data' must be of type 'Double[]'");
        }
        else if (data.length != minimums.length) {
        
            throw new InvalidDataException("'data' had a length of " + data.length + " when the scaling factors have a length of " + minimums.length);
        }
        
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
     * @throws InvalidDataException if parameter 'data' is not valid
     */
    @Override
    public Double[] scaleUp(Double data[]) {
    
        // Test for exceptions.
        if (!(data instanceof Double[])) {
        
            throw new InvalidDataException("'data' must be of type 'Double[]'");
        }
        else if (data.length != minimums.length) {
        
            throw new InvalidDataException("'data' had a length of " + data.length + " when the scaling factors have a length of " + minimums.length);
        }
        
        // Temporary array for holding return values.
        Double tempData[] = data.clone();
        
        // Scale values beyond the interval [0.0, 1.0].
        for (int i = 0; i < tempData.length; i++) {
        
            tempData[i] = tempData[i] * (maximums[i] - minimums[i]) + minimums[i];
        }
        
        return tempData;
    }
}
