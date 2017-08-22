package com.foxbud.neurogear.base.regularization;

/**
 * Null implementation of Regularization.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NullRegularization.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: This is the implementation for no
 * regularization.
 */
public final class NullRegularization implements Regularization {
    
    /**
     * Derivative of null regularization.
     * @param regParameter regularization parameter
     * @param weight weight value
     * @return f'(regParameter, weight) = 0.0
     */
    @Override
    public double df(double regParameter, double weight) {
    
        return 0.0;
    }
}
