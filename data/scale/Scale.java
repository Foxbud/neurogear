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
    public void setScalingFactors(Double firstFactors[], Double secondFactors[]);
    
    /**
     * Return this Scale's scaling factors.
     * @return scaling factors where row 0 is frist and row 1 is second
     */
    public Double[][] getScalingFactors();
    
    /**
     * Compute and set this Scale's scaling 
     * factors from a formatted set of data.
     * @param data formatted data
     */
    public void computeScalingFactors(Double data[][]);
    
    /**
     * Scale data down.
     * @param data data to be scaled
     * @return scaled data
     */
    public Double[] scaleDown(Double data[]);
    
    /**
     * Scale data up.
     * @param data data to be scaled
     * @return scaled data
     */
    public Double[] scaleUp(Double data[]);
}
