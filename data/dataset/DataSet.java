package neurogear.data.dataset;

import java.util.Random;
import neurogear.data.datum.Datum;

/**
 * BRIEF CLASS DESCRIPTION.
 * 
 * @author Garrett Russell Fairburn
 * @version X.X
 * File: FILE_NAME.java
 * Created: XX/XX/XX
 * Copyright (c) 20XX, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: DETAILED CLASS DESCRIPTION.
 */
public final class DataSet {
    
    // MEMBER VARIABLES.
    
    // Data.
    private final Datum data[];
    // Shuffle of indices of elements in data.
    private final Integer shuffle[];
    
    // Amount of valid data left in shuffle.
    private int remainingShuffle;
    
    // PRNG for shuffling data.
    private final Random generator;
    
    // MEMBER METHODS.
    
    DataSet(Datum dataP[], int seed) {
    
        data = dataP.clone();
        shuffle = new Integer[data.length];
        
        // Initialize shuffle with indices.
        for (int i = 0; i < shuffle.length; i++) {
        
            shuffle[i] = i;
        }
        
        reset();
        
        generator = new Random(seed);
    }
    
    public void reset() {
    
        remainingShuffle = shuffle.length;
    }
    
    public Datum[] getData() {
    
        return data.clone();
    }
    
    public boolean hasNext(int numData) {
    
        return remainingShuffle >= numData;
    }
    
    public Datum getNext() {
    
        // Index of next shuffle element to retrieve.
        int nextIndex = generator.nextInt(remainingShuffle);
        
        // Retrieve contents of nextIndex.
        int nextDatum = shuffle[nextIndex];
        
        // Swap indices in shuffle.
        shuffle[nextIndex] = shuffle[remainingShuffle - 1];
        shuffle[remainingShuffle - 1] = nextDatum;
        
        // Decrement total number of shuffle elements remaining.
        remainingShuffle--;
        
        // Return chosen Datum.
        return data[nextDatum];
    }
}
