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
     * @param sum sum value
     * @return f(sum) = 1.0 / (1.0 + e ^ -sum)
     */
    @Override
    public double f(double sum) {
    
        return 1.0 / (1.0 + Math.exp(-sum));
    }
    
    /**
     * Derivative of logistic activation function.
     * @param sum sum value
     * @return f'(sum) = 1.0 / (1.0 + e ^ -sum) * (1.0 - 1.0 / (1.0 + e ^ -sum))
     */
    @Override
    public double df(double sum) {
    
        return 1.0 / (1.0 + Math.exp(-sum)) * (1.0 - 1.0 / (1.0 + Math.exp(-sum)));
    }
}
