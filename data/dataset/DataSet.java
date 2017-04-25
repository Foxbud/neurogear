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
    
    // Size of shuffle buffer.
    private int sBufferSize;
    
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
     * @throws InvalidDatumException if parameter 'datum' is of incorrect type
     */
    public void addDatum(Datum datum) {
    
        // Test for exception.
        if (!(datum instanceof Datum)) {
        
            throw new InvalidDatumException("'datum' must be of type 'Datum'");
        }
        else {
        
            data.add(datum);
        }
    }
    
    /**
     * Remove a Datum from this DataSet 
     * (note that item will still be safely 
     * removed even if it is in the shuffle buffer).
     * @param datum Datum to remove
     * @return false if parameter 'datum' could not be found
     * @throws InvalidDatumException if parameter 'datum' is of incorrect type
     */
    public boolean removeDatum(Datum datum) {
    
        // Test for exception.
        if (!(datum instanceof Datum)) {
        
            throw new InvalidDatumException("'datum' must be of type 'Datum'");
        }
        else {
        
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
     */
    public boolean hasNextBuffer(int quantity) {
    
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
        else {
        
            // Randomly select next shuffle index.
            int nextIndex = generator.nextInt(sBufferSize);

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
}
