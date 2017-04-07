package neurogear.base.connection;

import neurogear.base.node.Node;
import neurogear.base.regularization.Regularization;

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
    private static final int INPUTVALUE = 1;
    // Output Node.
    private final Node OUTPUTNODE;
    
    // MEMBER METHODS.
    
    /**
     * Construct a BiasConnection with given parameters.
     * @param outputNodeP output node
     * @param weightP weight value
     * @throws neurogear.base.connection.ConnectionException
     */
    public BiasConnection(Node outputNodeP, double weightP) throws ConnectionException {
    
        super(weightP);
        
        // Test for exception.
        if (outputNodeP instanceof Node) {
            
            OUTPUTNODE = outputNodeP;
        }
        else {
        
            throw new InvalidOutputException("'outputNodeP' must be of type 'Node'");
        }
    }
    
    /**
     * Retrieve the weighted input bias.
     * @return weight * input bias.
     */
    @Override
    public double upstream() {
    
        return weight * INPUTVALUE;
    }
    
    /**
     * Add current delta to the delta sum by multiplying
     * input bias by output Node's delta.
     * @throws neurogear.base.connection.ConnectionException
     */
    @Override
    public void update() throws ConnectionException {
    
        // Test for exception.
        if (OUTPUTNODE instanceof Node) {
            
            deltaSum += INPUTVALUE * OUTPUTNODE.getDelta();
            numDelta++;
        }
        else {
        
            throw new InvalidOutputException("output was not of type 'Node'");
        }
    }
    
    /**
     * Return 0.0 because bias Connections are not regularized.
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @return 0.0
     */
    @Override
    protected double computeRegularization(Regularization regFunction, double regParameter) throws ConnectionException {
    
        // Test for exception.
        if (regFunction instanceof Regularization) {
        
            return 0.0;
        }
        else {
        
            throw new InvalidRegularizationException("'regFunction' must be of type 'Regularization'");
        }
    }
}
