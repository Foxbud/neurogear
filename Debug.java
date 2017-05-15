package neurogear;

import java.util.ArrayList;
import java.util.Random;
import neurogear.base.node.*;
import neurogear.base.connection.*;
import neurogear.base.activation.*;
import neurogear.base.cost.*;
import neurogear.base.regularization.*;
import neurogear.data.dataset.*;
import neurogear.data.datum.*;
import neurogear.data.scale.*;

/**
 * Debugging class.
 * 
 * @author Garrett Russell Fairburn
 * @version 1.0
 * File: NeuroGear.java
 * Created: 04/07/17
 * Copyright (c) 2017, Garrett Russell Fairburn, All rights reserved.
 * Summary of Modifications:
 *  N/A
 * 
 * Description: Messy class for debugging. It should not appear
 * in release builds. If it does, I'm an idiot.
 */
public class Debug {

    /**
     * Debugging method.
     * @param args command line parameters
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws java.io.IOException {
        
        // Hyperparameters.
        int seed =                          2347;
        int topology[] =                    {3, 9, 6, 3, 1};
        Activation functions[] =            {
                                                new IdentityActivation(), 
                                                new LeakyReLUActivation(), 
                                                new LeakyReLUActivation(), 
                                                new TanHActivation(), 
                                                new IdentityActivation()
                                            };
        Cost costFunction =                 new QuadraticCost();
        Regularization regFunction =        new L2Regularization();
        double learningRate =               0.05;
        double regParameter =               0.001;
        int numEpochs =                     128;
        int miniBatchSize =                 8;
        int numMiniBatch =                  128;
        
        // Variables.
        Random gen = new Random(seed);
        ArrayList<Connection> connections = new ArrayList<>(0);
        ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
        DataSet data = new DataSet(seed);
        Scale rawScale = new StandardScale();
        Scale labelScale = new StandardScale();
        
        // Create layers.
        for (int i = 0; i < topology.length; i++) {
        
            nodes.add(new ArrayList<>());
            
            // Create nodes.
            for (int j = 0; j < topology[i]; j++) {
            
                nodes.get(i).add(new Node());
            }
        }
        
        // Interconnect layers.
        for (int i = 1; i < topology.length; i++) {
        
            // Iterate through current layer nodes.
            for (int j = 0; j < topology[i]; j++) {
            
                // Iterate through previous layer ndoes.
                for (int k = 0; k < topology[i - 1]; k++) {
                
                    // Create new node connection.
                    NodeConnection nodeCon = new NodeConnection(gen.nextGaussian() / Math.sqrt(topology[i - 1]));
                    
                    // Add connection to connections.
                    connections.add(nodeCon);
                    
                    // Connect new node connection.
                    nodes.get(i - 1).get(k).connectOutput(nodeCon);
                    nodes.get(i).get(j).connectInput(nodeCon);
                }
                
                // Create new bias connection.
                BiasConnection biasCon = new BiasConnection(0.0);
                
                // Add connection to connections.256
                connections.add(biasCon);
                
                // Connect new bias connection.
                nodes.get(i).get(j).connectInput(biasCon);
            }
        }
        
        // Create data set.
        for (int i = 0; i < miniBatchSize * numMiniBatch; i++) {
        
            double result = 1.0;
            
            // Create raw data.
            Double raw[] = new Double[topology[0]];
            for (int j = 0; j < topology[0]; j++) {
            
                raw[j] = ((gen.nextDouble() - 0.5) * 100.0);
                
                result *= raw[j];
            }
            
            // Created label data.
            Double label[] = new Double[topology[topology.length - 1]];
            for (int j = 0; j < topology[topology.length - 1]; j++) {
            
                label[j] = result;
            }
            
            // Add data to data set.
            data.addDatum(new LabeledDatum(raw, label));
        }
        data.resetBuffer();
        data.saveToFile("TestDataSet.txt");
        
        // Create scales.
        rawScale.computeScalingFactors(data.presentRaw());
        labelScale.computeScalingFactors(data.presentLabel());
        
        // Epochs.
        for (int i = 0; i < numEpochs; i++) {
        
            // Whole data set.
            while (data.hasNextBuffer(miniBatchSize)) {
            
                // Mini-batch.
                for (int k = 0; k < miniBatchSize; k++) {
                
                    // Get next Datum.
                    LabeledDatum curDatum = (LabeledDatum)data.getNextBuffer();
                    
                    // Initial propagation.
                    Double rawData[] = rawScale.scaleDown(curDatum.getRaw());
                    for (int l = 0; l < topology[0]; l++) {
                    
                        nodes.get(0).get(l).propagate(functions[0], rawData[l]);
                    }
                    
                    // Propagation.
                    for (int l = 1; l < topology.length; l++) {
                    
                        for (int m = 0; m < topology[l]; m++) {
                        
                            nodes.get(l).get(m).propagate(functions[l]);
                        }
                    }
                    
                    // Print errors.
                    Double labelData[] = labelScale.scaleDown(curDatum.getLabel());
                    for (int l = 0; l < topology[topology.length - 1]; l++) {
                    
                        System.out.printf(" %f :", Math.abs(nodes.get(topology.length - 1).get(l).getActivation() - labelData[l]));
                    }
                    System.out.printf("%n");
                    
                    // Initial backpropagation.
                    for (int l = 0; l < topology[topology.length - 1]; l++) {
                    
                        nodes.get(topology.length - 1).get(l).backpropagate(functions[topology.length - 1], labelData[l], costFunction);
                    }
                    
                    // Backpropagation.
                    for (int l = topology.length - 2; l > 0; l--) {
                    
                        for (int m = 0; m < topology[l]; m++) {
                        
                            nodes.get(l).get(m).backpropagate(functions[l]);
                        }
                    }
                    
                    // Update connections.
                    for (int l = 0; l < connections.size(); l++) {
                    
                        connections.get(l).update();
                    }
                }
                
                // Correct connections.
                for (int k = 0; k < connections.size(); k++) {
                
                    connections.get(k).correct(learningRate, regFunction, regParameter);
                }
            }
            
            data.resetBuffer();
        }
        
        // Print weights.
        for (int i = 0; i < connections.size(); i++) {

            System.out.printf("%f%n", connections.get(i).getWeight());
        }

        int catcher = 0;
    }
}
