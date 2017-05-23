package neurogear.utilities.layer;

import java.util.Arrays;
import java.util.Random;
import neurogear.base.activation.Activation;
import neurogear.base.node.Node;
import neurogear.utilities.kernel.Kernel;

/**
 * Layer class for neural networks.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Layer.java
 * Created: 05/15/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Holds a group a similar Nodes
 * and their corresponding input Connections.
 */
public final class Layer {
    
    /*
    Outline:
    
    Data:
    array of Kernels
    2d array of Nodes
    activation function
    input Layer
    has output Layer
    
    Methods:
    constructor(num features, Nodes per feature, offsets, stride, activation, seed)
    constructor(num features, Nodes per feature, num FC, stride, activation, seed)
    getWeights
    setWeights(2d array of weights)
    connect(Layer)
    disconnect
    hasOutput
    toggleOutput
    expressNodes
    getActivationValues
    setActivationSums(array of doubles)
    propagate
    backpropagate
    correct(hyperparameters)
    */
    
    // MEMBER VARIABLES.
    
    // Kernel for each feature.
    private final Kernel kernels[];
    // Nodes for each feature.
    private final Node nodes[][];
    
    // Activation function for Nodes.
    private final Activation activationFunction;
    
    // This Layer's input Layer.
    private Layer inputLayer;
    // States whether this Layer is input to another layer.
    private boolean hasOutputLayer;
    
    // MEMBER METHODS.
    
    public Layer(int numFeatures, int nodesPerFeature, int offsets[], int strideLength, Activation activationFunctionP, int seed) {
    
        this(numFeatures, nodesPerFeature, activationFunctionP);
        
        // Initialize PRNG for weight generation.
        Random PRNG = new Random(seed);
        
        // Create weight arrays.
        double weights[][] = new double[numFeatures][offsets.length + 1];
        
        // Generate weights.
        for (int i = 0; i < weights.length; i++) {
        
            // Set NodeConnection weights.
            for (int j = 0; j < weights[i].length - 1; j++) {
            
                weights[i][j] = PRNG.nextDouble() / Math.sqrt(weights[i].length - 1);
            }
            
            // Set BiasConnection weight.
            weights[i][weights[i].length - 1] = 0.0;
        }
        
        // Initialize Kernel array.
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i] = new Kernel(offsets, weights[i], strideLength);
            
            // Connect Kernel to appropriate output.
            kernels[i].connectOutputNodes(nodes[i]);
        }
    }
    
    public Layer(int numFeatures, int nodesPerFeature, int numConnections, int strideLength, Activation activationFunctionP, int seed) {
    
        this(numFeatures, nodesPerFeature, activationFunctionP);
        
        // Initialize PRNG for weight generation.
        Random PRNG = new Random(seed);
        
        // Create weight arrays.
        double weights[][] = new double[numFeatures][numConnections + 1];
        
        // Generate weights.
        for (int i = 0; i < weights.length; i++) {
        
            // Set NodeConnection weights.
            for (int j = 0; j < weights[i].length - 1; j++) {
            
                weights[i][j] = PRNG.nextDouble() / Math.sqrt(weights[i].length - 1);
            }
            
            // Set BiasConnection weight.
            weights[i][weights[i].length - 1] = 0.0;
        }
        
        // Initialize Kernel array.
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i] = new Kernel(numConnections, weights[i], strideLength);
            
            // Connect Kernel to appropriate output.
            kernels[i].connectOutputNodes(nodes[i]);
        }
    }
    
    // HELPER METHODS.
    
    private Layer(int numFeatures, int nodesPerFeature, Activation activationFunctionP) {
    
        // Create 2d Node array.
        nodes = new Node[numFeatures][nodesPerFeature];
        
        // Initialize 2d Node array.
        for (int i = 0; i < nodes.length; i++) {
        
            Arrays.fill(nodes[i], new Node());
        }
        
        // Create Kernel array.
        kernels = new Kernel[numFeatures];
        
        // Set activation function.
        activationFunction = activationFunctionP;
        
        // Initialize hasOutputLayer.
        hasOutputLayer = false;
    }
}
