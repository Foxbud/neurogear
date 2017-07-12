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
    
    // MEMBER VARIABLES.
    
    // Magnitude at which x for f(x) is overridden.
    private static final double BOUNDARY = 10.0;
    
    // Positive f(x) override value.
    private static final double POS_OVERRIDE = 1.0 / (1.0 + Math.exp(-BOUNDARY));
    // Negative f(x) override value.
    private static final double NEG_OVERRIDE = 1.0 / (1.0 + Math.exp(BOUNDARY));
    
    // MEMBER METHODS.
    
    /**
     * Logistic activation function.
     * @param sum sum value
     * @return f(sum) = 1.0 / (1.0 + e ^ -sum)
     */
    @Override
    public double f(double sum) {
    
        // Test for boundary override.
        if (sum >= BOUNDARY) {
        
            return POS_OVERRIDE;
        }
        else if (sum <= -BOUNDARY) {
        
            return NEG_OVERRIDE;
        }
        
        // No override.
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
