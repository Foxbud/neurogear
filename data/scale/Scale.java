package neurogear.data.scale;

/**
 * Interface for implementing scaling functions.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Scale.java
 * Created: 04/17/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Specifies requirements for a 
 * scaling function. Implementations should be final.
 */
public interface Scale {
    
    /**
     * Set this Scale's scaling factors.
     * @param firstFactors first logical set of factors
     * @param secondFactors second logical set of factors
     */
    public void setScalingFactors(double firstFactors[], double secondFactors[]);
    
    /**
     * Return this Scale's scaling factors.
     * @return scaling factors where row 0 is frist and row 1 is second
     */
    public double[][] getScalingFactors();
    
    /**
     * Compute and set this Scale's scaling 
     * factors from a formatted set of data.
     * @param data formatted data
     */
    public void computeScalingFactors(double data[][]);
    
    /**
     * Scale data down.
     * @param data data to be scaled
     * @return scaled data
     */
    public double[] scaleDown(double data[]);
    
    /**
     * Scale data up.
     * @param data data to be scaled
     * @return scaled data
     */
    public double[] scaleUp(double data[]);
}
