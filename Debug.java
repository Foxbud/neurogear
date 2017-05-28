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
        int seed = 34988;
        double learningRate = 0.5;
        double regParameter = 0.00001;
        Regularization regFunction = new L2Regularization();
        Cost costFunction = new CrossEntropyCost();
        int batchSize = 32;
        int numEpochs = 1024;
        Scale rawScale = new NormalScale();
        //Scale labelScale = new NormalScale();
        
        DataSet trainingSet = new DataSet(seed);
        trainingSet.loadFromFile("training_data.txt");
        trainingSet.resetBuffer();
        DataSet testingSet = new DataSet(seed + 1);
        testingSet.loadFromFile("testing_data.txt");
        testingSet.resetBuffer();
        
        rawScale.computeScalingFactors(trainingSet.presentRaw());
        //labelScale.computeScalingFactors(trainingSet.presentLabel());
        
        Layer inputLayer = new Layer(1, 160, new IdentityActivation());
        Layer hiddenLayerA = new Layer(8, 31, new LeakyReLUActivation(), sequence(10), 1, 5, seed + 2);
        Layer hiddenLayerB = new Layer(16, 10, new LeakyReLUActivation(), sequence(4), 8, 3, seed + 3);
        Layer hiddenLayerC = new Layer(32, 3, new LeakyReLUActivation(), sequence(4), 16, 3, seed + 4);
        Layer outputLayer = new Layer(1, 1, new LogisticActivation(), sequence(3), 32, 1, seed + 5);

        hiddenLayerA.connect(inputLayer);
        hiddenLayerB.connect(hiddenLayerA);
        hiddenLayerC.connect(hiddenLayerB);
        outputLayer.connect(hiddenLayerC);
        
        for (int i = 0; i < numEpochs; i++) {
        
            while (trainingSet.hasNextBuffer(batchSize)) {
            
                for (int j = 0; j < batchSize; j++) {
                
                    LabeledDatum curDatum = (LabeledDatum)trainingSet.getNextBuffer();
                
                    inputLayer.propagate(new double[][]{rawScale.scaleDown(curDatum.getRaw())});
                    hiddenLayerA.propagate();
                    hiddenLayerB.propagate();
                    hiddenLayerC.propagate();
                    outputLayer.propagate();
                    
                    outputLayer.backpropagate(new double[][]{curDatum.getLabel()}, costFunction);
                    hiddenLayerC.backpropagate();
                    hiddenLayerB.backpropagate();
                    hiddenLayerA.backpropagate();
                    
                    outputLayer.clearNodeSums();
                    hiddenLayerC.clearNodeSums();
                    hiddenLayerB.clearNodeSums();
                    hiddenLayerA.clearNodeSums();
                    inputLayer.clearNodeSums();
                }
                
                outputLayer.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayerC.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayerB.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayerA.correctKernels(learningRate, regFunction, regParameter);
            }
            
            double avgErr = 0.0;
            
            for (int j = 0; j < testingSet.size(); j++) {
            
                LabeledDatum curDatum = (LabeledDatum)testingSet.getNextBuffer();
                
                inputLayer.propagate(new double[][]{rawScale.scaleDown(curDatum.getRaw())});
                hiddenLayerA.propagate();
                hiddenLayerB.propagate();
                hiddenLayerC.propagate();
                outputLayer.propagate();
                
                double curErr = Math.abs(outputLayer.getActivationValues()[0][0] - curDatum.getLabel()[0]);
                avgErr += curErr / testingSet.size();
                
                outputLayer.clearNodeSums();
                hiddenLayerC.clearNodeSums();
                hiddenLayerB.clearNodeSums();
                hiddenLayerA.clearNodeSums();
                inputLayer.clearNodeSums();
            }
            
            System.out.printf("Epoch #%d | avg error - %f\n", i, avgErr);
            
            trainingSet.resetBuffer();
            testingSet.resetBuffer();
        }
        /**/
        
        /*
        int seed = 543;
        
        Random PRNG = new Random(seed);
        DataSet set = new DataSet(seed);
        
        ArrayList<ArrayList<Double>> data = fileToFormattedASCII("testing_text.txt");
        //ArrayList<ArrayList<Double>> data = fileToFormattedASCII("training_text.txt");
        
        ArrayList<Double> temp;
        double raw[] = new double[data.get(0).size()];
        
        for (int i = 0; i < data.size(); i++) {
        
            for (int j = 0; j < raw.length; j++) {
            
                raw[j] = data.get(i).get(j);
            }
            set.addDatum(new LabeledDatum(charsToBits(raw), new double[]{0.9}));
            
            temp = encrypt(data.get(i), generateKey(PRNG));
            for (int j = 0; j < raw.length; j++) {
            
                raw[j] = temp.get(j);
            }
            set.addDatum(new LabeledDatum(charsToBits(raw), new double[]{0.1}));
        }
        
        set.saveToFile("testing_data.txt");
        //set.saveToFile("training_data.txt");
        /**/
    }
    
    public static int[] sequence(int exclusiveBound) {
    
        int returnArray[] = new int[exclusiveBound];
        
        for (int i = 0; i < exclusiveBound; i++) {
        
            returnArray[i] = i;
        }
        
        return returnArray;
    }
    
    public static ArrayList<ArrayList<Double>> fileToFormattedASCII(String fileName) throws IOException {
    
        byte rawData[] = Files.readAllBytes(Paths.get(fileName));
        
        ArrayList<ArrayList<Double>> returnData = new ArrayList<>();
        
        ArrayList<Double> temp = new ArrayList<>();
        
        for (int i = 0; i < rawData.length; i++) {
        
            if (rawData[i] >= 97 && rawData[i] <= 122) {
            
                temp.add((double)(rawData[i] - 97));
            }
            else if (rawData[i] >= 65 && rawData[i] <= 90) {
            
                temp.add((double)(rawData[i] - 65));
            }
            
            if (temp.size() == 32) {
            
                returnData.add(temp);
                
                temp = new ArrayList<>();
            }
            
            if (returnData.size() == -1) {
            
                break;
            }
        }
        
        return returnData;
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
    
        double returnData[] = new double[data.length * 5];
        
        for (int i = 0; i < data.length; i++) {
        
            int tempBits = (int)data[i];
            
            for (int j = 0; j < 5; j++) {
            
                if ((tempBits & 0b00000001) == 0b1) {
                
                    returnData[i * 5 + j] = 1.0;
                }
                else {
                
                    returnData[i * 5 + j] = 0.0;
                }
                
                tempBits >>= 1;
            }
        }
        
        return returnData;
    }
}
