 package neurogear.utilities.layer.kernel;

import java.util.Arrays;
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
    
    // Node Connections which represent this Kernel with rows connectiong to channels.
    private final NodeConnection primaryConnections[][];
    // Bias Connection for Kernel.
    private final BiasConnection biasConnection;
    // Offset for each row of NodeConnections from start index.
    private final int receptiveField[];
    
    // Kernel input Nodes with rows being channels.
    private Node inputNodes[][];
    // Kernel output Nodes.
    private Node outputNodes[];
    
    // Number of input Node columns to stride by.
    private final int strideLength;
    
    // MEMBER METHODS.
    
    /**
     * Construct a Kernel with given parameters 
     * (will create implicit BiasConnection).
     * @param numChannels number of input channels for this Kernel
     * @param receptiveFieldP each column of Connections' relative offset to the stride index
     * @param strideLengthP number of input Node columns to stride by
     * @throws InvalidArrayException if parameter 'receptiveFieldP' is not valid
     * @throws InvalidSizeException if the parameters 'numChannels' and 'strideLengthP' or not valid
     */
    public Kernel(int numChannels, int receptiveFieldP[], int strideLengthP) {
        
        // Test for exceptions.
        if (receptiveFieldP == null) {
        
            throw new InvalidArrayException("'receptiveFieldP' must not be null");
        }
        else if (receptiveFieldP.length == 0) {
        
            throw new InvalidArrayException("'receptiveFieldsP' must not be empty");
        }
        else if (numChannels <= 0) {
        
            throw new InvalidSizeException("'numChannels' must be greater than zero");
        }
        else if (strideLengthP <= 0) {
        
            throw new InvalidSizeException("'strideLengthP' must be greater than zero");
        }
        
        // Create connections array.
        primaryConnections = new NodeConnection[numChannels][receptiveFieldP.length];
        
        // Initialize all Connections.
        for (int channelI = 0; channelI < primaryConnections.length; channelI++) {
        
            for (int nodeI = 0; nodeI < primaryConnections[channelI].length; nodeI++) {
            
                primaryConnections[channelI][nodeI] = new NodeConnection(0.0);
            }
        }
        biasConnection = new BiasConnection(0.0);
        
        // Create and initialize receptiveField array.
        receptiveField = receptiveFieldP.clone();
        
        strideLength = strideLengthP;
        
        inputNodes = null;
        outputNodes = null;
    }
    
    /**
     * Connect a 2D array of Nodes to this Kernel's input.
     * @param inputNodesP Nodes to connect
     * @throws InputOverrideException if Kernel already has input
     * @throws InvalidArrayException if parameter 'inputNodesP' is not valid
     * @throws SizeConflictException if parameter 'inputNodesP' and internal Connections have conflicting numbers of channels
     * @throws ReceptiveFieldConflictException if input and output are not compatible with receptive field
     */
    public void connectInputNodes(Node inputNodesP[][]) {
        
        // Test for exceptions.
        if (inputNodes != null) {
        
            throw new InputOverrideException("cannot set new input without disconnecting current input");
        }
        else if (inputNodesP == null) {
        
            throw new InvalidArrayException("'inputNodesP' must not be null");
        }
        else if (inputNodesP.length == 0) {
        
            throw new InvalidArrayException("'inputNodesP' must not be empty");
        }
        else if (inputNodes.length != primaryConnections.length) {
        
            throw new SizeConflictException("'inputNodesP' has " + inputNodesP.length + " channels while Kernel is configured for " + primaryConnections.length + " channels");
        }
        else if (outputNodes != null) {
        
            testForReceptiveFieldConflict(inputNodesP, outputNodes);
        }
        
        inputNodes = inputNodesP.clone();
    }
    
    /**
     * Connect an array of Nodes to this Kernel's output.
     * @param outputNodesP Nodes to connect
     * @throws OutputOverrideException if Kernel already has output
     * @throws InvalidArrayException if parameter 'outputNodesP' is not valid
     * @throws ReceptiveFieldConflictException if input and output are not compatible with receptive field
     */
    public void connectOutputNodes(Node outputNodesP[]) {
        
        // Test for exceptions.
        if (outputNodes != null) {
        
            throw new OutputOverrideException("cannot set new output without disconnecting current output");
        }
        else if (outputNodesP == null) {
        
            throw new InvalidArrayException("'outputNodesP' must not be null");
        }
        else if (outputNodesP.length == 0) {
        
            throw new InvalidArrayException("'outputNodesP' must not be empty");
        }
        else if (inputNodes != null) {
        
            testForReceptiveFieldConflict(inputNodes, outputNodesP);
        }
        
        outputNodes = outputNodesP.clone();
    }
    
    /**
     * Disconnect this Kernel's 2D input Node array.
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
     * Return a copy of the weights of this Kernel's
     * Connections (including bias).
     * @return Connection weights (the last of which is bias)
     */
    public double[] getWeights() {
    
        // Array to return.
        double returnArray[] = new double[primaryConnections.length * primaryConnections[0].length + 1];
        
        // Copy weight values.
        for (int channelI = 0; channelI < primaryConnections.length; channelI++) {
        
            for (int nodeI = 0; nodeI < primaryConnections[channelI].length; nodeI++) {
            
                returnArray[channelI * primaryConnections[channelI].length + nodeI] = primaryConnections[channelI][nodeI].getWeight();
            }
        }
        returnArray[returnArray.length - 1] = biasConnection.getWeight();
        
        return returnArray;
    }
    
    /**
     * Set the weights of this Kernel's
     * Connections (including bias).
     * @param weights weights to copy (the last of which is bias)
     * @throws InvalidArrayException if parameter 'weights' is null
     * @throws SizeConflictException if size of parameter 'weights' and number of Connections conflicts
     */
    public void setWeights(double weights[]) {
        
        // Test for exceptions.
        if (weights == null) {
        
            throw new InvalidArrayException("'weights' must not be null");
        }
        else if (weights.length != primaryConnections.length * primaryConnections[0].length + 1) {
        
            throw new SizeConflictException("size of 'weights' and number of Connections conflict");
        }
        
        // Set weight values.
        for (int channelI = 0; channelI < primaryConnections.length; channelI++) {
        
            for (int nodeI = 0; nodeI < primaryConnections[channelI].length; nodeI++) {
            
                primaryConnections[channelI][nodeI].setWeight(weights[channelI * primaryConnections[channelI].length + nodeI]);
            }
        }
        biasConnection.setWeight(weights[weights.length - 1]);
    }
    
    /**
     * Stride through all output Nodes and propagate
     * Connections using each.
     * @throws InvalidInputException if no input Nodes have been connected
     * @throws InvalidOutputException if no output Nodes have been connected
     */
    public void propagateAll() {
        
        // Test for exceptions.
        if (inputNodes == null) {
        
            throw new InvalidInputException("must connect input Nodes before propagating");
        }
        else if (outputNodes == null) {
        
            throw new InvalidOutputException("must connect output Nodes before propagating");
        }
        
        // Propagate once for all output Nodes.
        for (int i = 0; i < outputNodes.length; i++) {
        
            propagateOnce(i);
        }
    }
    
    /**
     * Stride through all output Nodes and backpropagate
     * Connections using each.
     * @throws InvalidInputException if no input Nodes have been connected
     * @throws InvalidOutputException if no output Nodes have been connected
     */
    public void backpropagateAll() {
        
        // Test for exceptions.
        if (inputNodes == null) {
        
            throw new InvalidInputException("must connect input Nodes before backpropagating");
        }
        else if (outputNodes == null) {
        
            throw new InvalidOutputException("must connect output Nodes before backpropagating");
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
        for (int channelI = 0; channelI < primaryConnections.length; channelI++) {
        
            for (int nodeI = 0; nodeI < primaryConnections[channelI].length; nodeI++) {
            
                primaryConnections[channelI][nodeI].correct(learningRate, regFunction, regParameter);
            }
        }
        biasConnection.correct(learningRate, regFunction, regParameter);
    }
    
    // HELPER METHODS.
    
    /**
     * Test current input, output, and receptive
     * field for incompatibilities.
     * @param input input Nodes
     * @param output output Nodes
     * @throws ReceptiveFieldConflictException if input and output are not compatible with receptive field
     */
    private void testForReceptiveFieldConflict(Node input[][], Node output[]) {
    
        // Array of booleans to track which input columns have been visited.
        boolean visited[] = new boolean[input[0].length];
        Arrays.fill(visited, false);
        
        // Iterate through all output Nodes.
        for (int oNodeI = 0; oNodeI < output.length; oNodeI++) {
        
            // True offset index.
            int trueOffset = oNodeI * strideLength;
            
            // Iterate through all offsets in receptive field.
            for (int rpI = 0; rpI < receptiveField.length; rpI++) {
            
                // Test for exception.
                if (trueOffset + receptiveField[rpI] >= inputNodes[0].length) {
                
                    throw new ReceptiveFieldConflictException("receptive field column #" + rpI + " will fall out of bounds on stride #" + oNodeI);
                }
                
                // Set appropriate visited boolean.
                visited[trueOffset + receptiveField[rpI]] = true;
            }
        }
        
        // Check if all input columns were vistied.
        for (int column = 0; column < visited.length; column++) {
        
            // Test for exception.
            if (!visited[column]) {
            
                throw new ReceptiveFieldConflictException("input Node column #" + column + " will not be visited by receptive field");
            }
        }
    }
    
    /**
     * Perform a single propagation stride.
     * @param strideNum current stride number
     */
    private void propagateOnce(int strideNum) {
        
        // Connect, propagate and disconnect each NodeConnection.
        for (int channelI = 0; channelI < primaryConnections.length; channelI++) {
        
            for (int nodeI = 0; nodeI < primaryConnections[channelI].length; nodeI++) {
            
                // Connect Connection.
                primaryConnections[channelI][nodeI].setInput(inputNodes[channelI][nodeI + strideNum * strideLength]);
                primaryConnections[channelI][nodeI].setOutput(outputNodes[strideNum]);
                
                // Propagate Connection.
                primaryConnections[channelI][nodeI].propagate();
                
                // Disconnect Connection.
                primaryConnections[channelI][nodeI].clearOutput();
                primaryConnections[channelI][nodeI].clearInput();
            }
        }
        
        // Connect, propagate and disconnect BiasConnection.
        biasConnection.setOutput(outputNodes[strideNum]);
        biasConnection.propagate();
        biasConnection.clearOutput();
    }
    
    /**
     * Perform a single backpropagation stride.
     * @param strideNum current stride number
     * @throws ConnectionOutOfBoundsException if any Connections are out of bounds
     */
    private void backpropagateOnce(int strideNum) {
        
        // Connect, backpropagate and disconnect each NodeConnection.
        for (int channelI = 0; channelI < primaryConnections.length; channelI++) {
        
            for (int nodeI = 0; nodeI < primaryConnections[channelI].length; nodeI++) {
            
                // Connect Connection.
                primaryConnections[channelI][nodeI].setInput(inputNodes[channelI][nodeI + strideNum * strideLength]);
                primaryConnections[channelI][nodeI].setOutput(outputNodes[strideNum]);
                
                // Backpropagate Connection.
                primaryConnections[channelI][nodeI].backpropagate();
                
                // Disconnect Connection.
                primaryConnections[channelI][nodeI].clearOutput();
                primaryConnections[channelI][nodeI].clearInput();
            }
        }
        
        // Connect, backpropagate and disconnect BiasConnection.
        biasConnection.setOutput(outputNodes[strideNum]);
        biasConnection.backpropagate();
        biasConnection.clearOutput();
    }
}
