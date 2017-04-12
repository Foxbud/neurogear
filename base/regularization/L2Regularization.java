package neurogear.base.regularization;

/**
 * L2 implementation of Regularization.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: L2Regularization.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for L2
 * regularization.
 */
public final class L2Regularization implements Regularization {
    
    /**
     * Derivative of L2 regularization.
     * @param regParameter regularization parameter
     * @param weight weight value
     * @return f'(regParameter, weight) = regParameter * weight
     */
    @Override
    public double df(double regParameter, double weight) {
    
        return regParameter * weight;
    }
}
