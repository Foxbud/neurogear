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
        double learningRate = 0.1;
        double regParameter = 0.001;
        Regularization regFunction = new L2Regularization();
        Cost costFunction = new CrossEntropyCost();
        int batchSize = 128;
        int numEpochs = 1024;
        Scale rawScale = new StandardScale();
        Scale labelScale = new NormalScale();
        
        DataSet trainingSet = new DataSet(seed);
        trainingSet.loadFromFile("formatted_data.txt");
        trainingSet.resetBuffer();
        rawScale.computeScalingFactors(trainingSet.presentRaw());
        labelScale.computeScalingFactors(trainingSet.presentLabel());
        
        Layer inputLayer = new Layer(8, 1, new IdentityActivation());
        Layer hiddenLayerA = new Layer(8, 7, new LeakyReLUActivation(), sequence(2 * 1), 1 * 1, seed);
        Layer hiddenLayerB = new Layer(16, 6, new LeakyReLUActivation(), sequence(2 * 8), 1 * 8, seed + 1);
        Layer hiddenLayerC = new Layer(32, 3, new LeakyReLUActivation(), sequence(4 * 16), 1 * 16, seed + 2);
        Layer outputLayer = new Layer(1, 1, new LogisticActivation(), sequence(32 * 3), 1, seed + 3);

        hiddenLayerA.connect(inputLayer);
        hiddenLayerB.connect(hiddenLayerA);
        hiddenLayerC.connect(hiddenLayerB);
        outputLayer.connect(hiddenLayerC);
        
        for (int i = 0; i < numEpochs; i++) {
        
            while (trainingSet.hasNextBuffer(batchSize)) {
            
                for (int j = 0; j < batchSize; j++) {
                
                    LabeledDatum curDatum = (LabeledDatum)trainingSet.getNextBuffer();
                
                    inputLayer.propagate(rawScale.scaleDown(curDatum.getRaw()));
                    hiddenLayerA.propagate();
                    hiddenLayerB.propagate();
                    hiddenLayerC.propagate();
                    outputLayer.propagate();

                    System.out.printf("%f\n", Math.abs(curDatum.getLabel()[0] - labelScale.scaleUp(outputLayer.getActivationValues())[0]));
                    
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
                
                outputLayer.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayerC.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayerB.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayerA.correctKernels(learningRate, regFunction, regParameter);
            }
            
            trainingSet.resetBuffer();
        }
        /**/
        
        /*
        int seed = 543;
        
        Random PRNG = new Random(seed);
        DataSet set = new DataSet(seed);
        
        byte data[] = Files.readAllBytes(Paths.get("raw_data.txt"));
        
        for (int i = 0; i < 2048; i++) {
        
            double raw[] = new double[8];
            double label[] = new double[1];
            
            for (int j = 0; j < 8; j++) {
            
                raw[j] = data[i * 8 + j];
            }
            label[0] = 1.0;
            set.addDatum(new LabeledDatum(raw, label));
            
            for (int j = 0; j < 8; j++) {
            
                raw[j] = (double)PRNG.nextInt(128);
            }
            label[0] = 0.0;
            set.addDatum(new LabeledDatum(raw, label));
        }
        
        set.saveToFile("formatted_data.txt");
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
