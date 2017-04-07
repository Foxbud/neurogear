package neurogear.base.cost;

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
 * the cost function. Implementations should also be final.
 */
public interface Cost {
    
    /**
     * The derivative of the cost function.
     * @param x activation value
     * @param y target value
     * @return f'(x)
     */
    public double df(double x, double y);
}
