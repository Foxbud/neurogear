package neurogear.data.dataset;

import java.util.Random;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import neurogear.data.datum.Datum;
import neurogear.data.datum.UnlabeledDatum;
import neurogear.data.datum.LabeledDatum;

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
    
    // Primary data.
    private final ArrayList<Datum> data;
    
    // Shuffle of the indices of the data.
    private Integer[] shuffleBuffer;
    // Effective size of shuffleBuffer.
    private int shuffleSize;
    
    // PRNG for shuffling.
    private final Random generator;
    
    // MEMBER METHODS.
    
    /**
     * Construct a DataSet with passed PRNG seed.
     * @param seed PRNG seed
     */
    DataSet(int seed) {
    
        data = new ArrayList<>();
        
        // Initialize indexShuffle.
        resetBatch();
        
        generator = new Random(seed);
    }
    
    /**
     * Format and save all data in this DataSet to a file.
     * @param fileName file name with path
     * @throws java.io.IOException if parameter 'fileName' causes a file error
     */
    public void saveToFile(String fileName) throws java.io.IOException {
    
        // File buffer.
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        
        // Write all data to file.
        for (Datum currDatum : data) {
        
            // Determine Datum type.
            if (currDatum instanceof UnlabeledDatum) {
            
                // Write raw size.
                out.write(currDatum.getRaw().length);
                out.newLine();
                
                // Write Datum raw elements.
                for (Double datumElement : currDatum.getRaw()) {
                
                    out.write(Double.toString(datumElement) + " ");
                }
                out.newLine();
            }
            else if (currDatum instanceof LabeledDatum) {
            
                // Write raw and label size.
                out.write(currDatum.getRaw().length + " " + ((LabeledDatum)currDatum).getLabel().length);
                out.newLine();
                
                // Write Datum raw elements.
                for (Double datumElement : currDatum.getRaw()) {
                
                    out.write(Double.toString(datumElement) + " ");
                }
                out.newLine();
                
                // Write Datum label elements.
                for (Double datumElement : ((LabeledDatum)currDatum).getLabel()) {
                
                    out.write(Double.toString(datumElement) + " ");
                }
                out.newLine();
            }
            
            // Flush file buffer.
            out.flush();
        }
        
        // Close file buffer.
        out.close();
    }
    
    /**
     * Return reference to this DataSet's internal data.
     * @return data
     */
    public ArrayList<Datum> getData() {
    
        return data;
    }
    
    /**
     * Add a datum to this DataSet (note that 
     * it will not appear in shuffle buffer
     * until 'resetBatch()' is called).
     * @param datum datum to add
     */
    public void addDatum(Datum datum) {
    
        data.add(datum);
    }
    
    /**
     * Reset shuffle buffer for a new batch.
     */
    public void resetBatch() {
    
        // Initialize indexShuffle with the indices of data array list.
        shuffleBuffer = new Integer[data.size()];
        for (int i = 0; i < data.size(); i++) {
        
            shuffleBuffer[i] = i;
        }
        shuffleSize = shuffleBuffer.length;
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
        int dataIndex = shuffleBuffer[nextShuffleIndex];
        
        // Swap contents of end of shuffle buffer with new shuffle index.
        shuffleBuffer[nextShuffleIndex] = shuffleBuffer[shuffleSize - 1];
        shuffleBuffer[shuffleSize - 1] = dataIndex;
        
        shuffleSize--;
        
        return data.get(dataIndex);
    }
}
