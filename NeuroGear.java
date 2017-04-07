package neurogear;

import java.util.ArrayList;
import java.util.Random;
import neurogear.base.node.*;
import neurogear.base.connection.*;
import neurogear.base.activation.*;
import neurogear.base.cost.*;

/*
Debugging class.
*/
public class NeuroGear {

    /*
    Debugging function.
    */
    public static void main(String[] args) {
    
        // Hyperparameters.
        int seed =                          89035;
        int topology[] =                    {5, 1, 5};
        Activation functions[] =            {
                                                new IdentityActivation(), 
                                                new TanHActivation(), 
                                                new LogisticActivation()
                                            };
        Cost costFunction =                 new CrossEntropyCost();
        double learningRate =               0.1;
        int numEpochs =                     10;
        int miniBatchSize =                 10;
        int numMiniBatch =                  100;
        
        // Variables.
        Random gen = new Random(seed);
        ArrayList<Connection> connections = new ArrayList<>(0);
        ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
        Double inputData[][] = new Double[miniBatchSize * numMiniBatch][topology[0]];
        Double outputData[][] = new Double[miniBatchSize * numMiniBatch][topology[topology.length - 1]];
        
        // Create layers.
        for (int i = 0; i < topology.length; i++) {
        
            nodes.add(new ArrayList<>());
            
            // Create nodes.
            for (int j = 0; j < topology[i]; j++) {
            
                nodes.get(i).add(new Node(functions[i]));
            }
        }
        
        // Interconnect layers.
        for (int i = 1; i < topology.length; i++) {
        
            // Iterate through current layer nodes.
            for (int j = 0; j < topology[i]; j++) {
            
                // Iterate through previous layer ndoes.
                for (int k = 0; k < topology[i - 1]; k++) {
                
                    // Create new node connection.
                    NodeConnection nodeCon = new NodeConnection(nodes.get(i - 1).get(k), nodes.get(i).get(j), gen.nextGaussian() / Math.sqrt(topology[i - 1]));
                    
                    // Add connection to connections.
                    connections.add(nodeCon);
                    
                    // Connect new node connection.
                    nodes.get(i - 1).get(k).connectOutput(nodeCon);
                    nodes.get(i).get(j).connectInput(nodeCon);
                }
                
                // Create new bias connection.
                BiasConnection biasCon = new BiasConnection(nodes.get(i).get(j), 0.0);
                
                // Add connection to connections.
                connections.add(biasCon);
                
                // Connect new bias connection.
                nodes.get(i).get(j).connectInput(biasCon);
            }
        }
        
        // Create data sets.
        for (int i = 0; i < miniBatchSize * numMiniBatch; i++) {
        
            // Create single sample.
            inputData[i][0] = gen.nextDouble();
            for (int j = 1; j < topology[0]; j++) {
            
                inputData[i][j] = inputData[i][j - 1] / 2;
            }
            for (int j = 0; j < topology[topology.length - 1]; j++) {
            
                outputData[i][j] = inputData[i][j];
            }
        }
        
        // Epochs.
        for (int i = 0; i < numEpochs; i++) {
        
            // Whole data set.
            for (int j = 0; j < numMiniBatch; j++) {
            
                // Mini-batch.
                for (int k = 0; k < miniBatchSize; k++) {
                
                    // Initial propagation.
                    for (int l = 0; l < topology[0]; l++) {
                    
                        nodes.get(0).get(l).propagate(inputData[j * miniBatchSize + k][l]);
                    }
                    
                    // Propagation.
                    for (int l = 1; l < topology.length; l++) {
                    
                        for (int m = 0; m < topology[l]; m++) {
                        
                            nodes.get(l).get(m).propagate();
                        }
                    }
                    
                    // Print errors.
                    for (int l = 0; l < topology[topology.length - 1]; l++) {
                    
                        System.out.printf(" %f :", Math.abs(nodes.get(topology.length - 1).get(l).getActivation() - outputData[j * miniBatchSize + k][l]));
                    }
                    System.out.printf("%n");
                    
                    // Initial backpropagation.
                    for (int l = 0; l < topology[topology.length - 1]; l++) {
                    
                        nodes.get(topology.length - 1).get(l).backpropagate(outputData[j * miniBatchSize + k][l], costFunction);
                    }
                    
                    // Backpropagation.
                    for (int l = topology.length - 2; l > 0; l--) {
                    
                        for (int m = 0; m < topology[l]; m++) {
                        
                            nodes.get(l).get(m).backpropagate();
                        }
                    }
                    
                    // Update connections.
                    for (int l = 0; l < connections.size(); l++) {
                    
                        connections.get(l).update();
                    }
                }
                
                // Correct connections.
                for (int k = 0; k < connections.size(); k++) {
                
                    connections.get(k).correct(learningRate);
                }
            }
        }
        
        int catcher = 0;
    }
}
