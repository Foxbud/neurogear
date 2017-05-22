package neurogear.utilities.layer;

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
public final class Kernel {
    
    /*
    Outline:
    
    Data:
    array of NodeConnections
    array of NodeConnection offsets
    BiasConnection
    array of input Nodes
    array of output Nodes
    stride length
    
    Methods:
    constructor(array of offsets, array of weights, stride size)
    constructor(number of fully connected, array of weights, stride size)
    connectOutputNodes(array of Node)
    disconnectOutputNodes
    connectInputNodes(array of Nodes)
    disconnectInputNodes
    getWeights
    setWeights(array of weights)
    propagateAll
    backpropagateAll
    correct(hyper parameters)
    
    Helper Methods:
    genericConstructor(array of weights, stride size)
    verifyValidConnections
    propagateOnce(stride)
    backpropagateOnce(stride)
    */
    
    // MEMEBR VARIABLES.
    
    
}
