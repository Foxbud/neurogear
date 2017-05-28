 package neurogear.utilities.layer.kernel;

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
     */
    public Kernel(int numChannels, int receptiveFieldP[], int strideLengthP) {
        
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
     */
    public void connectInputNodes(Node inputNodesP[][]) {
        
        inputNodes = inputNodesP.clone();
    }
    
    /**
     * Connect an array of Nodes to this Kernel's output.
     * @param outputNodesP Nodes to connect
     */
    public void connectOutputNodes(Node outputNodesP[]) {
        
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
     */
    public void setWeights(double weights[]) {
        
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
     */
    public void propagateAll() {
        
        // Propagate once for all output Nodes.
        for (int i = 0; i < outputNodes.length; i++) {
        
            propagateOnce(i);
        }
    }
    
    /**
     * Stride through all output Nodes and backpropagate
     * Connections using each.
     */
    public void backpropagateAll() {
        
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
                
                // Propagate Connection.
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
