package com.fairburn.neurogear.base.connection;

import com.fairburn.neurogear.base.node.Node;
import com.fairburn.neurogear.base.regularization.Regularization;

/**
 * Subclass of Connection connecting a Node to a bias.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: BiasConnection.java
 * Created: 04/04/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Allows a Node and a bias to be 
 * connected by a weight.
 */
public final class BiasConnection extends Connection {
    
    // MEMBER VARIABLES.
    
    // Input bias constant.
    private static final double INPUT_VALUE = 1.0;
    
    // MEMBER METHODS.
    
    /**
     * Construct a BiasConnection with passed weight.
     * @param weightP weight value
     */
    public BiasConnection(double weightP) {
    
        super(weightP);
    }
    
    /**
     * Here for compatability; will throw exception if called.
     * @param inputNodeP input node
     * @throws BadUsageException if called
     */
    @Override
    public void setInput(Node inputNodeP) {
    
        // Method not supported.
        throw new BadUsageException("BiasConnection does not support 'setInput()'");
    }
    
    /**
     * Here for compatability; will throw exception if called.
     * @throws BadUsageException if called
     */
    @Override
    public void clearInput() {
    
        // Method not supported.
        throw new BadUsageException("BiasConnection does not support 'clearInput()'");
    }
    
    /**
     * Weight and relay the bias value 
     * to the output Node's activation sum.
     * @throws InvalidOutputException if Connection's output Node is null
     */
    @Override
    public void propagate() {
    
        // Test for exception.
        if (outputNode == null) {
        
            throw new InvalidOutputException("must assign a Node to this Connection's output");
        }
        
        // Relay activation.
        outputNode.addToActivationSum(weight * INPUT_VALUE);
    }
    
    /**
     * Add delta to this Connection's 
     * internal delta sum.
     * @throws InvalidOutputException if Connection's output Node is null
     */
    @Override
    public void backpropagate() {
    
        // Test for exception.
        if (outputNode == null) {
        
            throw new InvalidOutputException("must assign a Node to this Connection's output");
        }
        
        // Add delta.
        deltaSum += INPUT_VALUE * outputNode.getDeltaValue();
        numDelta++;
    }
    
    // HELPER METHODS.
    
    /**
     * Return 0.0 because bias Connections are not regularized.
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @return 0.0
     */
    @Override
    protected double computeRegularization(Regularization regFunction, double regParameter) {
    
        return 0.0;
    }
}
