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
    // Nodes formatted into a one dimensional array.
    private final Node formattedNodes[];
    
    // Activation function for Nodes.
    private final Activation activationFunction;
    
    // This Layer's input Layer.
    private Layer inputLayer;
    // States whether this Layer is input to another layer.
    private boolean hasOutputLayer;
    
    // MEMBER METHODS.
    
    public Layer(int numFeatures, int nodesPerFeature, Activation activationFunctionP, int offsets[], int strideLength, int seed) {
    
        // Create 2d Node array.
        nodes = new Node[numFeatures][nodesPerFeature];
        
        // Initialize 2d Node array.
        for (int i = 0; i < nodes.length; i++) {
        
            Arrays.fill(nodes[i], new Node());
        }
        
        // Create formatted Node array.
        formattedNodes = formatNodes(nodes);
        
        // Create Kernel array.
        kernels = new Kernel[numFeatures];
        
        // Set activation function.
        activationFunction = activationFunctionP;
        
        // Initialize hasOutputLayer.
        hasOutputLayer = false;
        
        // Generate Kernel weights.
        double weights[][] = generateWeights(numFeatures, offsets.length, seed);
        
        // Initialize Kernel array.
        for (int i = 0; i < kernels.length; i++) {
        
            // Create Kernel.
            kernels[i] = new Kernel(offsets, strideLength);
            
            // Set Kernel weights.
            kernels[i].setWeights(weights[i]);
            
            // Connect Kernel to appropriate output.
            kernels[i].connectOutputNodes(nodes[i]);
        }
    }
    
    public Layer(int numFeatures, int nodesPerFeature, Activation activationFunctionP) {
    
        // Create 2d Node array.
        nodes = new Node[numFeatures][nodesPerFeature];
        
        // Initialize 2d Node array.
        for (int i = 0; i < nodes.length; i++) {
        
            Arrays.fill(nodes[i], new Node());
        }
        
        // Create formatted Node array.
        formattedNodes = formatNodes(nodes);
        
        // Create empty kernels array.
        kernels = new Kernel[0];
        
        // Set activation function.
        activationFunction = activationFunctionP;
        
        // Initialize hasOutputLayer.
        hasOutputLayer = false;
    }
    
    public Node[] getNodes() {
    
        return formattedNodes;
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
        
            kernels[i].connectInputNodes(inputLayerP.getNodes());
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
    
    public double[] getActivationValues() {
        
        // Array to return.
        double activationValues[] = new double[formattedNodes.length];
        
        // Copy activation values.
        for (int i = 0; i < activationValues.length; i++) {
        
            activationValues[i] = formattedNodes[i].getActivationValue();
        }
        
        return activationValues;
    }
    
    public void propagate(double initialValues[]) {
    
        // Set activation sums.
        for (int i = 0; i < initialValues.length; i++) {
        
            formattedNodes[i].addToActivationSum(initialValues[i]);
        }
        
        propagate();
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
    
    public void clearNodeSums() {
    
        // Clear the sums of all Nodes.
        for (int i = 0; i < formattedNodes.length; i++) {
        
            formattedNodes[i].clearSums();
        }
    }
    
    public void correctKernels(double learningRate, Regularization regFunction, double regParameter) {
    
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].correctConnections(learningRate, regFunction, regParameter);
        }
    }
    
    // HELPER METHODS.
    
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
    
    private Node[] formatNodes(Node nodesP[][]) {
    
        // Array to return.
        Node returnNodes[] = new Node[nodesP.length * nodesP[0].length];
        
        // Format Nodes.
        for (int iNode = 0; iNode < nodesP[0].length; iNode++) {
        
            for (int iFeature = 0; iFeature < nodesP.length; iFeature++) {
            
                returnNodes[iNode * nodesP.length + iFeature] = nodesP[iFeature][iNode];
            }
        }
        
        return returnNodes;
    }
}
