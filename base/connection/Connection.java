package neurogear.base.connection;

import neurogear.base.node.Node;
import neurogear.base.regularization.Regularization;

/**
 * Abstract parent class for connections.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Connection.java
 * Created: 04/02/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Specifies requirements for an object
 * connecting two objects with a weight.
 */
public abstract class Connection {
    
    // MEMBER VARIABLES.
    
    // Learning parameter.
    double weight;
    
    // Delta sum.
    double deltaSum;
    // Number of deltas in delta sum.
    int numDelta;
    
    // Output Node.
    Node outputNode;
    
    // MEMBER METHODS.
    
    /**
     * Construct a Connection with passed weight.
     * @param weightP weight value
     */
    Connection(double weightP) {
    
        weight = weightP;
        
        deltaSum = 0.0;
        numDelta = 0;
        
        outputNode = null;
    }
    
    /**
     * Set this Connection's weight value.
     * @param weightP weight value
     */
    public void setWeight(double weightP) {
    
        weight = weightP;
    }
    
    /**
     * Return this Connection's weight value.
     * @return weight value
     */
    public double getWeight() {
    
        return weight;
    }
    
    /**
     * Set this Connection's output Node.
     * @param outputNodeP output node
     * @throws OutputOverrideException
     * @throws InvalidOutputException
     */
    public void setOutput(Node outputNodeP) throws OutputOverrideException, InvalidOutputException {
    
        // Test for exceptions.
        if (outputNode != null) {
        
            throw new OutputOverrideException("cannot override existing output without clearing it");
        }
        else if (!(outputNodeP instanceof Node)) {
        
            throw new InvalidOutputException("'outputNodeP' must be of type 'Node'");
        }
        else {
        
            outputNode = outputNodeP;
        }
    }
    
    /**
     * Clear this Connection's output Node.
     */
    public void clearOutput() {
    
        outputNode = null;
    }
    
    /**
     * Retrieve the weighted activation of the input.
     * @return weight * input's activation
     */
    public abstract double upstream();
    
    /**
     * Add current delta to the delta sum.
     */
    public abstract void update();
    
    /**
     * Use current internal batch of deltas
     * and regularization to correct the
     * weight of this Connection and clear delta
     * sum for next batch.
     * @param learningRate learning factor
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @throws NullDeltaException
     */
    public void correct(double learningRate, Regularization regFunction, double regParameter) throws NullDeltaException {
    
        // Test for exception.
        if (numDelta > 0) {
            
            // Correct weight.
            weight -= learningRate * (deltaSum / numDelta + computeRegularization(regFunction, regParameter));

            // Clear delta values.
            deltaSum = 0.0;
            numDelta = 0;
        }
        else {
        
            throw new NullDeltaException("'correct()' called with no deltas");
        }
    }
    
    /**
     * Calculate this Connection's regularization value.
     * @param regFunction regularization function
     * @param regParameter regularization parameter
     * @return regularization value
     */
    protected abstract double computeRegularization(Regularization regFunction, double regParameter);
}
