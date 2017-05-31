package neurogear.utilities.layer;

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
    // Regularization function for Kernel correction.
    private final Regularization regularizationFunction;
    
    // This Layer's input Layer.
    private Layer inputLayer;
    // States whether this Layer is input to another layer.
    private boolean hasOutputLayer;
    
    // MEMBER METHODS.
    
    /**
     * Consrtuct a Layer with given parameters.
     * @param numChannels number of channels for this Layer
     * @param nodesPerChannel number of nodes for each channel of this Layer
     * @param activationFunctionP activation function
     * @param regularizationFunctionP regularization function
     * @param receptiveField receptive field configuration for all Kernels
     * @param numInputChannels number of input Layer channels to configure Kernels for
     * @param strideLength how far to move receptive field for each internal Node
     * @param seed seed for weight initialization
     */
    public Layer(int numChannels, int nodesPerChannel, Activation activationFunctionP, Regularization regularizationFunctionP, int receptiveField[], int numInputChannels, int strideLength, int seed) {
    
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
        
        // Set functions.
        activationFunction = activationFunctionP;
        regularizationFunction = regularizationFunctionP;
        
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
    
    /**
     * Construct a Layer with given parameters
     * which does not have any Kernels and cannot have
     * and input Layer.
     * @param numChannels number of channels for this Layer
     * @param nodesPerChannel number of nodes for each channel of this Layer
     * @param activationFunctionP activation function 
     */
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
        
        // Set functions.
        activationFunction = activationFunctionP;
        regularizationFunction = null;
        
        // Initialize hasOutputLayer.
        hasOutputLayer = false;
    }
    
    /**
     * Return this Layer's Nodes.
     * @return Nodes where rows are channels and columns are locations
     */
    public Node[][] getNodes() {
    
        return nodes;
    }
    
    /**
     * Return copy of this Layer's weights.
     * @return each Kernel's weights
     */
    public double[][] getWeights() {
    
        // Array to return.
        double weights[][] = new double[kernels.length][];
        
        // Copy weights.
        for (int i = 0; i < weights.length; i++) {
        
            weights[i] = kernels[i].getWeights();
        }
        
        return weights;
    }
    
    /**
     * Set this Layer's weights.
     * @param weights each Kernel's weights
     */
    public void setWeights(double weights[][]) {
    
        // Copy weights.
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].setWeights(weights[i]);
        }
    }
    
    /**
     * Set this Layer's input Layer.
     * @param inputLayerP Layer to set as input
     */
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
    
    /**
     * Clear this Layer's input Layer.
     */
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
    
    /**
     * Check whether this Layer is acting as an
     * input to another Layer.
     * @return whether this Layer is an input
     */
    public boolean hasOutput() {
    
        return hasOutputLayer;
    }
    
    /**
     * Toggle whether this Layer is acting as an 
     * input to another Layer.
     */
    public void toggleOutput() {
    
        hasOutputLayer = !hasOutputLayer;
    }
    
    /**
     * Return copy of this Layer's Node's activtion values.
     * @return Node activation values where rows are channels and columns are locations
     */
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
    
    /**
     * Trigger Node activations with initial
     * activation sums (will trigger Kernel
     * propagation).
     * @param initialValues initiale Node activation sums where rows are channels and columns are locations
     */
    public void propagate(double initialValues[][]) {
    
        // Set activation sums.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].addToActivationSum(initialValues[channelI][nodeI]);
            }
        }
        
        propagate();
    }
    
    /**
     * Trigger Node activations based on sums
     * received from Kernel propagations.
     */
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
    
    /**
     * Trigger Node deltas based on target 
     * values and a cost function and
     * propagate deltas backwards.
     * @param targetValues target values where rows are channels and columns are locations
     * @param costFunction cost function
     */
    public void backpropagate(double targetValues[][], Cost costFunction) {
        
        // Set delta sums.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].setInitialDelta(costFunction, targetValues[channelI][nodeI]);
            }
        }
        
        backpropagate();
    }
    
    /**
     * Trigger Node deltas based on received 
     * delta sums and propagate deltas backwards.
     */
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
    
    /**
     * Clear internal Node activation 
     * and delta sums.
     */
    public void clearNodeSums() {
    
        // Clear the sums of all Nodes.
        for (int channelI = 0; channelI < nodes.length; channelI++) {
        
            for (int nodeI = 0; nodeI < nodes[channelI].length; nodeI++) {
            
                nodes[channelI][nodeI].clearSums();
            }
        }
    }
    
    /**
     * Correct Kernel weights using given parameters
     * and internal regularization function.
     * @param learningRate learning factor
     * @param regParameter regularization parameter
     */
    public void correctKernels(double learningRate, double regParameter) {
    
        for (int i = 0; i < kernels.length; i++) {
        
            kernels[i].correctConnections(learningRate, regularizationFunction, regParameter);
        }
    }
    
    // HELPER METHODS.
    
    /**
     * Generate a single set of Kernel weights
     * with given parameters.
     * @param numInputChannels number of input Layer channels to configure Kernel weights for
     * @param numConnections number of Kernel NodeConnections
     * @param seed seed for generating weights
     * @return a single set of Kernel weights
     */
    private double[] generateWeights(int numInputChannels, int numConnections, int seed) {
    
        // Initialize PRNG for weight generation.
        Random PRNG = new Random(seed);
        
        // Create weight arrays.
        double weights[] = new double[numInputChannels * numConnections + 1];
        
        // Generate weights.
        for (int i = 0; i < numInputChannels; i++) {
        
            // Set NodeConnection weights.
            for (int j = 0; j < numConnections; j++) {
            
                weights[i * numConnections + j] = (PRNG.nextDouble() - 0.5) * 2.0 / Math.sqrt(numConnections);
            }
            
            // Set BiasConnection weight.
            weights[weights.length - 1] = 0.0;
        }
        
        return weights;
    }
}
