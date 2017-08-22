package com.foxbud.neurogear.data.datum;

/**
 * Class to hold a single unit of data 
 * and the corresponding label.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Datum.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Used to abstract the conecpt of 
 * training and testing data in neural networks.
 */
public final class Datum {
    
    // MEMBER VARIABLES.
    
    // Raw information where rows are channels and columns are locations.
    private final double raw[][];
    
    // Label for raw information where rows are channels and columns are locations.
    private final double label[][];
    
    // MEMBER METHODS.
    
    /**
     * Construct a Datum with passed information.
     * @param rawP raw datum values
     * @param labelP label for raw values
     */
    public Datum(double rawP[][], double labelP[][]) {
    
        raw = rawP;
        label = labelP;
    }
    
    /**
     * Return this Datum's raw values.
     * @return raw datum values
     */
    public double[][] getRaw() {
    
        return raw;
    }
    
    /**
     * Return this Datum's label values.
     * @return label for raw values
     */
    public double[][] getLabel() {
    
        return label;
    }
}
