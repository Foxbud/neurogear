package neurogear.base.activation;

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
     * @param x value
     * @return f(x) = x / (1 + |x|)
     */
    @Override
    public double f(double x) {
    
        return x / (1 + Math.abs(x));
    }
    
    /**
     * Derivative of softsign activation function.
     * @param x value
     * @return f'(x) = 1 / (1 + |x|) ^ 2
     */
    @Override
    public double df(double x) {
    
        return 1 / ((1 + Math.abs(x)) * (1 + Math.abs(x)));
    }
}
