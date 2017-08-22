package com.foxbud.neurogear.data.scale;

/**
 * Null implementation of Scale.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NullScale.java
 * Created: 07/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Identity implementation (null) of
 * Scale. Does not rescale values.
 */
public final class NullScale implements Scale {
    
    // MEMBER METHODS.
    
    /**
     * Construct a NormalScale.
     */
    public NullScale() {
        
        // Do nothing.
    }
    
    /**
     * Here for compatability; is an empty method.
     * @param nullA unused parameter
     * @param nullB unused parameter
     */
    @Override
    public void setScalingFactors(double nullA[][], double nullB[][]) {
    
        // Do nothing.
    }
    
    /**
     * Return empty scaling factors.
     * @return empty scaling factors
     */
    @Override
    public double[][][] getScalingFactors() {
    
        // Temporary array for holding return values.
        double tempFactors[][][] = new double[2][0][0];
        
        return tempFactors;
    }
    
    /**
     * Here for compatability; is an empty method.
     * @param data formatted data
     */
    @Override
    public void computeScalingFactors(double data[][][]) {
    
        // Do nothing.
    }
    
    /**
     * Return passed data without scaling it.
     * @param data data to be scaled
     * @return scaled data
     */
    @Override
    public double[][] scaleDown(double data[][]) {
    
        return data;
    }
    
    /**
     * Return passed data without scaling it.
     * @param data data to be scaled
     * @return scaled data
     */
    @Override
    public double[][] scaleUp(double data[][]) {
    
        return data;
    }
}
