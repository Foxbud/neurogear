package neurogear.base.activation;

/**
 * Logistic implementation of Activation.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: LogisticActivation.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the logistic
 * activation function.
 */
public final class LogisticActivation implements Activation {
    
    /**
     * Logistic activation function.
     * @param x value
     * @return f(x) = 1 / (1 + e ^ -x)
     */
    @Override
    public double f(double x) {
    
        return 1.0 / (1.0 + Math.exp(-x));
    }
    
    /**
     * Derivative of logistic activation function.
     * @param x value
     * @return f'(x) = 1 / (1 + e ^ -x) * (1 - 1 / (1 + e ^ -x))
     */
    @Override
    public double df(double x) {
    
        return 1.0 / (1.0 + Math.exp(-x)) * (1 - 1.0 / (1.0 + Math.exp(-x)));
    }
}
