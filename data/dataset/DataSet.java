package neurogear.data.dataset;

import java.util.Random;
import neurogear.data.datum.Datum;

/**
 * Container for managing data.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: DataSet.java
 * Created: 04/17/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Allows easier interfacig between neural
 * networks and individual instances of Datum.
 */
public class DataSet {
    
    // MEMBER VARIABLES.
    
    // Primary data.
    private final Datum data[];
    
    // Shuffle of the indices of the data.
    private final Integer indexShuffle[];
    // Effective size of indexShuffle.
    private int shuffleSize;
    
    // PRNG for shuffling.
    private final Random generator;
    
    // MEMBER METHODS.
    
    /**
     * Construct a DataSet with passed PRNG seed and copy of dataP.
     * @param dataP data
     * @param seed PRNG seed
     */
    DataSet(Datum dataP[], int seed) {
    
        data = dataP.clone();
        
        // Initialize indexShuffle with the indices of data array.
        indexShuffle = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
        
            indexShuffle[i] = i;
        }
        shuffleSize = indexShuffle.length;
        
        generator = new Random(seed);
    }
    
    /**
     * Return copy of this DataSet's internal data.
     * @return data
     */
    public Datum[] getData() {
    
        return data.clone();
    }
    
    /**
     * Reset shuffle buffer of the 
     * internal data for a new batch.
     */
    public void resetBatch() {
    
        shuffleSize = indexShuffle.length;
    }
    
    /**
     * Determine if this DataSet has at least
     * a certain quantity left in the shuffle buffer.
     * @param quantity quantity to check
     * @return shuffleSize >= quantity
     */
    public boolean hasNextQuantity(int quantity) {
    
        return shuffleSize >= quantity;
    }
    
    /**
     * Perform single step of Fisher-Yates shuffle
     * to retrieve next Datum from shuffle buffer.
     * @return random Datum
     */
    public Datum getNext() {
    
        // Randomly select next shuffle index.
        int nextShuffleIndex = generator.nextInt(shuffleSize - 1);
        
        // Store data index at that shuffle index.
        int dataIndex = indexShuffle[nextShuffleIndex];
        
        // Swap contents of end of shuffle buffer with new shuffle index.
        indexShuffle[nextShuffleIndex] = indexShuffle[shuffleSize - 1];
        indexShuffle[shuffleSize - 1] = dataIndex;
        
        shuffleSize--;
        
        return data[dataIndex];
    }
}
