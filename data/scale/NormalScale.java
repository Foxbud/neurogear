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
    private double minimums[][];
    // Maximums for all data elements.
    private double maximums[][];
    
    // MEMBER METHODS.
    
    /**
     * Construct a NormalScale.
     */
    public NormalScale() {
    
        minimums = null;
        maximums = null;
    }
    
    /**
     * Set this NormalScale's scaling factors.
     * @param minimumsP respective minimums of all data elements
     * @param maximumsP respective maximums of all data elements
     * @throws InvalidFactorException if parameters 'minimumsP' and 'maximumsP' are not valid
     */
    @Override
    public void setScalingFactors(double minimumsP[][], double maximumsP[][]) {
    
        // Test for exceptions.
        if (minimumsP == null || maximumsP == null) {
        
            throw new InvalidFactorException("parameters must not be null");
        }
        else if (minimumsP.length != maximumsP.length) {
        
            throw new InvalidFactorException("'minimumsP' and 'maximumsP' must have the same number of channels");
        }
        else if (minimumsP.length == 0) {
        
            throw new InvalidFactorException("parameters must have at least one channel each");
        }
        else {
        
            // Check each element for a range of zero.
            for (int i = 0; i < minimumsP.length; i++) {
            
                for (int j = 0; j < minimumsP[i].length; j++) {
                
                    if (minimumsP[i][j] == maximumsP[i][j]) {

                        throw new InvalidFactorException("each element must have a range greater than 0.0");
                    }
                }
            }
        }
        
        minimums = minimumsP;
        maximums = maximumsP;
    }
    
    /**
     * Return this NormalScale's scaling factors.
     * @return scaling factors where row 0 is mins and row 1 is maxes
     */
    @Override
    public double[][][] getScalingFactors() {
    
        // Temporary array for holding return values.
        double tempFactors[][][] = new double[2][][];
        
        tempFactors[0] = minimums;
        tempFactors[1] = maximums;
        
        return tempFactors;
    }
    
    /**
     * Compute and set this NormalScale's scaling 
     * factors from a formatted set of data.
     * @param data formatted data
     * @throws InvalidDataException if parameter 'data' is not valid
     * @throws InvalidFactorException if generated factors are not valid
     */
    @Override
    public void computeScalingFactors(double data[][][]) {
    
        // Test for exceptions.
        if (data == null) {
        
            throw new InvalidDataException("'data' must not be null");
        }
        else if (data.length == 0) {
        
            throw new InvalidDataException("'data' must contain at least one array");
        }
        else if (data[0].length == 0) {
        
            throw new InvalidDataException("each array of 'data' must contain at least one channel");
        }
        else if (data[0][0].length == 0) {
        
            throw new InvalidDataException("each channel of 'data' must contain at least one element");
        }
        
        // Temporary arrays for minimums and maximums.
        double tempMins[][] = new double[data[0].length][data[0][0].length];
        double tempMaxes[][] = new double[data[0].length][data[0][0].length];
        
        // Copy first data vector.
        for (int i = 0; i < tempMins.length; i++) {
        
            for (int j = 0; j < tempMins[i].length; j++) {
            
                tempMins[i][j] = data[0][i][j];
                tempMaxes[i][j] = data[0][i][j];
            }
        }
        
        // Search for minimums and maximums in all data vectors.
        for (int i = 1; i < data.length; i++) {
        
            // Iterate through all channels in each data vector.
            for (int j = 0; j < data[i].length; j++) {
            
                // Iterate through all elements in each channel.
                for (int k = 0; k < data[i][j].length; k++) {
                    
                    // Check for new min.
                    if (data[i][j][k] < tempMins[j][k]) {

                        tempMins[j][k] = data[i][j][k];
                    }

                    // Check for new max.
                    if (data[i][j][k] > tempMaxes[j][k]) {

                        tempMaxes[j][k] = data[i][j][k];
                    }
                }
            }
        }
        
        // Test for exception.
        for (int i = 0; i < tempMins.length; i++) {

            for (int j = 0; j < tempMins[i].length; j++) {
            
                if (tempMins[i][j] == tempMaxes[i][j]) {

                    throw new InvalidFactorException("each element must have a range greater than 0.0");
                }
            }
        }
        
        // Set minimums and maximums.
        minimums = tempMins;
        maximums = tempMaxes;
    }
    
    /**
     * Scale data down to the interval [0.0, 1.0].
     * @param data data to be scaled
     * @return scaled data
     * @throws InvalidDataException if parameter 'data' is not valid
     * @throws UninitializedFactorException if scaling factors have not been initialized
     */
    @Override
    public double[][] scaleDown(double data[][]) {
    
        testForExceptions(data);
        
        // Temporary array for holding return values.
        double tempData[][] = new double[data.length][data[0].length];
        
        // Scale values to the interval [0.0, 1.0].
        for (int i = 0; i < tempData.length; i++) {
        
            for (int j = 0; j < tempData[i].length; j++) {
                
                tempData[i][j] = (data[i][j] - minimums[i][j]) / (maximums[i][j] - minimums[i][j]);
            }
        }
        
        return tempData;
    }
    
    /**
     * Scale data up beyond the interval [0.0, 1.0].
     * @param data data to be scaled
     * @return scaled data
     * @throws InvalidDataException if parameter 'data' is not valid
     * @throws UninitializedFactorException if scaling factors have not been initialized
     */
    @Override
    public double[][] scaleUp(double data[][]) {
    
        testForExceptions(data);
        
        // Temporary array for holding return values.
        double tempData[][] = new double[data.length][data[0].length];
        
        // Scale values beyond the interval [0.0, 1.0].
        for (int i = 0; i < tempData.length; i++) {
        
            for (int j = 0; j < tempData[i].length; j++) {
                
                tempData[i][j] = data[i][j] * (maximums[i][j] - minimums[i][j]) + minimums[i][j];
            }
        }
        
        return tempData;
    }
    
    // HELPER METHODS.
    
    /**
     * Check for exceptions before scaling data.
     * @param data data to be scaled
     * @throws InvalidDataException if parameter 'data' is not valid
     * @throws UninitializedFactorException if scaling factors have not been initialized
     */
    private void testForExceptions(double data[][]) {
    
        // Test for exceptions.
        if (data == null) {
        
            throw new InvalidDataException("'data' must not be null");
        }
        else if (data.length != minimums.length) {
        
            throw new InvalidDataException("'data' has " + data.length + " channels when the scaling factors have " + minimums.length);
        }
        else if (minimums == null) {
    
            throw new UninitializedFactorException("must initialize scaling factors before scaling data");
        }
    }
}
