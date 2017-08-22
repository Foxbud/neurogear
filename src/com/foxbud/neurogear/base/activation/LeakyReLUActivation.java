package com.foxbud.neurogear.base.activation;

/**
 * Leaky rectified linear unit implementation of Activation.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: LeakyReLUActivation.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the leaky
 * rectified linear unit activation function.
 */
public final class LeakyReLUActivation implements Activation {
    
    /**
     * Leaky rectified linear unit activation function.
     * @param sum sum value
     * @return f(sum) = sum for sum >= 0.0; 0.01 * sum otherwise
     */
    @Override
    public double f(double sum) {
    
        if (sum < 0.0) {
        
            return 0.01 * sum;
        }
        else {
        
            return sum;
        }
    }
    
    /**
     * Derivative of leaky rectified linear unit activation function.
     * @param sum sum value
     * @return f'(sum) = 1.0 for sum >= 0.0; 0.01 otherwise
     */
    @Override
    public double df(double sum) {
    
        if (sum < 0.0) {
        
            return 0.01;
        }
        else {
        
            return 1.0;
        }
    }
}
