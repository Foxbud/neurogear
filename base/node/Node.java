package neurogear.base.node;

import java.util.ArrayList;
import neurogear.base.connection.Connection;
import neurogear.base.connection.NodeConnection;
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
public class Node {
    
    // MEMBER VARIABLES.
    
    // Input connections.
    private final ArrayList<Connection> inputs;
    // Output connections.
    private final ArrayList<NodeConnection> outputs;
    
    // Incoming weighted sum.
    private double sum;
    // Outgoing activation.
    private double activation;
    // Backpropagation error.
    private double delta;
    
    // MEMBER METHODS.
    
    /**
     * Construct a Node.
     */
    public Node() {
    
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        
        sum = 0.0;
        activation = 0.0;
        delta = 0.0;
    }
    
    /**
     * Return this Node's activation value.
     * @return activation value
     */
    public double getActivation() {
    
        return activation;
    }
    
    /**
     * Return this Node's delta value.
     * @return delta value
     */
    public double getDelta() {
    
        return delta;
    }
    
    /**
     * Specify a Connection to add to this Node's inputs.
     * @param inputConnection input connection to add
     * @throws neurogear.base.node.NodeException
     */
    public void connectInput(Connection inputConnection) throws NodeException {
    
        // Test for exception.
        if (inputConnection instanceof Connection) {
        
            // New connection.
            inputs.add(inputConnection);
        }
        else {
        
            throw new InvalidConnectionException("'inputConnection' must be of type 'Connection'");
        }
    }
    
    /**
     * Remove all input Connections from this Node.
     */
    public void clearInputs() {
    
        inputs.clear();
    }
    
    /**
     * Specify a Connection to add to this Node's outputs.
     * @param outputConnection output connection to add
     * @throws neurogear.base.node.NodeException
     */
    public void connectOutput(NodeConnection outputConnection) throws NodeException {
    
        // Test for exception.
        if (outputConnection instanceof NodeConnection) {
        
            // New connection.
            outputs.add(outputConnection);
        }
        else {
        
            throw new InvalidConnectionException("'outputConnection' must be of type 'NodeConnection'");
        }
    }
    
    /**
     * Remove all output Connections from this Node.
     */
    public void clearOutputs() {
    
        outputs.clear();
    }
    
    /**
     * Use all input Connections to get a weighted input sum and then
     * use the passed activation function to activate the sum
     * (note that all values are stored internally within the node).
     * @param activationFunction activation function
     * @throws neurogear.base.node.NodeException
     */
    public void propagate(Activation activationFunction) throws NodeException {
    
        // Test for exception.
        if (inputs.size() <= 0) {
        
            throw new BadPropagateException("cannot use this 'propagate()' on Nodes with no input");
        }
        
        // Sum of all inputs.
        sum = 0.0;
        
        // Sum all weighted inputs excluding the bias.
        for (int i = 0; i < inputs.size(); i++) {
        
            // Test for exception.
            if (inputs.get(i) instanceof Connection) {

                sum += inputs.get(i).upstream();
            }
            else {

                throw new InvalidConnectionException("input connection " + i + " was not of type 'Connection'");
            }
        }
        
        // Test for exception.
        if (activationFunction instanceof Activation) {
        
            activation = activationFunction.f(sum);
        }
        else {
        
            throw new InvalidActivationException("'activationFunction' was not of type 'Activation'");
        }
    }
    
    /**
     * Alternate propagate method for input Nodes which
     * directly sets the sum with a given parameter.
     * @param activationFunction activation function
     * @param initialInput input value
     * @throws neurogear.base.node.NodeException
     */
    public void propagate(Activation activationFunction, double initialInput) throws NodeException {
    
        sum = initialInput;
        
        // Test for exception.
        if (activationFunction instanceof Activation) {
        
            // Activate sum.
            activation = activationFunction.f(sum);
        }
        else {
        
            throw new InvalidActivationException("'activationFunction' was not of type 'Activation'");
        }
    }
    
    /**
     * Use all output Connections to get a weighted delta sum and then
     * use the passed activation function to derive the delta
     * (note that all values are stored internally within the node).
     * @param activationFunction activation function
     * @throws neurogear.base.node.NodeException
     */
    public void backpropagate(Activation activationFunction) throws NodeException {
    
        // Test for exception.
        if (outputs.size() <= 0) {
        
            throw new BadBackpropagateException("cannot use this 'backpropagate()' on Nodes with no output");
        }
        
        // Sum of all output deltas.
        delta = 0.0;
        
        // Sum all weighted output deltas.
        for (int i = 0; i < outputs.size(); i++) {
        
            // Test for exception.
            if (outputs.get(i) instanceof NodeConnection) {

                delta += outputs.get(i).downstream();
            }
            else {

                throw new InvalidConnectionException("output connection " + i + " was not of type 'NodeConnection'");
            }
        }
        
        // Test for exception.
        if (activationFunction instanceof Activation) {
        
            // Activate delta.
            delta *= activationFunction.df(sum);
        }
        else {
        
            throw new InvalidActivationException("'activationFunction' was not of type 'Activation'");
        }
    }
    
    /**
     * Alternate backpropagate method for ouput Nodes which
     * directly sets the delta with given parameters.
     * @param activationFunction activation function
     * @param targetValue ideal output
     * @param costFunction cost function to use
     * @throws neurogear.base.node.NodeException
     */
    public void backpropagate(Activation activationFunction, double targetValue, Cost costFunction) throws NodeException {
    
        // Test for exceptions.
        if (!(costFunction instanceof Cost)) {
        
            throw new InvalidCostException("'costFunction' must be of type 'Cost'");
        }
        else if (!(activationFunction instanceof Activation)) {
        
            throw new InvalidActivationException("'activationFunction' was not of type 'Activation'");
        }
        else {
        
            // Activate delta.
            delta = costFunction.df(activation, targetValue) * activationFunction.df(sum);
        }
    }
}
