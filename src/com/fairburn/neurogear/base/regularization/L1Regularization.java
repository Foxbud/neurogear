package com.fairburn.neurogear.base.regularization;

/**
 * L1 implementation of Regularization.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: L1Regularization.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for L1
 * regularization.
 */
public final class L1Regularization implements Regularization {
    
    /**
     * Derivative of L1 regularization.
     * @param regParameter regularization parameter
     * @param weight weight value
     * @return f'(regParameter, weight) = regParameter * sign(weight)
     */
    @Override
    public double df(double regParameter, double weight) {
    
        // Return the regularization parameter times the sign of the weight value.
        if (weight > 0.0) {
        
            return regParameter;
        }
        else if (weight < 0.0) {
        
            return -regParameter;
        }
        else {
        
            return 0.0;
        }
    }
}
