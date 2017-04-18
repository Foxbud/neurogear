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
     * Return this Scale's scaling factors.
     * @return scaling factors
     */
    public Double[][] getScalingFactors();
    
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
