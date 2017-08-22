package com.foxbud.neurogear.base.activation;

/**
 * Identity implementation of Activation.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: IdentityActivation.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the identity
 * activation function.
 */
public final class IdentityActivation implements Activation {
    
    /**
     * Identity activation function.
     * @param sum sum value
     * @return f(sum) = sum
     */
    @Override
    public double f(double sum) {
    
        return sum;
    }
    
    /**
     * Derivative of identity activation function.
     * @param sum sum value
     * @return f'(sum) = 1.0
     */
    @Override
    public double df(double sum) {
    
        return 1.0;
    }
}
