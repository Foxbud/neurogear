package neurogear;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import neurogear.utilities.layer.Layer;

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
        
        /**/
        int seed = 24988;
        double learningRate = 0.5;
        double regParameter = 0.00001;
        Cost costFunction = new CrossEntropyCost();
        int batchSize = 32;
        int numEpochs = 32;
        
        DataSet trainingSet = new DataSet(seed);
        trainingSet.loadFromFile("tr.dat");
        trainingSet.resetBuffer();
        DataSet testingSet = new DataSet(seed + 1);
        testingSet.loadFromFile("te.dat");
        testingSet.resetBuffer();
        
        Layer inputLayer = new Layer(1, 112, new IdentityActivation());
        Layer hiddenLayerA = new Layer(16, 15, new LeakyReLUActivation(), new NullRegularization(), sequence(14), 1, 7, seed + 2);
        Layer hiddenLayerB = new Layer(32, 7, new LeakyReLUActivation(), new NullRegularization(), sequence(3), 16, 2, seed + 3);
        Layer hiddenLayerC = new Layer(64, 3, new LeakyReLUActivation(), new NullRegularization(), sequence(3), 32, 2, seed + 4);
        Layer outputLayer = new Layer(7, 1, new LogisticActivation(), new L2Regularization(), sequence(3), 64, 1, seed + 5);

        hiddenLayerA.connect(inputLayer);
        hiddenLayerB.connect(hiddenLayerA);
        hiddenLayerC.connect(hiddenLayerB);
        outputLayer.connect(hiddenLayerC);
        
        for (int i = 0; i < numEpochs; i++) {
        
            while (trainingSet.hasNextBuffer(batchSize)) {
            
                for (int j = 0; j < batchSize; j++) {
                
                    LabeledDatum curDatum = (LabeledDatum)trainingSet.getNextBuffer();
                
                    inputLayer.propagate(new double[][]{curDatum.getRaw()});
                    hiddenLayerA.propagate();
                    hiddenLayerB.propagate();
                    hiddenLayerC.propagate();
                    outputLayer.propagate();
                    
                    double rawTarget[] = curDatum.getLabel();
                    double formattedTarget[][] = new double[curDatum.getLabel().length][1];
                    for (int k = 0; k < curDatum.getLabel().length; k++) {
                    
                        formattedTarget[k][0] = rawTarget[k];
                    }
                    
                    outputLayer.backpropagate(formattedTarget, costFunction);
                    hiddenLayerC.backpropagate();
                    hiddenLayerB.backpropagate();
                    hiddenLayerA.backpropagate();
                    
                    outputLayer.clearNodeSums();
                    hiddenLayerC.clearNodeSums();
                    hiddenLayerB.clearNodeSums();
                    hiddenLayerA.clearNodeSums();
                    inputLayer.clearNodeSums();
                }
                
                outputLayer.correctKernels(learningRate, regParameter);
                hiddenLayerC.correctKernels(learningRate, regParameter);
                hiddenLayerB.correctKernels(learningRate, regParameter);
                hiddenLayerA.correctKernels(learningRate, regParameter);
            }
            
            double avgErr = 0.0;
            
            for (int j = 0; j < testingSet.size(); j++) {
            
                LabeledDatum curDatum = (LabeledDatum)testingSet.getNextBuffer();
                
                inputLayer.propagate(new double[][]{curDatum.getRaw()});
                hiddenLayerA.propagate();
                hiddenLayerB.propagate();
                hiddenLayerC.propagate();
                outputLayer.propagate();
                
                double curErr = 0.0;
                double target[] = curDatum.getLabel();
                double prediction[][] = outputLayer.getActivationValues();
                for (int k = 0; k < curDatum.getLabel().length; k++) {
                
                    curErr += Math.abs(prediction[k][0] - target[k]) / curDatum.getLabel().length;
                }
                avgErr += curErr / testingSet.size();
                
                outputLayer.clearNodeSums();
                hiddenLayerC.clearNodeSums();
                hiddenLayerB.clearNodeSums();
                hiddenLayerA.clearNodeSums();
                inputLayer.clearNodeSums();
            }
            
            System.out.printf("epoch %d | average error - %f\n", i, avgErr);
            
            trainingSet.resetBuffer();
            testingSet.resetBuffer();
        }
        /**/
        
        /*
        createDataSet("te");
        createDataSet("tr");
        /**/
    }
    
    public static int[] sequence(int exclusiveBound) {
    
        int returnArray[] = new int[exclusiveBound];
        
        for (int i = 0; i < exclusiveBound; i++) {
        
            returnArray[i] = i;
        }
        
        return returnArray;
    }
    
    public static void createDataSet(String fileName) throws IOException {
    
        final int STRING_SIZE = 16;
        
        DataSet tempSet = new DataSet(0);
        
        byte rawData[] = Files.readAllBytes(Paths.get(fileName + ".txt"));
        
        for (int i = 0; i < rawData.length - STRING_SIZE; i++) {
        
            double curRaw[] = new double[STRING_SIZE];
            
            for (int j = 0; j < STRING_SIZE; j++) {
            
                curRaw[j] = rawData[i + j];
            }
            
            double formattedRaw[] = charsToBits(curRaw);
            
            double curLabel[] = new double[1];
            
            curLabel[0] = rawData[i + STRING_SIZE];
            
            double formattedLabel[] = charsToBits(curLabel);
            
            tempSet.addDatum(new LabeledDatum(formattedRaw, formattedLabel));
        }
        
        tempSet.saveToFile(fileName + ".dat");
    }
    
    public static ArrayList<Double> encrypt(ArrayList<Double> message, int key[]) {
    
        ArrayList<Double> cipher = new ArrayList<>();
        
        for (int i = 0; i < message.size(); i++) {
        
            cipher.add(i, (double)key[message.get(i).intValue()]);
        }
        
        return cipher;
    }
    
    public static int[] generateKey(Random PRNG) {
    
        int[] returnKey = sequence(26);
        
        for (int i = 25; i != 0; i--) {
        
            int swapIndex = PRNG.nextInt(i + 1);
            
            int temp = returnKey[i];
            returnKey[i] = returnKey[swapIndex];
            returnKey[swapIndex] = temp;
        }
        
        return returnKey;
    }
    
    public static double[] charsToBits(double data[]) {
    
        final int NUM_BITS = 7;
        
        double returnData[] = new double[data.length * NUM_BITS];
        
        for (int i = 0; i < data.length; i++) {
        
            int tempBits = (int)data[i];
            
            for (int j = 0; j < NUM_BITS; j++) {
            
                if ((tempBits & 0b00000001) == 0b1) {
                
                    returnData[i * NUM_BITS + j] = 1.0;
                }
                else {
                
                    returnData[i * NUM_BITS + j] = 0.0;
                }
                
                tempBits >>= 1;
            }
        }
        
        return returnData;
    }
}
