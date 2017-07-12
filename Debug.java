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
        
        /**/
        int seed = 24917;
        double learningRate = 0.1;
        double regParameter = 0.00001;
        Cost costFunction = new CrossEntropyCost();
        int batchSize = 32;
        int numEpochs = 128;
        
        DataSet trainingSet = new DataSet(seed);
        populateDataSet(trainingSet, "trainingText.txt");
        trainingSet.resetBuffer();
        DataSet validationSet = new DataSet(seed + 1);
        populateDataSet(validationSet, "validationText.txt");
        validationSet.resetBuffer();
        
        Scale rawScale = new NormalScale();
        rawScale.computeScalingFactors(trainingSet.presentRaw());
        Scale labelScale = new NormalScale();
        labelScale.computeScalingFactors(trainingSet.presentLabel());
        
        Layer inputLayer = new Layer(7, 15, new IdentityActivation());
        Layer hiddenLayerA = new Layer(16, 7, new LeakyReLUActivation(), new NullRegularization(), sequence(3), 7, 2, seed + 2);
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
                
                    Datum curDatum = trainingSet.getNextBuffer();
                
                    inputLayer.propagate(rawScale.scaleDown(curDatum.getRaw()));
                    hiddenLayerA.propagate();
                    hiddenLayerB.propagate();
                    hiddenLayerC.propagate();
                    outputLayer.propagate();
                    
                    outputLayer.backpropagate(labelScale.scaleDown(curDatum.getLabel()), costFunction);
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
            
            for (int j = 0; j < validationSet.size(); j++) {
            
                Datum curDatum = validationSet.getNextBuffer();
                
                inputLayer.propagate(rawScale.scaleDown(curDatum.getRaw()));
                hiddenLayerA.propagate();
                hiddenLayerB.propagate();
                hiddenLayerC.propagate();
                outputLayer.propagate();
                
                double curErr = 0.0;
                double target[][] = curDatum.getLabel();
                double prediction[][] = labelScale.scaleUp(outputLayer.getActivationValues());
                for (int k = 0; k < target.length; k++) {
                
                    for (int l = 0; l < target[k].length; l++) {
                    
                        curErr += Math.abs(prediction[k][l] - target[k][l]) / target.length;
                    }
                }
                avgErr += curErr / validationSet.size();
                
                outputLayer.clearNodeSums();
                hiddenLayerC.clearNodeSums();
                hiddenLayerB.clearNodeSums();
                hiddenLayerA.clearNodeSums();
                inputLayer.clearNodeSums();
            }
            
            System.out.printf("epoch %d | average error - %f\n", i, avgErr);
            
            trainingSet.resetBuffer();
            validationSet.resetBuffer();
        }
        /**/
    }
    
    public static int[] sequence(int exclusiveBound) {
    
        int returnArray[] = new int[exclusiveBound];
        
        for (int i = 0; i < exclusiveBound; i++) {
        
            returnArray[i] = i;
        }
        
        return returnArray;
    }
    
    public static void addCharToArray(byte charToAdd, double[][] workingArray, int index) {
    
        for (int i = 0; i < 7; i++) {
        
            workingArray[i][index] = (double)((charToAdd >>> i) & 0b00000001);
        }
    }
    
    public static byte getCharFromArray(double[][] workingArray, int index) {
    
        byte returnByte = 0x00;
        
        for (int i = 0; i < 7; i++) {
        
            returnByte |= ((byte)workingArray[i][index]) << i;
        }
        
        return returnByte;
    }
    
    public static double[][] formatCharArray(byte[] workingArray, int startIndex, int endIndex) {
    
        double[][] returnArray = new double[7][endIndex - startIndex];
        
        for (int i = startIndex; i < endIndex; i++) {
        
            addCharToArray(workingArray[i], returnArray, i - startIndex);
        }
        
        return returnArray;
    }
    
    public static byte[] recoverCharArray(double[][] workingArray) {
    
        byte[] returnArray = new byte[workingArray[0].length];
        
        for (int i = 0; i < returnArray.length; i++) {
        
            returnArray[i] = getCharFromArray(workingArray, i);
        }
        
        return returnArray;
    }
    
    public static void populateDataSet(DataSet workingDataSet, String workingPath) throws IOException {
    
        byte[] charData = Files.readAllBytes(Paths.get(workingPath));
        
        int fieldSize = 15;
        
        for (int i = 0; i < charData.length - fieldSize; i++) {
        
            workingDataSet.addDatum(new Datum(formatCharArray(charData, i, i + fieldSize), formatCharArray(charData, i + fieldSize, i + fieldSize + 1)));
        }
    }
}