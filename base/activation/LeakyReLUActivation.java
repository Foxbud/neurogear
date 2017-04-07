package neurogear.base.activation;

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
     * @param x value
     * @return f(x) = x for x >= 0.0; 0.01 * x otherwise
     */
    @Override
    public double f(double x) {
    
        if (x < 0.0) {
        
            return 0.01 * x;
        }
        else {
        
            return x;
        }
    }
    
    /**
     * Derivative of leaky rectified linear unit activation function.
     * @param x value
     * @return f'(x) = 1.0 for x >= 0.0; 0.01 otherwise
     */
    @Override
    public double df(double x) {
    
        if (x < 0.0) {
        
            return 0.01;
        }
        else {
        
            return 1.0;
        }
    }
}
