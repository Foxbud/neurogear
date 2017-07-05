package neurogear.data.dataset;

import java.util.Random;
import java.util.ArrayList;
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
public final class DataSet {
    
    // MEMBER VARIABLES.
    
    // Data.
    private final ArrayList<Datum> data;
    
    // Size of shuffle buffer.
    private int sBufferSize;
    
    // PRNG for shuffling.
    private final Random PRNG;
    
    // MEMBER METHODS.
    
    /**
     * Construct a DataSet with passed PRNG seed.
     * @param seed PRNG seed
     */
    public DataSet(int seed) {
    
        data = new ArrayList<>();
        
        // Initialize shuffleBuffer.
        resetBuffer();
        
        PRNG = new Random(seed);
    }
    
    /**
     * Return an array containing all this DataSet's data.
     * @return data
     */
    public Datum[] getData() {
    
        return data.toArray(new Datum[data.size()]);
    }
    
    /**
     * Return an array containing all this 
     * DataSet's raw data.
     * @return raw data
     */
    public double[][][] presentRaw() {
    
        // Raw data array.
        double rawData[][][] = new double[data.size()][][];
        
        // Get all raw data.
        for (int i = 0; i < data.size(); i++) {
        
            rawData[i] = data.get(i).getRaw();
        }
        
        return rawData;
    }
    
    /**
     * Return an array containing all this 
     * DataSet's label data.
     * @return label data
     */
    public double[][][] presentLabel() {
    
        // Label data array.
        double labelData[][][] = new double[data.size()][][];
        
        // Get all label data.
        for (int i = 0; i < data.size(); i++) {
        
            labelData[i] = data.get(i).getLabel();
        }
        
        return labelData;
    }
    
    /**
     * Return total amount of data contained in 
     * this DataSet.
     * @return amount of data
     */
    public int size() {
    
        return data.size();
    }
    
    /**
     * Return whether there this no data contained
     * in this DataSet.
     * @return size() == 0
     */
    public boolean isEmpty() {
    
        return data.isEmpty();
    }
    
    /**
     * Add a Datum to this DataSet (note that 
     * item will not appear in shuffle buffer
     * until 'resetBuffer()' is called).
     * @param datum Datum to add
     * @throws InvalidDatumException if parameter 'datum' is null
     */
    public void addDatum(Datum datum) {
    
        // Test for exception.
        if (datum == null) {
        
            throw new InvalidDatumException("'datum' must not be null");
        }
        
        data.add(datum);
    }
    
    /**
     * Remove a Datum from this DataSet 
     * (note that item will still be safely 
     * removed even if it is in the shuffle buffer).
     * @param datum Datum to remove
     * @return false if parameter 'datum' could not be found
     * @throws InvalidDatumException if parameter 'datum' is null
     */
    public boolean removeDatum(Datum datum) {
    
        // Test for exception.
        if (datum == null) {
        
            throw new InvalidDatumException("'datum' must not be null");
        }

        // Search for datum.
        int datumIndex = data.indexOf(datum);

        // Check for datum.
        if (datumIndex > -1) {

            // Check if datum is in shuffle buffer.
            if (datumIndex < sBufferSize) {

                // Update new shuffle buffer size.
                sBufferSize--;
            } 

            // Remove datum from data array list.
            data.remove(datumIndex);

            return true;
        }
        else {

            return false;
        }
    }
    
    /**
     * Remove all data from this DataSet.
     */
    public void clear() {
    
        data.clear();
        
        resetBuffer();
    }
    
    /**
     * Reset shuffle buffer for a new batch.
     */
    public void resetBuffer() {
        
        // Reset shuffle buffer size.
        sBufferSize = data.size();
    }
    
    /**
     * Determine if this DataSet has at least
     * a certain quantity left in the shuffle buffer.
     * @param quantity quantity to check
     * @return shuffle buffer size >= quantity
     * @throws NegativeQuantityException if parameter 'quantity' is negative
     */
    public boolean hasNextBuffer(int quantity) {
    
        // Test for exception.
        if (quantity < 0) {
        
            throw new NegativeQuantityException("cannot test for negative shuffle buffer quantity");
        }
        
        return sBufferSize >= quantity;

    }
    
    /**
     * Perform single step of Fisher-Yates shuffle
     * to retrieve next Datum from shuffle buffer.
     * @return random Datum
     * @throws EmptyBufferException if shuffle buffer is empty prior to method call
     */
    public Datum getNextBuffer() {
        
        // Test for exception.
        if (sBufferSize == 0) {
        
            throw new EmptyBufferException("shuffle buffer was empty, consider using 'resetBatch()'");
        }

        // Randomly select next shuffle index.
        int nextIndex = PRNG.nextInt(sBufferSize);

        // Store Datum at nextIndex.
        Datum nextDatum = data.get(nextIndex);

        // Swap end of shuffle buffer with nextIndex.
        data.set(nextIndex, data.get(sBufferSize - 1));
        data.set(sBufferSize - 1, nextDatum);

        // Update new shuffle buffer size.
        sBufferSize--;

        return nextDatum;
    }
}
