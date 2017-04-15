package neurogear.data.datum;

/**
 * Subclass of Datum for labeled information.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: LabeledDatum.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Raw piece of information which
 * has been assigned a meaningful label.
 */
public final class LabeledDatum extends Datum {
    
    // MEMBER VARIABLES.
    
    // Label for raw datum values.
    private final Double label[];
    
    // MEMBER METHODS.
    
    /**
     * Construct a LabeledDatum with copies of passed information.
     * @param rawP raw datum values
     * @param labelP label for raw datum values
     * @throws InvalidLabelException if parameter 'labelP' is of incorrect type or is empty
     */
    LabeledDatum(Double rawP[], Double labelP[]) {
    
        super(rawP);
        
        // Test for exceptions.
        if (!(labelP instanceof Double[])) {
        
            throw new InvalidLabelException("'labelP' must be of type 'Double[]'");
        }
        else if (labelP.length == 0) {
        
            throw new InvalidLabelException("'labelP' must not be empty");
        }
        else {
        
            label = labelP.clone();
        }
    }
    
    /**
     * Return copy of this Datum's label.
     * @return label for raw datum values
     */
    public Double[] getLabel() {

        return label.clone();
    }
    
    /**
     * Return copy of this Datum with the label
     * stripped off.
     * @return unlabeled copy of this Datum
     */
    public UnlabeledDatum unlabel() {
    
        return new UnlabeledDatum(raw);
    }
}
