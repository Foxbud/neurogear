package com.foxbud.neurogear.base.activation;

/**
 * Hyperbolic tangent implementation of Activation.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: TanHActivation.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the hyperbolic tangent
 * activation function.
 */
public final class TanHActivation implements Activation {
    
    /**
     * Hyperbolic tangent activation function.
     * @param sum sum value
     * @return f(sum) = tanh(sum)
     */
    @Override
    public double f(double sum) {
    
        return Math.tanh(sum);
    }
    
    /**
     * Derivative of hyperbolic tangent activation function.
     * @param sum sum value
     * @return f'(sum) = 1.0 - tanh(sum) ^ 2
     */
    @Override
    public double df(double sum) {
    
        return 1.0 - Math.tanh(sum) * Math.tanh(sum);
    }
}
