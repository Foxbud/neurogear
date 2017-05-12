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
     * Retrieve the weighted input bias.
     * @return weight * input bias.
     */
    @Override
    public double upstream() {
    
        return weight * INPUTVALUE;
    }
    
    /**
     * Here for compatability; will throw exception if called.
     * @throws BadUsageException if called
     */
    @Override
    public double downstream() {
    
        // Method not supported.
        throw new BadUsageException("BiasConnection does not support 'downstream()'");
    }
    
    /**
     * Add current delta to the delta sum by multiplying
     * input bias by output Node's delta.
     * @throws InvalidOutputException if output is of incorrect type
     */
    @Override
    public void update() {
    
        // Test for exception.
        if (!(outputNode instanceof Node)) {
            
            throw new InvalidOutputException("output was not of type 'Node'");
        }
        else {
            
            deltaSum += INPUTVALUE * outputNode.getDelta();
            numDelta++;
        }
    }
    
    // HELPER METHODS.
    
    /**
     * Return 0.0 because bias Connections are not regularized.
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @return 0.0
     * @throws InvalidRegularizationException if parameter 'regFunction' is of incorrect type
     */
    @Override
    protected double computeRegularization(Regularization regFunction, double regParameter) {
    
        // Test for exception.
        if (!(regFunction instanceof Regularization)) {
            
            throw new InvalidRegularizationException("'regFunction' must be of type 'Regularization'");
        }
        else {
            
            return 0.0;
        }
    }
}
