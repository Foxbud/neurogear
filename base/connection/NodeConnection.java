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
    private final Node INPUTNODE;
    // Output Node.
    private final Node OUTPUTNODE;
    
    // MEMBER METHODS.
    
    /**
     * Construct a NodeConnection with given parameters.
     * @param inputNodeP input node
     * @param outputNodeP output node
     * @param weightP weight value
     * @throws neurogear.base.connection.ConnectionException
     */
    public NodeConnection(Node inputNodeP, Node outputNodeP, double weightP) throws ConnectionException {
    
        super(weightP);
        
        // Test for exception.
        if (inputNodeP instanceof Node) {
            
            INPUTNODE = inputNodeP;
        }
        else {
        
            throw new InvalidInputException("'inputNodeP' must be of type 'Node'");
        }
        
        // Test for exception.
        if (outputNodeP instanceof Node) {
            
            OUTPUTNODE = outputNodeP;
        }
        else {
        
            throw new InvalidOutputException("'outputNodeP' must be of type 'Node'");
        }
    }
    
    /**
     * Retrieve the weighted activation of the input Node.
     * @return weight * input Node's activation
     * @throws neurogear.base.connection.ConnectionException
     */
    @Override
    public double upstream() throws ConnectionException {
    
        // Test for exception.
        if (INPUTNODE instanceof Node) {
            
            return weight * INPUTNODE.getActivation();
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
        if (OUTPUTNODE instanceof Node) {
            
            return weight * OUTPUTNODE.getDelta();
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
        if (!(INPUTNODE instanceof Node)) {
        
            throw new InvalidInputException("input was not of type 'Node'");
        }
        else if (!(OUTPUTNODE instanceof Node)) {
        
            throw new InvalidOutputException("output was not of type 'Node'");
        }
        else {
            
            deltaSum += INPUTNODE.getActivation() * OUTPUTNODE.getDelta();
            numDelta++;
        }
    }
    
    /**
     * Calculate this Connection's regularization value.
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @return regularization value
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
