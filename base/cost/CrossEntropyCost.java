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
     * @return f'(x, y) = ((1 - y) / (1 - x)) - (y / x)
     * @throws neurogear.base.cost.CostException
     */
    @Override
    public double df(double x, double y) throws CostException {
    
        // Test for exceptions.
        if (x > 1.0 || x < 0.0) {
        
            throw new CrossEntropyException("'x' must be in the range 0.0 to 1.0");
        }
        else if (y > 1.0 || y < 0.0) {
        
            throw new CrossEntropyException("'y' must be in the range 0.0 to 1.0");
        }
        else {
        
         return ((1 - y) / (1 - x)) - (y / x);   
        }
    }
}
