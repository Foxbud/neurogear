package com.foxbud.neurogear.base.cost;

/**
 * Interface for implementing cost functions.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Cost.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Implementations must specify df(), the derivative of 
 * the cost function. Implementations should be final.
 */
public interface Cost {
    
    /**
     * The derivative of the cost function.
     * @param activation activation value
     * @param target target value
     * @return f'(activation, target)
     */
    public double df(double activation, double target);
}
