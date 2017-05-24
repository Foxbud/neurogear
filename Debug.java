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
        
        
        int seed = 15;
        double learningRate = 0.1;
        double regParameter = 0.00001;
        Regularization regFunction = new L2Regularization();
        Cost costFunction = new QuadraticCost();
        int batchSize = 8;
        int numEpochs = 100;
        Scale rawScale = new StandardScale();
        Scale labelScale = new StandardScale();
        
        DataSet trainingSet = new DataSet(seed);
        trainingSet.loadFromFile("TestDataSet.txt");
        trainingSet.resetBuffer();
        rawScale.computeScalingFactors(trainingSet.presentRaw());
        labelScale.computeScalingFactors(trainingSet.presentLabel());
        
        Layer inputLayer = new Layer(2, 1, new IdentityActivation());
        Layer hiddenLayer = new Layer(4, 1, new ReLUActivation(), new int[]{0, 1}, 1, seed);
        Layer outputLayer = new Layer(1, 1, new IdentityActivation(), new int[]{0, 1, 2, 3}, 1, seed);

        hiddenLayer.connect(inputLayer);
        outputLayer.connect(hiddenLayer);
        
        for (int i = 0; i < numEpochs; i++) {
        
            while (trainingSet.hasNextBuffer(batchSize)) {
            
                for (int j = 0; j < batchSize; j++) {
                
                    LabeledDatum curDatum = (LabeledDatum)trainingSet.getNextBuffer();
                
                    inputLayer.propagate(rawScale.scaleDown(curDatum.getRaw()));
                    hiddenLayer.propagate();
                    outputLayer.propagate();

                    double[] results = labelScale.scaleUp(outputLayer.getActivationValues());
                    System.out.printf("%f\n", Math.abs(curDatum.getLabel()[0] - results[0]));
                    
                    outputLayer.backpropagate(labelScale.scaleDown(curDatum.getLabel()), costFunction);
                    hiddenLayer.backpropagate();
                    
                    outputLayer.clearNodeSums();
                    hiddenLayer.clearNodeSums();
                    inputLayer.clearNodeSums();
                }
                
                outputLayer.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayer.correctKernels(learningRate, regFunction, regParameter);
            }
            
            trainingSet.resetBuffer();
        }
        
        
        /*
        Random PRNG = new Random(1234);
        
        DataSet trainingSet = new DataSet(1234);
        
        for (int i = 0; i < 1024; i++) {
        
            double raw[] = {(PRNG.nextDouble() - 0.5) * 1000, (PRNG.nextDouble() - 0.5) * 1000};
            double label[] = {raw[0] + raw[1]};
            
            trainingSet.addDatum(new LabeledDatum(raw, label));
        }
        
        trainingSet.saveToFile("TestDataSet.txt");
        */
    }
}
