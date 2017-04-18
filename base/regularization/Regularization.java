package neurogear.base.regularization;

/**
 * Interface for implementing regularization.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Regularization.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Implementations must specify df(), the derivative of 
 * the regularization. Implementations should be final.
 */
public interface Regularization {
    
    /**
     * The derivative of the regularization.
     * @param regParameter regularization parameter
     * @param weight weight value
     * @return f'(regParameter, weight)
     */
    public double df(double regParameter, double weight);
}
