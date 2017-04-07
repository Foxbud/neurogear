package neurogear.base.activation;

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
     * @param x value
     * @return f(x) = x
     */
    @Override
    public double f(double x) {
    
        return x;
    }
    
    /**
     * Derivative of identity activation function.
     * @param x value
     * @return f'(x) = 1
     */
    @Override
    public double df(double x) {
    
        return 1.0;
    }
}
