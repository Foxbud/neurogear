package neurogear.utilities.layer;

import neurogear.base.connection.Connection;
import neurogear.base.node.Node;

/**
 * Collection of Connections shared
 * by a group of Nodes in a Layer.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: Kernel.java
 * Created: 05/19/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Used to search for a particular
 * feature within data in a location independent
 * manner.
 */
public class Kernel {
    
    /*
    Outline:
    
    Data:
    array of Connections
    array of Connection offsets
    stride size
    current stride offset
    output Node
    array of input Nodes
    
    Methods:
    constructor(array of offsets, stride size)
    constructor(number of fully connected, stride size)
    connectOutputNode(Node)
    disconnectOutputNode
    connectInputNodes(array of Nodes)
    disconnectInputNodes
    takeStride
    resetStride
    getWeights
    setWeights(array of weights)
    propagate
    backpropagate
    correct(hyper parameters)
    */
    
    // MEMEBR VARIABLES.
    
    // Connections that represent this Kernel.
    private final Connection connections[];
    // Offset of each Connection w/ respect to the stride offset.
    private final int connectionOffsets[];
    
    // Output Node o this Kernel.
    private Node outputNode;
    // Input Node array to this Kernel.
    private Node inptuNodes[];
    
    // Number of input Nodes to step over with each stride.
    private final int strideSize;
    // Current stride offset w/ respect to start of input Node array.
    private int strideOffset;
    
    // MEMBER METHODS.
    
    public Kernel(int offsets[], int strideSizeP) {
    
        // Create Connections.
        connections = new Connection[offsets.length];
        
        // Create offset array.
        connectionOffsets = offsets.clone();
        
        strideSize = strideSizeP;
    }
}
