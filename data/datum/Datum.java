package neurogear.data.datum;

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
    
    @Override
    public String toString() {
    
        // String representation to return.
        String returnString = "";
        
        // Write raw values.
        returnString += dataToString(raw);
        
        // Write label values.
        returnString += dataToString(label);
        
        return returnString;
    }
    
    // HELPER METHODS.
    
    private String dataToString(double data[][]) {
    
        // String representation to return.
        String returnString = "";
        
        // Write number of rows on first line.
        returnString += data.length + "\n";
        
        // Write each row's contents on a new line.
        for (int i = 0; i < data.length; i++) {
        
            for (int j = 0; j < data[i].length; j++) {
            
                returnString += data[i][j] + " ";
            }
            
            returnString += "\n";
        }
        
        return returnString;
    }
}
