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
    
    // String representation delimiters.
    private static final String ELEMENT_DELIM = "#";
    private static final String FIELD_DELIM = "@";
    
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
    
        // Open file.
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
            
            // Write all data to file.
            for (Datum currDatum : data) {
                
                // Write Datum to file.
                out.write(datumToString(currDatum));
                
                // Separate Datum with new line.
                out.newLine();
                
                // Flush file buffer.
                out.flush();
            }
        }
    }
    
    /**
     * Populate this DataSet with formatted data 
     * from a file (note that items will not appear in 
     * shuffle buffer until 'resetBuffer()' is called).
     * @param fileName file name with path
     * @throws java.io.FileNotFoundException if parameter 'fileName' does not represent a valid file
     * @throws FileFormatException if file 'fileName' contains incorrectly formatted data
     */
    public void loadFromFile(String fileName) throws java.io.FileNotFoundException {
    
        // Open file.
        try (Scanner in = new Scanner(new File(fileName))) {
            
            // Read all data from file.
            while (in.hasNextLine()) {
                
                // Add Datum to DataSet.
                addDatum(stringToDatum(in.nextLine()));
            }
        }
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
    
    // HELPER METHODS.
    
    /**
     * Return a String representation of the passed Double
     * array using the ELEMENT_DELIM.
     * @param elements data to convert
     * @return String representation
     */
    private String dataToString(Double elements[]) {
    
        // String to hold the representation.
        String dataRep = new String();
        
        // Write all elements to dataRep.
        for (Double i : elements) {
            
            // Write each element with delimiter.
            dataRep += i.toString() + ELEMENT_DELIM;
        }
        
        return dataRep;
    }
    
    /**
     * Return a String representation of the passed Datum
     * using the FIELD_DELIM.
     * @param datum Datum to convert
     * @return String representation
     */
    private String datumToString(Datum datum) {
    
        // Write raw elements and field delimiter.
        String datumRep = dataToString(datum.getRaw()) + FIELD_DELIM;
        
        // Check for label.
        if (datum instanceof LabeledDatum) {
        
            // Write label elements and field delimiter.
            datumRep += dataToString(((LabeledDatum)datum).getLabel()) + FIELD_DELIM;
        }
        
        return datumRep;
    }
    
    /**
     * Return a Double array from the passed String
     * representation using the ELEMENT_DELIM.
     * @param dataRep String to convert
     * @return Double array
     * @throws FileFormatException if parameter 'dataRep' contains incorrectly formatted data
     */
    private Double[] stringToData(String dataRep) {
    
        // ArrayList to hold the data elements.
        ArrayList<Double> elements = new ArrayList<>();
        
        try (Scanner parser = new Scanner(dataRep)) {
            
            parser.useDelimiter(ELEMENT_DELIM);
            
            // Read each element.
            while (parser.hasNextDouble()) {
                
                elements.add(parser.nextDouble());
            }
            
            // Test for exceptions.
            if (parser.hasNextLine()) {
            
                String endString = parser.nextLine();
                if (!endString.equals(ELEMENT_DELIM)) {

                    throw new FileFormatException("encountered invalid value in input file: '" + endString + "'");
                }
            }
            else {
            
                throw new FileFormatException("all input file values must be proceeded by '" + ELEMENT_DELIM + "'");
            }
        }
        
        // Create array to return.
        Double returnArray[] = new Double[elements.size()];
        
        return elements.toArray(returnArray);
    }
    
    /**
     * Return a Datum from the passed String
     * representation using the FIELD_DELIM.
     * @param datumRep String to convert
     * @return Datum
     * @throws FileFormatException if parameter 'datumRep' contains incorrectly formatted data
     */
    private Datum stringToDatum(String datumRep) {
    
        // ArrayList to hold the data arrays.
        ArrayList<Double[]> dataArrays = new ArrayList<>();
        
        try (Scanner parser = new Scanner(datumRep)) {
            
            parser.useDelimiter(FIELD_DELIM);
            
            // Read each data array.
            while (parser.hasNext()) {
                
                dataArrays.add(stringToData(parser.next()));
            }
            
            // Test for exception.
            if (!parser.hasNextLine()) {
            
                throw new FileFormatException("all input file fields must be proceeded by '" + FIELD_DELIM + "'");
            }
        }
        
        // Datum to return.
        Datum datum;
        
        // Create Datum.
        switch (dataArrays.size()) {
            
            case 1:
                
                datum = new UnlabeledDatum(dataArrays.get(0));
                break;
                
            case 2:
                
                datum = new LabeledDatum(dataArrays.get(0), dataArrays.get(1));
                break;
                
            default:
                
                throw new FileFormatException("all lines in input file must have either one or two fields");
        }
        
        return datum;
    }
}
