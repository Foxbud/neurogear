package neurogear.utilities.kernel;

import neurogear.base.connection.BiasConnection;
import neurogear.base.connection.NodeConnection;
import neurogear.base.node.Node;
import neurogear.base.regularization.Regularization;

/**
 * Collection of Connections shared
 * by a group of Nodes in a Layer.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Kernel.java
 * Created: 05/19/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Used to search for a particular
 * feature within data in a location independent
 * manner.
 */
public final class Kernel {
    
    // MEMBER VARIABLES.
    
    // Node Connections which represent this Kernel.
    private final NodeConnection primaryConnections[];
    // Bias Connection for Kernel.
    private final BiasConnection biasConnection;
    // Offset of each NodeConnection from start index.
    private final int connectionOffsets[];
    
    // Kernel input Nodes.
    private Node inputNodes[];
    // Kernel output Nodes.
    private Node outputNodes[];
    
    // Stride length in indicies.
    private final int strideLength;
    
    // MEMBER METHODS.
    
    /**
     * Construct a Kernel with given parameters 
     * (will create implicit BiasConnection).
     * @param offsets each Connection's relative offset
     * @param weights each Connection's weight
     * @param strideLengthP number of input Nodes to step by for each output Node
     * @throws InvalidArrayException if parameters 'offsets' and/or 'weights' are null
     * @throws InvalidSizeException if parameters 'offsets' and/or 'strideLengthP' are invalid
     * @throws InconsistentSizeException if the sizes of parameters 'offsets' and 'weights' conflict
     */
    public Kernel(int offsets[], int weights[], int strideLengthP) {
    
        // Test for exceptions.
        if (offsets == null || weights == null) {
        
            throw new InvalidArrayException("parameters must not be null");
        }
        else if (offsets.length == 0) {
        
            throw new InvalidSizeException("'offsets' must not be empty");
        }
        else if (offsets.length != weights.length + 1) {
        
            throw new InconsistentSizeException("'offsets' must be one element less than 'weights'");
        }
        else if (strideLengthP <= 0) {
        
            throw new InvalidSizeException("'strideLengthP' must be greater than zero");
        }
        
        // Create connections array.
        primaryConnections = new NodeConnection[offsets.length];
        
        // Initialize all Connections.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            primaryConnections[i] = new NodeConnection(weights[i]);
        }
        biasConnection = new BiasConnection(weights[primaryConnections.length]);
        
        // Create and initialize offsets array.
        connectionOffsets = offsets.clone();
        
