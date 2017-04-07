package neurogear.base.activation;

/**
 * Interface for implementing activation functions.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Activation.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Implementations must specify f(), the activation function
 * itself, and df(), the derivative of that function. Implementations
 * should also be final.
 */
public interface Activation {
    
    /**
     * The activation function.
     * @param x value
     * @return f(x)
     */
    public double f(double x);
    
    /**
     * The derivative of the activation function.
     * @param x value
     * @return f'(x)
     */
    public double df(double x);
}
