package com.fairburn.neurogear;

import com.fairburn.neurogear.base.activation.*;
import com.fairburn.neurogear.base.cost.*;
import com.fairburn.neurogear.base.regularization.*;
import com.fairburn.neurogear.data.dataset.DataSet;
import com.fairburn.neurogear.data.datum.Datum;
import com.fairburn.neurogear.data.scale.*;
import com.fairburn.neurogear.utilities.layer.Layer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

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

    // MEMBER VARIABLES.
    
    private static final int FIELD_SIZE = 11;
    private static final int RESPONSE_SIZE = 100;
    
    // MEMBER METHODS.
    
    /**
     * Debugging method.
     * @param args command line parameters
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws java.io.IOException {
        
        /**/
        int seed = 7749321;
        double learningRate = 0.1;
        double regParameter = 0.0001;
        Cost costFunction = new CrossEntropyCost();
        int batchSize = 32;
        int numEpochs = 16;
        
        DataSet trainingSet = new DataSet(seed);
        populateDataSet(trainingSet, "training/progTraining.txt");
        trainingSet.resetBuffer();
        DataSet validationSet = new DataSet(seed + 1);
        populateDataSet(validationSet, "training/progValidation.txt");
        validationSet.resetBuffer();
        
        Scale rawScale = new NullScale();
        rawScale.computeScalingFactors(trainingSet.presentRaw());
        Scale labelScale = new NullScale();
        labelScale.computeScalingFactors(trainingSet.presentLabel());
        
        Layer inputLayer = new Layer(7, 11, new IdentityActivation());
        Layer hiddenLayerA = new Layer(64, 9, new ReLUActivation(), new NullRegularization(), sequence(3), 7, 1, seed + 2);
        Layer hiddenLayerB = new Layer(128, 4, new SoftsignActivation(), new NullRegularization(), sequence(3), 64, 2, seed + 3);
        Layer outputLayer = new Layer(7, 1, new LogisticActivation(), new L2Regularization(), sequence(4), 128, 1, seed + 4);

        hiddenLayerA.connect(inputLayer);
        hiddenLayerB.connect(hiddenLayerA);
        outputLayer.connect(hiddenLayerB);
        
        for (int i = 0; i < numEpochs; i++) {
        
            while (trainingSet.hasNextBuffer(batchSize)) {
            
                for (int j = 0; j < batchSize; j++) {
                
                    Datum curDatum = trainingSet.getNextBuffer();
                
                    inputLayer.propagate(rawScale.scaleDown(curDatum.getRaw()));
                    hiddenLayerA.propagate();
                    hiddenLayerB.propagate();
                    outputLayer.propagate();
                    
                    outputLayer.backpropagate(labelScale.scaleDown(curDatum.getLabel()), costFunction);
                    hiddenLayerB.backpropagate();
                    hiddenLayerA.backpropagate();
                    
                    outputLayer.clearNodeSums();
                    hiddenLayerB.clearNodeSums();
                    hiddenLayerA.clearNodeSums();
                    inputLayer.clearNodeSums();
                }
                
                outputLayer.correctKernels(learningRate, regParameter);
                hiddenLayerB.correctKernels(learningRate, regParameter);
                hiddenLayerA.correctKernels(learningRate, regParameter);
            }
            
            double avgErr = 0.0;
            
            for (int j = 0; j < validationSet.size(); j++) {
            
                Datum curDatum = validationSet.getNextBuffer();
                
                inputLayer.propagate(rawScale.scaleDown(curDatum.getRaw()));
                hiddenLayerA.propagate();
                hiddenLayerB.propagate();
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
                hiddenLayerB.clearNodeSums();
                hiddenLayerA.clearNodeSums();
                inputLayer.clearNodeSums();
            }
            
            System.out.printf("epoch %d | average error - %f\n", i, avgErr);
            
            trainingSet.resetBuffer();
            validationSet.resetBuffer();
        }
        
        Scanner inputScanner = new Scanner(System.in);
        while (true) {
        
            System.out.printf("Enter a seed string: ");
            String seedString = inputScanner.nextLine();
            
            byte[] workingArray = new byte[FIELD_SIZE];
            for (int i = seedString.length() - FIELD_SIZE; i < seedString.length(); i++) {
            
                workingArray[i - seedString.length() + FIELD_SIZE] = (byte)seedString.charAt(i);
            }
            
            System.out.printf("NETWORK RESPONSE: ");
            
            for (int i = 0; i < RESPONSE_SIZE; i++) {
            
                inputLayer.propagate(rawScale.scaleDown(formatCharArray(workingArray, 0, FIELD_SIZE)));
                hiddenLayerA.propagate();
                hiddenLayerB.propagate();
                outputLayer.propagate();
                
                for (int j = 0; j < FIELD_SIZE - 1; j++) {
                
                    workingArray[j] = workingArray[j + 1];
                }
                workingArray[FIELD_SIZE - 1] = getCharFromArray(labelScale.scaleUp(outputLayer.getActivationValues()), 0);
                
                System.out.printf("%c", (char)workingArray[FIELD_SIZE - 1]);
                
                outputLayer.clearNodeSums();
                hiddenLayerB.clearNodeSums();
                hiddenLayerA.clearNodeSums();
                inputLayer.clearNodeSums();
            }
            
            
            for (int i = 0; i < workingArray.length; i++) {
            
                System.out.printf("%c", (char)workingArray[i]);
            }
            System.out.printf("\n");
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
        
            if (workingArray[i][index] >= 0.5) {
            
                returnByte |= 0b00000001 << i;
            }
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
        
        for (int i = 0; i < charData.length - FIELD_SIZE; i++) {
        
            workingDataSet.addDatum(new Datum(formatCharArray(charData, i, i + FIELD_SIZE), formatCharArray(charData, i + FIELD_SIZE, i + FIELD_SIZE + 1)));
        }
    }
}