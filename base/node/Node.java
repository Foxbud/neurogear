package neurogear.base.node;

import neurogear.base.activation.Activation;
import neurogear.base.cost.Cost;

/**
 * Fundamental node class for neural networks.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Node.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: The analog of a neuron in a natural neural network.
 * Uses the Connection class for linkage between Nodes.
 */
public final class Node {
    
    // MEMBER VARIABLES.
    
    // Incoming weighted activation sum.
    private double activationSum;
    // Outgoing activation value.
    private double activationValue;
    // Incoming weighted error sum.
    private double deltaSum;
    // Outgoing error value.
    private double deltaValue;
    
    // MEMBER METHODS.
    
    /**
     * Construct a Node.
     */
    public Node() {
    
        activationSum = 0.0;
        activationValue = 0.0;
        deltaSum = 0.0;
        deltaValue = 0.0;
    }
    
    /**
     * Return this Node's activation value.
     * @return activation value
     */
    public double getActivationValue() {
    
        return activationValue;
    }
    
    /**
     * Return this Node's delta value.
     * @return delta value
     */
    public double getDeltaValue() {
    
        return deltaValue;
    }
    
    /**
     * Add a value to this Node's activation sum.
     * @param addend value to add
     */
    public void addToActivationSum(double addend) {
    
        activationSum += addend;
    }
    
    /**
     * Add a value to this Node's delta sum.
     * @param addend value to add
     */
    public void addToDeltaSum(double addend) {
    
        deltaSum += addend;
    }
    
    /**
     * Set the initial delta sum for Nodes
     * in the output layer of a network.
     * @param costFunction function for gauging loss
     * @param targetValue known target value
     * @throws InvalidCostException if parameter 'costFunction' is null
     */
    public void setInitialDelta(Cost costFunction, double targetValue) {
    
        // Test for exception.
        if (costFunction == null) {
        
            throw new InvalidCostException("'costFunction' must not be null");
        }
        
        // Set initial node delta with cost function and target value.
        deltaSum = costFunction.df(activationValue, targetValue);
    }
    
    /**
     * Compute this Node's activation value using
     * its activation sum and an activation function.
     * @param activationFunction activation function to use
     * @throws InvalidActivationException if parameter 'activationFunction' is null
     */
    public void triggerActivation(Activation activationFunction) {
    
        // Test for exception.
        if (activationFunction == null) {
        
            throw new InvalidActivationException("'activationFunction' must not be null");
        }
        
        // Trigger activation value.
        activationValue = activationFunction.f(activationSum);
    }
    
    /**
     * Compute this Node's delta value using
     * its delta sum and an activation function.
     * @param activationFunction activation function to use
     * @throws InvalidActivationException if parameter 'activationFunction' is null
     */
    public void triggerDelta(Activation activationFunction) {
    
        // Test for exception.
        if (activationFunction == null) {
        
            throw new InvalidActivationException("'activationFunction' must not be null");
        }
        
        // Trigger delta value.
        deltaValue = deltaSum * activationFunction.df(activationSum);
    }
    
    /**
     * Clear this Node's activation and delta sums.
     */
    public void clearSums() {
    
        activationSum = 0.0;
        deltaSum = 0.0;
    }
}
