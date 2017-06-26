package neurogear;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
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
        
        /*
        int seed = 24918;
        double learningRate = 0.5;
        double regParameter = 0.00001;
        Cost costFunction = new CrossEntropyCost();
        int batchSize = 32;
        int numEpochs = 128;
        
        DataSet trainingSet = new DataSet(seed);
        trainingSet.loadFromFile("tr.dat");
        trainingSet.resetBuffer();
        DataSet testingSet = new DataSet(seed + 1);
        testingSet.loadFromFile("te.dat");
        testingSet.resetBuffer();
        
        Layer inputLayer = new Layer(1, 56, new IdentityActivation());
        Layer hiddenLayerA = new Layer(16, 7, new LeakyReLUActivation(), new NullRegularization(), sequence(14), 1, 7, seed + 2);
        Layer hiddenLayerB = new Layer(32, 6, new LeakyReLUActivation(), new NullRegularization(), sequence(2), 16, 1, seed + 3);
        Layer hiddenLayerC = new Layer(64, 5, new LeakyReLUActivation(), new NullRegularization(), sequence(2), 32, 1, seed + 4);
        Layer outputLayer = new Layer(7, 1, new LogisticActivation(), new L2Regularization(), sequence(5), 64, 1, seed + 5);

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
        
        /**/
        int seed = 324987;
        
        DataSet testSet = new DataSet(seed);
        
        Random PRNG = new Random(seed);
        
        double[][] raw = new double[3][16];
        double[][] label = new double[5][3];
        
        for (int i = 0; i < 8; i++) {
        
            for (int j = 0; j < raw.length; j++) {
            
                for (int k = 0; k < raw[j].length; k++) {
                
                    raw[j][k] = PRNG.nextDouble();
                }
            }
            
            for (int j = 0; j < label.length; j++) {
            
                for (int k = 0; k < label[j].length; k++) {
                
                    label[j][k] = PRNG.nextDouble();
                }
            }
            
            testSet.addDatum(new Datum(raw, label));
        }
        
        testSet.saveToFile("testSet.txt");
        /**/
    }
    
    public static int[] sequence(int exclusiveBound) {
    
        int returnArray[] = new int[exclusiveBound];
        
        for (int i = 0; i < exclusiveBound; i++) {
        
            returnArray[i] = i;
        }
        
        return returnArray;
    }
}