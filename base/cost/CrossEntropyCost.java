package neurogear.base.cost;

/**
 * Cross-entropy implementation of Cost.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: CrossEntropyCost.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for the cross-entropy
 * cost function.
 */
public final class CrossEntropyCost implements Cost {
    
    /**
     * Derivative of the cross-entropy cost function.
     * @param x activation value
     * @param y target value
     * @return f'(x, y) = x - y
     */
    @Override
    public double df(double x, double y) {
    
        return ((1 - y) / (1 - x)) - (y / x);
    }
}
