package neurogear.base.activation;

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
     * @param x value
     * @return f(x) = tanh(x)
     */
    @Override
    public double f(double x) {
    
        return Math.tanh(x);
    }
    
    /**
     * Derivative of hyperbolic tangent activation function.
     * @param x value
     * @return f'(x) = 1 - tanh(x) ^ 2
     */
    @Override
    public double df(double x) {
    
        return 1.0 - Math.tanh(x) * Math.tanh(x);
    }
}
