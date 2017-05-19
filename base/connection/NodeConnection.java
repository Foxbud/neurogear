package neurogear.base.connection;

import neurogear.base.node.Node;
import neurogear.base.regularization.Regularization;

/**
 * Subclass of Connection connecting two Nodes.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NodeConnection.java
 * Created: 04/04/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Allows two Nodes to be connected
 * by a weight.
 */
public final class NodeConnection extends Connection {
    
    // MEMBER VARIABLES.
    
    // Input Node.
    private Node inputNode;
    
    // MEMBER METHODS.
    
    /**
     * Construct a NodeConnection with passed weight.
     * @param weightP weight value
     */
    public NodeConnection(double weightP) {
    
        super(weightP);
        
        inputNode = null;
    }
    
    /**
     * Set this Connection's input Node.
     * @param inputNodeP input node
     * @throws InputOverrideException if Connection already has intput
     * @throws InvalidInputException if parameter 'inputNodeP' is null
     */
    @Override
    public void setInput(Node inputNodeP) {
    
        // Test for exceptions.
        if (inputNode != null) {
        
            throw new InputOverrideException("cannot override existing input without clearing it");
        }
        else if (inputNodeP == null) {
        
            throw new InvalidInputException("'inputNodeP' must not be null");
        }

        inputNode = inputNodeP;
    }
    
    /**
     * Clear this Connection's input Node.
     */
    @Override
    public void clearInput() {
    
        inputNode = null;
    }
    
    /**
     * Weight and relay the input Node's activation 
     * value to the output Node's activation sum.
     * @throws InvalidOutputException if Connection's output Node is null
     * @throws InvalidInputException if Connection's input Node is null
     */
    @Override
    public void propagate() {
    
        // Test for exceptions.
        if (outputNode == null) {
        
            throw new InvalidOutputException("must assign a Node to this Connection's output");
        }
        else if (inputNode == null) {
        
            throw new InvalidInputException("must assign a Node to this Connection's input");
        }
        
        // Relay activation.
        outputNode.addToActivationSum(weight * inputNode.getActivationValue());
    }
    
    /**
     * Weight and relay the output Node's delta 
     * value to the inputs Node's delta sum and
     * add delta to this Connection's 
     * internal delta sum.
     * @throws InvalidOutputException if Connection's output Node is null
     * @throws InvalidInputException if Connection's input Node is null
     */
    @Override
    public void backpropagate() {
    
        // Test for exceptions.
        if (outputNode == null) {
        
            throw new InvalidOutputException("must assign a Node to this Connection's output");
        }
        else if (inputNode == null) {
        
            throw new InvalidInputException("must assign a Node to this Connection's input");
        }
        
        // Temporary delta value.
        double outputDelta = outputNode.getDeltaValue();
        
        // Relay delta.
        inputNode.addToDeltaSum(weight * outputDelta);
        
        // Add delta.
        deltaSum += inputNode.getActivationValue() * outputDelta;
        numDelta++;
    }
    
    // HELPER METHODS.
    
    /**
     * Calculate this Connection's regularization value.
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @return regularization value
     */
    @Override
    protected double computeRegularization(Regularization regFunction, double regParameter) {
            
        return regFunction.df(regParameter, weight);
    }
}
