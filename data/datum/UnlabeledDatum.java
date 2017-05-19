package neurogear.data.datum;

import java.util.Random;

/**
 * Subclass of Datum for unlabeled information.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: UnlabeledDatum.java
 * Created: 04/14/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Raw piece of information which
 * has NOT been assigned a meaningful label.
 */
public final class UnlabeledDatum extends Datum {
    
    // MEMBER METHODS.
    
    /**
     * Construct an UnlabeledDatum with copy of passed information.
     * @param rawP raw datum values
     */
    public UnlabeledDatum(Double rawP[]) {
    
        super(rawP);
    }
    
    /**
     * Return noisy copy of this Datum's raw datum values.
     * @param rate rate at which to add noise
     * @param generator seeded PRNG for adding noise
     * @return noisy raw datum values
     * @throws InvalidRandomException if parameter 'generator' is null
     * @throws BadRateException if parameter 'rate' falls outside the interval [0.0, 1.0]
     */
    public Double[] getNoisyRaw(double rate, Random generator) {
    
        // Test for exceptions.
        if (generator == null) {
        
            throw new InvalidRandomException("'generator' must not be null");
        }
        else if (rate > 1.0 || rate < 0.0) {
        
            throw new BadRateException("'rate' must be <= 1.0 and >= 0.0");
        }

        // Array to return.
        Double returnVals[] = raw.clone();

        // Introduce noise.
        if (rate > 0.0) {

            for (int i = 0; i < returnVals.length; i++) {

                // Determine whether to add noise.
                if (generator.nextDouble() <= rate) {

                    returnVals[i] = 0.0;
                }
            }
        }

        return returnVals;
    }
    
    /**
     * Return copy of this Datum with an
     * added label.
     * @param label label for raw datum values
     * @return labeled copy of this Datum
     */
    public LabeledDatum label(Double label[]) {
    
        return new LabeledDatum(raw, label);
    }
}
