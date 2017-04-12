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
     * @param activation activation value
     * @param target target value
     * @return f'(activation, target) = ((1 - target) / (1 - activation)) - (target / activation)
     * @throws neurogear.base.cost.CostException
     */
    @Override
    public double df(double activation, double target) throws CostException {
    
        // Test for exceptions.
        if (activation > 1.0 || activation < 0.0) {
        
            throw new CrossEntropyException("'activation' must be in the range 0.0 to 1.0");
        }
        else if (target > 1.0 || target < 0.0) {
        
            throw new CrossEntropyException("'target' must be in the range 0.0 to 1.0");
        }
        else {
        
         return ((1 - target) / (1 - activation)) - (target / activation);   
        }
    }
}
