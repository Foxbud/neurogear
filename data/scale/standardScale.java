package neurogear.data.scale;

/**
 * Standard implementation of Scale.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: standardScale.java
 * Created: 05/10/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Standardization implementation of
 * Scale. Rescales values to fit the standard normal
 * distribution.
 */
public class standardScale implements Scale {
    
    // MEMBER VARIABLES
    
    // Means for all data elements.
    private final Double means[];
    // Standard deviations for all data elements.
    private final Double stdDevs[];
    
    // MEMBER METHODS
    
    /**
     * Construct a standardScale with passed scaling factors.
     * @param meansP respective means of all data elements
     * @param stdDevsP respective standard deviations of all data elements
     */
    standardScale(Double meansP[], Double stdDevsP[]) {
    
        means = meansP.clone();
        stdDevs = stdDevsP.clone();
    }
    
    /**
     * Construct a standardScale by computing the scaling
     * factors of a formatted set of data.
     * @param data formatted data
     */
    standardScale(Double data[][]) {
    
        // Temporary arrays for means and standard deviations.
        Double tempMeans[] = new Double[data[0].length];
        Double tempStdDevs[] = new Double[data[0].length];
        
        // Initialize all elements to zero.
        for (int i = 0; i < tempMeans.length; i++) {
        
            tempMeans[i] = 0.0;
            tempStdDevs[i] = 0.0;
        }
        
        // Find means of all data vectors.
        for (int i = 0; i < data.length; i++) {
        
            // Iterate through all elements in each data vector.
            for (int j = 0; j < data[i].length; j++) {
            
                // Add weighted element.
                tempMeans[j] += data[i][j] / data.length;
            }
        }
        
        // Find the standard deviations of all data vectors.
        for (int i = 0; i < data.length; i++) {
        
            // Iterate through all elements in each data vector.
            for (int j = 0; j < data[i].length; j++) {
            
                // Difference between element and mean.
                Double dif = data[i][j] - tempMeans[j];
                
                // Add weighted squared difference.
                tempStdDevs[j] += dif * dif / data.length;
            }
        }
        
        // Take the square root to find final standard deviations.
        for (int i = 0; i < tempStdDevs.length; i++) {
        
            tempStdDevs[i] = Math.sqrt(tempStdDevs[i]);
        }
        
        // Set means and standard deviations.
        means = tempMeans.clone();
        stdDevs = tempStdDevs.clone();
    }
    
    /**
     * Return this Scale's scaling factors.
     * @return scaling factors where row 0 is means and row 1 is standard deviations
     */
    @Override
    public Double[][] getScalingFactors() {
    
        // Temporary array for holding return values.
        Double tempFactors[][] = new Double[2][means.length];
        
        tempFactors[0] = means.clone();
        tempFactors[1] = stdDevs.clone();
        
        return tempFactors;
    }
    
    /**
     * Scale data down to the standard normal distribution.
     * @param data data to be scaled
     * @return scaled data
     */
    @Override
    public Double[] scaleDown(Double data[]) {
    
        // Temporary array for holding return values.
        Double tempData[] = data.clone();
        
        // Scale values to the standard normal distribution.
        for (int i = 0; i < tempData.length; i++) {
        
            tempData[i] = (tempData[i] - means[i]) / stdDevs[i];
        }
        
        return tempData;
    }
    
    /**
     * Scale data up beyond the standard normal distribution.
     * @param data data to be scaled
     * @return scaled data
     */
    @Override
    public Double[] scaleUp(Double data[]) {
    
        // Temporary array for holding return values.
        Double tempData[] = data.clone();
        
        // Scale values beyond the standard normal distribution.
        for (int i = 0; i < tempData.length; i++) {
        
            tempData[i] = data[i] * stdDevs[i] + means[i];
        }
        
        return tempData;
    }
}
