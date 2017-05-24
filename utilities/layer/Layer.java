package neurogear.utilities.layer;

import java.util.Arrays;
import java.util.Random;
import neurogear.base.activation.Activation;
import neurogear.base.cost.Cost;
import neurogear.base.node.Node;
import neurogear.base.regularization.Regularization;
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
        double weights[][] = generateWeights(numFeatures, offsets.length, seed);
        
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
        double weights[][] = generateWeights(numFeatures, numConnections, seed);
        
        // Initialize Kernel array.
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i] = new Kernel(numConnections, weights[i], strideLength);
            
            // Connect Kernel to appropriate output.
            kernels[i].connectOutputNodes(nodes[i]);
        }
    }
    
    public double[][] getWeights() {
    
        // Array to return.
        double weights[][] = new double[kernels.length][];
        
        // Copy weights.
        for (int i = 0; i < weights.length; i++) {
        
            weights[i] = kernels[i].getWeights();
        }
        
        return weights;
    }
    
    public void setWeights(double weights[][]) {
    
        // Copy weights.
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].setWeights(weights[i]);
        }
    }
    
    public void connect(Layer inputLayerP) {
    
        // Test for exceptions.
        
        // Set own input Layer.
        inputLayer = inputLayerP;
        
        // Toggle inputLayer's output.
        inputLayer.toggleOutput();
        
        // Connect Kernels to inputLayer.
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].connectInputNodes(inputLayerP.expressNodes());
        }
    }
    
    public void disconnect() {
    
        // Disconnect Kernels from inputLayer.
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].disconnectInputNodes();
        }
        
        // Toggle inputLayer's output.
        inputLayer.toggleOutput();
        
        // Clear own input Layer.
        inputLayer = null;
    }
    
    public boolean hasOutput() {
    
        return hasOutputLayer;
    }
    
    public void toggleOutput() {
    
        hasOutputLayer = !hasOutputLayer;
    }
    
    public Node[] expressNodes() {
    
        // Array to return.
        Node returnNodes[] = new Node[nodes.length * nodes[0].length];
        
        // Format Nodes.
        for (int iNode = 0; iNode < nodes[0].length; iNode++) {
        
            for (int iFeature = 0; iFeature < nodes.length; iFeature++) {
            
                returnNodes[iNode * nodes.length + iFeature] = nodes[iFeature][iNode];
            }
        }
        
        return returnNodes;
    }
    
    public double[] getActivationValues() {
    
        // Format Nodes.
        Node formattedNodes[] = expressNodes();
        
        // Array to return.
        double activationValues[] = new double[formattedNodes.length];
        
        // Copy activation values.
        for (int i = 0; i < activationValues.length; i++) {
        
            activationValues[i] = formattedNodes[i].getActivationValue();
        }
        
        return activationValues;
    }
    
    public void propagate(double initialValues[]) {
    
        // Format Nodes.
        Node formattedNodes[] = expressNodes();
        
        // Set activation sums.
        for (int i = 0; i < initialValues.length; i++) {
        
            formattedNodes[i].addToActivationSum(initialValues[i]);
        }
        
        // Activate Nodes.
        for (int i = 0; i < nodes.length; i++) {
        
            for (int j = 0; j < nodes[i].length; j++) {
            
                nodes[i][j].triggerActivation(activationFunction);
            }
        }
    }
    
    public void propagate() {
    
        // Propagate Kernels.
        for (int i = 0; i < kernels.length; i++) {

            kernels[i].propagateAll();
        }
        
        // Activate Nodes.
        for (int i = 0; i < nodes.length; i++) {
        
            for (int j = 0; j < nodes[i].length; j++) {
            
                nodes[i][j].triggerActivation(activationFunction);
            }
        }
    }
    
    public void backpropagate(double targetValues[], Cost costFunction) {
    
        // Format Nodes.
        Node formattedNodes[] = expressNodes();
        
        // Set delta sums.
        for (int i = 0; i < targetValues.length; i++) {
        
            formattedNodes[i].setInitialDelta(costFunction, targetValues[i]);
        }
        
        backpropagate();
    }
    
    public void backpropagate() {
    
        // Trigger Node deltas.
        for (int i = 0; i < nodes.length; i++) {
        
            for (int j = 0; j < nodes[i].length; j++) {
            
                nodes[i][j].triggerDelta(activationFunction);
            }
        }
        
        // Backropagate Kernels.
        for (int i = 0; i < kernels.length; i++) {

            kernels[i].backpropagateAll();
        }
    }
    
    public void correctKernels(double learningRate, Regularization regFunction, double regParameter) {
    
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].correctConnections(learningRate, regFunction, regParameter);
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
    
    private double[][] generateWeights(int numFeatures, int connectionsPerFeature, int seed) {
    
        // Initialize PRNG for weight generation.
        Random PRNG = new Random(seed);
        
        // Create weight arrays.
        double weights[][] = new double[numFeatures][connectionsPerFeature + 1];
        
        // Generate weights.
        for (int i = 0; i < weights.length; i++) {
        
            // Set NodeConnection weights.
            for (int j = 0; j < weights[i].length - 1; j++) {
            
                weights[i][j] = PRNG.nextDouble() / Math.sqrt(weights[i].length - 1);
            }
            
            // Set BiasConnection weight.
            weights[i][weights[i].length - 1] = 0.0;
        }
        
        return weights;
    }
}
