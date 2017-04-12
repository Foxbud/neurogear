package neurogear.base.cost;

/**
 * Quadratic implementation of Cost.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: QuadraticCost.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the quadratic
 * cost function.
 */
public final class QuadraticCost implements Cost {
    
    /**
     * Derivative of the quadratic cost function.
     * @param activation activation value
     * @param target target value
     * @return f'(activation, target) = activation - target
     */
    @Override
    public double df(double activation, double target) {
    
        return activation - target;
    }
}
