package neurogear.data.dataset;

import java.util.Random;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;
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
    public DataSet(int seed) {
    
        data = new ArrayList<>();
        
        // Initialize shuffleBuffer.
        resetBuffer();
        
        generator = new Random(seed);
    }
    
    /**
     * Format and save all data in this DataSet to a file.
     * @param fileName file name with path
     * @throws java.io.IOException if parameter 'fileName' causes a file exception
     */
    public void saveToFile(String fileName) throws java.io.IOException {
    
        // File buffer.
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        
        // Write all data to file.
        for (Datum currDatum : data) {
         
            // Write Datum raw elements.
            for (Double datumElement : currDatum.getRaw()) {

                out.write(Double.toString(datumElement) + " ");
            }
            out.newLine();

            // Check for LabeledDatum.
            if (currDatum instanceof LabeledDatum) {
                
                // Write Datum label elements.
                for (Double datumElement : ((LabeledDatum)currDatum).getLabel()) {
                
                    out.write(Double.toString(datumElement) + " ");
                }
                out.newLine();
            }
            
            // Separate Datum with new line.
            out.newLine();
            
            // Flush file buffer.
            out.flush();
        }
        
        // Close file buffer.
        out.close();
    }
    
    /**
     * Populate this DataSet with formatted data from a file.
     * @param fileName file name with path
     * @throws java.io.FileNotFoundException if parameter 'fileName' does not represent a valid file
     */
    public void loadFromFile(String fileName) throws java.io.FileNotFoundException {
    
        // File scanner.
        Scanner in = new Scanner(new File(fileName));
        
        // Set delimeter to a single space.
        in.useDelimiter(" ");
        
        // Read all data from file.
        while (in.hasNextLine()) {
            
            // Temporary ArrayList for raw elements.
            ArrayList<Double> tempRaw = new ArrayList<>();
            
            // Get raw elements.
            while (in.hasNextDouble()) {
            
                tempRaw.add(in.nextDouble());
            }
            
            // Skip a line.
            in.nextLine();
            
            // Temporary ArrayList for label elements.
            ArrayList<Double> tempLabel = new ArrayList<>();
            
            // Check for label elements.
            if (in.hasNextDouble()) {
            
                // Get label elements.
                while (in.hasNextDouble()) {
                
                    tempLabel.add(in.nextDouble());
                }
                
                // Skip a line.
                in.nextLine();
            }
            
            // Skip a line.
            in.nextLine();
            
            // Create and add Datum.
            if (tempLabel.isEmpty()) {
            
                data.add(new UnlabeledDatum(tempRaw.toArray(new Double[tempRaw.size()])));
            }
            else {
            
                data.add(new LabeledDatum(tempRaw.toArray(new Double[tempRaw.size()]), tempLabel.toArray(new Double[tempLabel.size()])));
            }
        }
        
        // Close file scanner.
        in.close();
    }
    
    /**
     * Return an array containing all this DataSet's data.
     * @return data
     */
    public Datum[] getData() {
    
        return data.toArray(new Datum[data.size()]);
    }
    
    /**
     * Add a Datum to this DataSet (note that 
     * item will not appear in shuffle buffer
     * until 'resetBuffer()' is called).
     * @param datum Datum to add
     */
    public void addDatum(Datum datum) {
    
        data.add(datum);
    }
    
    /**
     * Remove a Datum from this DataSet 
     * (note that item will still be safely 
     * removed even if it is in the shuffle buffer).
     * @param datum Datum to remove
     * @return false if parameter 'datum' could not be found
     */
    public boolean removeDatum(Datum datum) {
    
        // Search for datum in data array list.
        int datumIndex = data.indexOf(datum);
        
        // Check if item was in data.
        if (datumIndex > -1) {
        
            // Search for datumIndex in shuffle buffer.
            for (int i = 0; i < shuffleBuffer.length; i++) {
            
                // Check for match.
                if (shuffleBuffer[i] == datumIndex) {
                
                    // Check if in effective buffer.
                    if (i < shuffleSize) {
                    
                        // Move contents at end of buffer to i.
                        shuffleBuffer[i] = shuffleBuffer[shuffleSize - 1];
                        
                        shuffleSize--;
                    }
                    
                    // Remove datum from data array list.
                    data.remove(datum);
                    
                    break;
                }
            }
            
            return true;
        }
        else {
        
            return false;
        }
    }
    
    /**
     * Reset shuffle buffer for a new batch.
     */
    public void resetBuffer() {
    
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
        
        // Move contents at end of buffer to nextShuffleIndex.
        shuffleBuffer[nextShuffleIndex] = shuffleBuffer[shuffleSize - 1];
        
        shuffleSize--;
        
        return data.get(dataIndex);
    }
}
