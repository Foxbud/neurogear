package com.foxbud.neurogear.data.scale;

/**
 * Standard implementation of Scale.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: StandardScale.java
 * Created: 05/10/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Standardization implementation of
 * Scale. Rescales values to fit the standard normal
 * distribution.
 */
public class StandardScale implements Scale {
    
    // MEMBER VARIABLES
    
    // Means for all data elements.
    private double means[][];
    // Standard deviations for all data elements.
    private double stdDevs[][];
    
    // MEMBER METHODS
    
    /**
     * Construct a StandardScale.
     */
    public StandardScale() {
    
        means = null;
        stdDevs = null;
    }
    
    /**
     * Set this StandardScale's scaling factors.
     * @param meansP respective means of all data elements
     * @param stdDevsP respective standard deviations of all data elements
     * @throws InvalidFactorException if parameters 'meansP' and 'stdDevsP' are not valid
     */
    @Override
    public void setScalingFactors(double meansP[][], double stdDevsP[][]) {
    
        // Test for exceptions.
        if (means == null || stdDevs == null) {
        
            throw new InvalidFactorException("parameters must not be null");
        }
        else if (meansP.length != stdDevsP.length) {
        
            throw new InvalidFactorException("'meansP' and 'stdDevsP' must have the same number of channels");
        }
        else if (meansP.length == 0) {
        
            throw new InvalidFactorException("parameters must have at least one channel each");
        }
        else {
        
            // Check each element for a standard deviation of zero or less.
            for (int i = 0; i < stdDevsP.length; i++) {
            
                for (int j = 0; j < stdDevsP[i].length; j++) {
                
                    if (stdDevsP[i][i] <= 0.0) {

                        throw new InvalidFactorException("each element must have a standard deviation greater than 0.0");
                    }
                }
            }
        }
        
        means = meansP;
        stdDevs = stdDevsP;
    }
    
    /**
     * Return this StandardScale's scaling factors.
     * @return scaling factors where [0][][] is means and [1][][] is standard deviations
     */
    @Override
    public double[][][] getScalingFactors() {
    
        // Temporary array for holding return values.
        double tempFactors[][][] = new double[2][][];
        
        tempFactors[0] = means;
        tempFactors[1] = stdDevs;
        
        return tempFactors;
    }
    
    /**
     * Compute and set this StandardScale's scaling 
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
        
        // Temporary arrays for means and standard deviations.
        double tempMeans[][] = new double[data[0].length][data[0][0].length];
        double tempStdDevs[][] = new double[data[0].length][data[0][0].length];
        
        // Initialize all elements to zero.
        for (int i = 0; i < tempMeans.length; i++) {
        
            for (int j = 0; j < tempMeans[i].length; j++) {
            
                tempMeans[i][j] = 0.0;
                tempStdDevs[i][j] = 0.0;
            }
        }
        
        // Find means of all data vectors.
        for (int i = 0; i < data.length; i++) {
        
            // Iterate through all channels in each data vector.
            for (int j = 0; j < data[i].length; j++) {
            
                // Iterate through all elements in each channel.
                for (int k = 0; k < data[i][j].length; k++) {
                
                    // Add weighted element.
                    tempMeans[j][k] += data[i][j][k] / data.length;
                }
            }
        }
        
        // Find the standard deviations of all data vectors.
        for (int i = 0; i < data.length; i++) {
        
            // Iterate through all channels in each data vector.
            for (int j = 0; j < data[i].length; j++) {
            
                // Iterate through all elements in each channel.
                for (int k = 0; k < data[i][j].length; k++) {
                
                    // Difference between element and mean.
                    double dif = data[i][j][k] - tempMeans[j][k];

                    // Add weighted squared difference.
                    tempStdDevs[j][k] += dif * dif / data.length;
                }
            }
        }
        
        // Take the square root to find final standard deviations.
        for (int i = 0; i < tempStdDevs.length; i++) {
        
            for (int j = 0; j < tempStdDevs[i].length; j++)
            
            tempStdDevs[i][j] = Math.sqrt(tempStdDevs[i][j]);
        }
        
        // Test for exception.
        for (int i = 0; i < tempStdDevs.length; i++) {

            for (int j = 0; j < tempStdDevs[i].length; j++) {
            
                if (tempStdDevs[i][j] == 0.0) {

                    throw new InvalidFactorException("each element must have a standard deviation greater than 0.0");
                }
            }
        }
        
        // Set means and standard deviations.
        means = tempMeans;
        stdDevs = tempStdDevs;
    }
    
    /**
     * Scale data down to the standard normal distribution.
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
        
        // Scale values to the standard normal distribution.
        for (int i = 0; i < tempData.length; i++) {
        
            for (int j = 0; j < tempData[i].length; j++) {
            
                tempData[i][j] = (data[i][j] - means[i][j]) / stdDevs[i][j];
            }
        }
        
        return tempData;
    }
    
    /**
     * Scale data up beyond the standard normal distribution.
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
        
        // Scale values beyond the standard normal distribution.
        for (int i = 0; i < tempData.length; i++) {
        
            for (int j = 0; j < tempData[i].length; j++) {
            
                tempData[i][j] = data[i][j] * stdDevs[i][j] + means[i][j];
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
        else if (data.length != means.length) {
        
            throw new InvalidDataException("'data' has " + data.length + " channels when the scaling factors have " + means.length);
        }
        else if (means == null) {
    
            throw new UninitializedFactorException("must initialize scaling factors before scaling data");
        }
    }
}
