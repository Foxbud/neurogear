package com.fairburn.neurogear.base.activation;

/**
 * Softsign implementation of Activation.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: SoftsignActivation.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the softsign
 * activation function.
 */
public final class SoftsignActivation implements Activation {
    
    /**
     * Softsign activation function.
     * @param sum sum value
     * @return f(sum) = sum / (1.0 + |sum|)
     */
    @Override
    public double f(double sum) {
    
        return sum / (1.0 + Math.abs(sum));
    }
    
    /**
     * Derivative of softsign activation function.
     * @param sum sum value
     * @return f'(sum) = 1.0 / (1.0 + |sum|) ^ 2
     */
    @Override
    public double df(double sum) {
    
        return 1.0 / ((1.0 + Math.abs(sum)) * (1.0 + Math.abs(sum)));
    }
}