        strideLength = strideLengthP;
    }
    
    /**
     * Construct a sequential Kernel with given parameters 
     * (will create implicit BiasConnection).
     * @param numConnections number of Connections
     * @param weights each Connection's weight
     * @param strideLengthP number of input Nodes to step by for each output Node
     * @throws InvalidArrayException if parameter 'weights' is null
     * @throws InvalidSizeException if parameters 'numConnections' and/or 'strideLengthP' are invalid
     * @throws InconsistentSizeException if the parameter 'numConnections' and the size of parameter 'weights' conflict
     */
    public Kernel(int numConnections, int weights[], int strideLengthP) {
    
        // Test for exceptions.
        if (weights == null) {
        
            throw new InvalidArrayException("'weights' must not be null");
        }
        else if (numConnections == 0) {
        
            throw new InvalidSizeException("'numConnections' must be greater than zero");
        }
        else if (numConnections != weights.length + 1) {
        
            throw new InconsistentSizeException("'numConnections' must be one less than the length of 'weights'");
        }
        else if (strideLengthP <= 0) {
        
            throw new InvalidSizeException("'strideLengthP' must be greater than zero");
        }
        
        // Create connections array.
        primaryConnections = new NodeConnection[numConnections];
        
        // Initialize all Connections.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            primaryConnections[i] = new NodeConnection(weights[i]);
        }
        biasConnection = new BiasConnection(weights[primaryConnections.length]);
        
        // Create and initialize offsets array.
        connectionOffsets = new int[primaryConnections.length];
        for (int i = 0; i < connectionOffsets.length; i++) {
        
            connectionOffsets[i] = i;
        }
        
        strideLength = strideLengthP;
    }
    
    /**
     * Connect an array of Nodes to this Kernel's input.
     * @param inputNodesP Nodes to connect
     * @throws InputOverrideException if Kernel already has input
     * @throws InvalidArrayException if parameter 'inputNodesP' is null
     * @throws InvalidSizeException if parameter 'inputNodesP' is empty
     */
    public void connectInputNodes(Node inputNodesP[]) {
    
        // Test for exception.
        if (inputNodes != null) {
        
            throw new InputOverrideException("cannot override existing input without disconnecting it");
        }
        else if (inputNodesP == null) {
        
            throw new InvalidArrayException("'inputNodesP' must not be null");
        }
        else if (inputNodesP.length == 0) {
        
            throw new InvalidSizeException("'inputNodesP' must not be empty");
        }
        
        inputNodes = inputNodesP.clone();
    }
    
    /**
     * Connect an array of Nodes to this Kernel's output.
     * @param outputNodesP Nodes to connect
     * @throws OutputOverrideException if Kernel already has output
     * @throws InvalidArrayException if parameter 'outputNodesP' is null
     * @throws InvalidSizeException if parameter 'outputNodesP' is empty
     */
    public void connectOutputNodes(Node outputNodesP[]) {
    
        // Test for exception.
        if (outputNodes != null) {
        
            throw new OutputOverrideException("cannot override existing output without disconnecting it");
        }
        else if (outputNodesP == null) {
        
            throw new InvalidArrayException("'outputNodesP' must not be null");
        }
        else if (outputNodesP.length == 0) {
        
            throw new InvalidSizeException("'outputNodesP' must not be empty");
        }
        
        outputNodes = outputNodesP.clone();
    }
    
    /**
     * Disconnect this Kernel's input Node array.
     */
    public void disconnectInputNodes() {
    
        inputNodes = null;
    }
    
    /**
     * Disconnect this Kernel's output Node array.
     */
    public void disconnectOutputNodes() {
    
        outputNodes = null;
    }
    
    /**
     * Return a copy of the weights of this
     * Kernel's Connections (including bias).
     * @return Connection weights
     */
    public double[] getWeights() {
    
        // Array to return.
        double returnArray[] = new double[primaryConnections.length + 1];
        
        // Copy weight values.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            returnArray[i] = primaryConnections[i].getWeight();
        }
        returnArray[primaryConnections.length] = biasConnection.getWeight();
        
        return returnArray;
    }
    
    /**
     * Set the weights of this Kernel's
     * Connections (including bias).
     * @param weights weights to copy
     * @throws InvalidArrayException if parameter 'weights' is null
     * @throws InconsistentSizeException if the size of parameter 'weights' and the number of internal Connections conflict
     */
    public void setWeights(double weights[]) {
    
        // Test for exception.
        if (weights == null) {
        
            throw new InvalidArrayException("'weights' must not be null");
        }
        else if (weights.length != primaryConnections.length + 1) {
        
            throw new InconsistentSizeException("Kernel had " + (primaryConnections.length + 1) + " Connections while 'weights' had " + weights.length + "elements");
        }
        
        // Copy weight values.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            primaryConnections[i].setWeight(weights[i]);
        }
        biasConnection.setWeight(weights[primaryConnections.length]);
    }
    
    /**
     * Stride through all output Nodes and propagate
     * Connections using each.
     * @throws NullIOException if input and/or output is disconnected
     * @throws KernelOutOfBoundsException if any Connections are out of bounds
     */
    public void propagateAll() {
    
        // Test for exceptions.
        if (inputNodes == null) {
        
            throw new NullIOException("must connect input to Kernel");
        }
        else if (outputNodes == null) {
        
            throw new NullIOException("must connect output to Kernel");
        }
        
        // Propagate once for all output Nodes.
        for (int i = 0; i < outputNodes.length; i++) {
        
            propagateOnce(i);
        }
    }
    
    /**
     * Stride through all output Nodes and backpropagate
     * Connections using each.
     * @throws NullIOException if input and/or output is disconnected
     * @throws KernelOutOfBoundsException if any Connections are out of bounds
     */
    public void backpropagateAll() {
    
        // Test for exceptions.
        if (inputNodes == null) {
        
            throw new NullIOException("must connect input to Kernel");
        }
        else if (outputNodes == null) {
        
            throw new NullIOException("must connect output to Kernel");
        }
        
        // Backpropagate once for all output Nodes.
        for (int i = 0; i < outputNodes.length; i++) {
        
            backpropagateOnce(i);
        }
    }
    
    /**
     * Correct the weights of each Connection using
     * given hyperparameters.
     * @param learningRate learning factor
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     */
    public void correctConnections(double learningRate, Regularization regFunction, double regParameter) {
    
        // Correct all Connections.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            primaryConnections[i].correct(learningRate, regFunction, regParameter);
        }
        biasConnection.correct(learningRate, regFunction, regParameter);
    }
    
    // HELPER METHODS.
    
    /**
     * Check a single stride for valid boundaries.
     * @param trueOffset relative index offset into input Node array
     * @throws KernelOutOfBoundsException if any Connections are out of bounds
     */
    private void testForBoundException(int trueOffset) {
    
        // Check that all Connections are within input Node array.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            // If invalid offset found, throw exception.
            if (connectionOffsets[i] + trueOffset >= inputNodes.length) {
            
                throw new KernelOutOfBoundsException("Stride #" + trueOffset / strideLength + ": Connection #" + i + " fell out of bounds");
            }
        }
    }
    
    /**
     * Perform a single propagation stride.
     * @param strideNum current stride number
     * @throws KernelOutOfBoundsException if any Connections are out of bounds
     */
    private void propagateOnce(int strideNum) {
    
        // True input Node offset.
        int trueOffset = strideNum * strideLength;
        
        // Check if all offsets valid.
        testForBoundException(trueOffset);
        
        // Connect, propagate and disconnect each NodeConnection.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            // Connect to I/O Nodes.
            primaryConnections[i].setOutput(outputNodes[strideNum]);
            primaryConnections[i].setInput(inputNodes[connectionOffsets[i] + trueOffset]);
            
            // Propagate Connection.
            primaryConnections[i].propagate();
            
            // Disconnect from I/O Nodes.
            primaryConnections[i].clearInput();
            primaryConnections[i].clearOutput();
        }
        
        // Connect, propagate and disconnect BiasConnection.
        biasConnection.setOutput(outputNodes[strideNum]);
        biasConnection.propagate();
        biasConnection.clearOutput();
    }
    
    /**
     * Perform a single backpropagation stride.
     * @param strideNum current stride number
     * @throws KernelOutOfBoundsException if any Connections are out of bounds
     */
    private void backpropagateOnce(int strideNum) {
    
        // True input Node offset.
        int trueOffset = strideNum * strideLength;
        
        // Check if all offsets valid.
        testForBoundException(trueOffset);
        
        // Connect, backpropagate and disconnect each NodeConnection.
        for (int i = 0; i < primaryConnections.length; i++) {
        
            // Connect to I/O Nodes.
            primaryConnections[i].setOutput(outputNodes[strideNum]);
            primaryConnections[i].setInput(inputNodes[connectionOffsets[i] + trueOffset]);
            
            // Backpropagate Connection.
            primaryConnections[i].backpropagate();
            
            // Disconnect from I/O Nodes.
            primaryConnections[i].clearInput();
            primaryConnections[i].clearOutput();
        }
        
        // Connect, backpropagate and disconnect BiasConnection.
        biasConnection.setOutput(outputNodes[strideNum]);
        biasConnection.backpropagate();
        biasConnection.clearOutput();
    }
}
