package com.fairburn.neurogear.base.activation;

/**
 * Rectified linear unit implementation of Activation.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: ReLUActivation.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the rectified
 * linear unit activation function.
 */
public final class ReLUActivation implements Activation {
    
    /**
     * Rectified linear unit activation function.
     * @param sum sum value
     * @return f(sum) = sum for sum >= 0.0; 0.0 otherwise
     */
    @Override
    public double f(double sum) {
    
        if (sum < 0.0) {
        
            return 0.0;
        }
        else {
        
            return sum;
        }
    }
    
    /**
     * Derivative of rectified linear unit activation function.
     * @param sum sum value
     * @return f'(sum) = 1.0 for sum >= 0.0; 0.0 otherwise
     */
    @Override
    public double df(double sum) {
    
        if (sum < 0.0) {
        
            return 0.0;
        }
        else {
        
            return 1.0;
        }
    }
}
