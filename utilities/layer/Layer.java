package neurogear.utilities.layer;

import neurogear.base.activation.Activation;
import neurogear.base.connection.Connection;
import neurogear.base.node.Node;

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
public class Layer {
    
    // MEMBER VARIABLES.
    
    // Activation function for Nodes.
    private final Activation nodeType;
    // Nodes.
    private final Node nodes[];
    // Node input Connections.
    private Connection inputConnections[];
    
    // MEMBER METHODS.
    
    public Layer(int numNodes, Activation nodeTypeP) {
    
        nodeType = nodeTypeP;
        nodes = new Node[numNodes];
    }
}
