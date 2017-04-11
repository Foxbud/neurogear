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
     * Construct a Connection with passed weight.
     * @param weightP weight value
     */
    public NodeConnection(double weightP) {
    
        super(weightP);
        
        inputNode = null;
    }
    
    /**
     * Set this Connection's input Node.
     * @param inputNodeP input node
     * @throws neurogear.base.connection.ConnectionException
     */
    public void setInput(Node inputNodeP) throws ConnectionException {
    
        // Test for exceptions.
        if (inputNode != null) {
        
            throw new inputOverrideException("cannot override existing input without clearing it");
        }
        else if (!(inputNodeP instanceof Node)) {
        
            throw new InvalidInputException("'inputNodeP' must be of type 'Node'");
        }
        else {
        
            inputNode = inputNodeP;
        }
    }
    
    /**
     * Clear this Connection's input Node.
     */
    public void clearInput() {
    
        inputNode = null;
    }
    
    /**
     * Retrieve the weighted activation of the input Node.
     * @return weight * input Node's activation
     * @throws neurogear.base.connection.ConnectionException
     */
    @Override
    public double upstream() throws ConnectionException {
    
        // Test for exception.
        if (inputNode instanceof Node) {
            
            return weight * inputNode.getActivation();
        }
        else {
        
            throw new InvalidInputException("input was not of type 'Node'");
        }
    }
    
    /**
     * Retrieve the weighted delta of the output Node.
     * @return weight * output Node's delta
     * @throws neurogear.base.connection.ConnectionException
     */
    public double downstream() throws ConnectionException {
    
        // Test for exception.
        if (outputNode instanceof Node) {
            
            return weight * outputNode.getDelta();
        }
        else {
        
            throw new InvalidOutputException("output was not of type 'Node'");
        }
    }
    
    /**
     * Add current delta to the delta sum by multiplying
     * input Node's activation by output Node's delta.
     * @throws neurogear.base.connection.ConnectionException
     */
    @Override
    public void update() throws ConnectionException {
    
        // Test for exceptions.
        if (!(inputNode instanceof Node)) {
        
            throw new InvalidInputException("input was not of type 'Node'");
        }
        else if (!(outputNode instanceof Node)) {
        
            throw new InvalidOutputException("output was not of type 'Node'");
        }
        else {
            
            deltaSum += inputNode.getActivation() * outputNode.getDelta();
            numDelta++;
        }
    }
    
    /**
     * Calculate this Connection's regularization value.
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @return regularization value
     * @throws neurogear.base.connection.ConnectionException
     */
    @Override
    protected double computeRegularization(Regularization regFunction, double regParameter) throws ConnectionException {
    
        // Test for exception.
        if (regFunction instanceof Regularization) {
        
            return regFunction.df(regParameter, weight);
        }
        else {
        
            throw new InvalidRegularizationException("'regFunction' must be of type 'Regularization'");
        }
    }
}
