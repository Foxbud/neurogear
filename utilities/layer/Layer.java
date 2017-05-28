package neurogear.utilities.layer;

import java.util.Arrays;
import java.util.Random;
import neurogear.base.activation.Activation;
import neurogear.base.cost.Cost;
import neurogear.base.node.Node;
import neurogear.base.regularization.Regularization;
import neurogear.utilities.layer.kernel.Kernel;

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
    
    // Kernel for each channel.
    private final Kernel kernels[];
    // Nodes for each channel.
    private final Node nodes[][];
    
    // Activation function for Nodes.
    private final Activation activationFunction;
    
    // This Layer's input Layer.
    private Layer inputLayer;
    // States whether this Layer is input to another layer.
    private boolean hasOutputLayer;
    
    // MEMBER METHODS.
    
    public Layer(int numChannels, int nodesPerChannel, Activation activationFunctionP, int receptiveField[], int numInputChannels, int strideLength, int seed) {
    
        // Create 2d Node array.
        nodes = new Node[numChannels][nodesPerChannel];
        
        // Initialize 2D Node array.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI] = new Node();
            }
        }
        
        // Create Kernel array.
        kernels = new Kernel[numChannels];
        
        // Set activation function.
        activationFunction = activationFunctionP;
        
        // Initialize hasOutputLayer.
        hasOutputLayer = false;
        
        // Initialize Kernel array.
        for (int i = 0; i < kernels.length; i++) {
        
            // Create Kernel.
            kernels[i] = new Kernel(numInputChannels, receptiveField, strideLength);
            
            // Set Kernel weights.
            kernels[i].setWeights(generateWeights(numInputChannels, receptiveField.length, seed + i));
            
            // Connect Kernel to appropriate output.
            kernels[i].connectOutputNodes(nodes[i]);
        }
    }
    
    public Layer(int numChannels, int nodesPerChannel, Activation activationFunctionP) {
    
        // Create 2d Node array.
        nodes = new Node[numChannels][nodesPerChannel];
        
        // Initialize 2D Node array.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI] = new Node();
            }
        }
        
        // Create empty kernels array.
        kernels = new Kernel[0];
        
        // Set activation function.
        activationFunction = activationFunctionP;
        
        // Initialize hasOutputLayer.
        hasOutputLayer = false;
    }
    
    public Node[][] getNodes() {
    
        return nodes;
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
        
            kernels[i].connectInputNodes(inputLayer.getNodes());
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
    
    public double[][] getActivationValues() {
        
        // Array to return.
        double activationValues[][] = new double[nodes.length][nodes[0].length];
        
        // Copy activation values.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                activationValues[channelI][nodeI] = nodes[channelI][nodeI].getActivationValue();
            }
        }
        
        return activationValues;
    }
    
    public void propagate(double initialValues[][]) {
    
        // Set activation sums.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].addToActivationSum(initialValues[channelI][nodeI]);
            }
        }
        
        propagate();
    }
    
    public void propagate() {
    
        // Propagate Kernels.
        for (int i = 0; i < kernels.length; i++) {

            kernels[i].propagateAll();
        }
        
        // Activate Nodes.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].triggerActivation(activationFunction);
            }
        }
    }
    
    public void backpropagate(double targetValues[][], Cost costFunction) {
        
        // Set delta sums.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].setInitialDelta(costFunction, targetValues[channelI][nodeI]);
            }
        }
        
        backpropagate();
    }
    
    public void backpropagate() {
    
        // Trigger Node deltas.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].triggerDelta(activationFunction);
            }
        }
        
        // Backropagate Kernels.
        for (int i = 0; i < kernels.length; i++) {

            kernels[i].backpropagateAll();
        }
    }
    
    public void clearNodeSums() {
    
        // Clear the sums of all Nodes.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].clearSums();
            }
        }
    }
    
    public void correctKernels(double learningRate, Regularization regFunction, double regParameter) {
    
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].correctConnections(learningRate, regFunction, regParameter);
        }
    }
    
    // HELPER METHODS.
    
    private double[] generateWeights(int numInputChannels, int connectionsPerInputChannel, int seed) {
    
        // Initialize PRNG for weight generation.
        Random PRNG = new Random(seed);
        
        // Create weight arrays.
        double weights[] = new double[numInputChannels * connectionsPerInputChannel + 1];
        
        // Generate weights.
        for (int i = 0; i < numInputChannels; i++) {
        
            // Set NodeConnection weights.
            for (int j = 0; j < connectionsPerInputChannel; j++) {
            
                weights[i * connectionsPerInputChannel + j] = (PRNG.nextDouble() - 0.5) * 2.0 / Math.sqrt(connectionsPerInputChannel);
            }
            
            // Set BiasConnection weight.
            weights[weights.length - 1] = 0.0;
        }
        
        return weights;
    }
}
