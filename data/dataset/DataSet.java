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
    
    // Data.
    private final ArrayList<Datum> data;
    
    // Shuffle buffer tail pointer.
    private Datum sBufferPointer;
    
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
     * Populate this DataSet with formatted data 
     * from a file (note that items will not appear in 
     * shuffle buffer until 'resetBuffer()' is called).
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
    
        // Search for datum.
        int datumIndex = data.indexOf(datum);
        
        // Check for datum.
        if (datumIndex > -1) {
        
            // Check if datum is shuffle buffer tail.
            if (datum == sBufferPointer) {
            
                // Update sBufferPointer to point at new tail.
                if (datumIndex == 0) {
                    
                    sBufferPointer = null;
                }
                else {
                    
                    sBufferPointer = data.get(datumIndex - 1);
                }
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
    }
    
    /**
     * Reset shuffle buffer for a new batch.
     */
    public void resetBuffer() {
    
        // Point sBufferPointer at the last element of data.
        if (data.isEmpty()) {
        
            sBufferPointer = null;
        }
        else {
            
            sBufferPointer = data.get(data.size() - 1);
        }
    }
    
    /**
     * Determine if this DataSet has at least
     * a certain quantity left in the shuffle buffer.
     * @param quantity quantity to check
     * @return shuffle buffer size >= quantity
     */
    public boolean hasNextBuffer(int quantity) {
    
        // Test for empty DataSet.
        if (data.isEmpty()) {
        
            return false;
        }
        else {
        
            return data.indexOf(sBufferPointer) + 1 >= quantity;
        }
    }
    
    /**
     * Perform single step of Fisher-Yates shuffle
     * to retrieve next Datum from shuffle buffer.
     * @return random Datum
     */
    public Datum getNextBuffer() {
    
        // Get index of sBufferPointer.
        int tailIndex = data.indexOf(sBufferPointer);
        
        // Randomly select next shuffle index.
        int nextIndex = generator.nextInt(tailIndex + 1);
        
        // Store Datum at nextIndex.
        Datum nextDatum = data.get(nextIndex);
        
        // Swap end of shuffle buffer with nextIndex.
        data.set(nextIndex, sBufferPointer);
        data.set(tailIndex, nextDatum);
        
        // Update sBufferPointer to point at new tail.
        sBufferPointer = data.get(tailIndex - 1);
        
        return nextDatum;
    }
}
