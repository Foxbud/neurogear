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
        
        
        Layer scrapLayer = new Layer(1, 1, 1, 1, new IdentityActivation(), seed);
        Layer inputLayer = new Layer(2, 1, 1, 1, new IdentityActivation(), seed);
        Layer hiddenLayer = new Layer(3, 1, 2, 1, new ReLUActivation(), seed);
        Layer outputLayer = new Layer(1, 1, 2, 1, new IdentityActivation(), seed);

        inputLayer.connect(scrapLayer);
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
                    inputLayer.backpropagate();
                }
                
                outputLayer.correctKernels(learningRate, regFunction, regParameter);
                hiddenLayer.correctKernels(learningRate, regFunction, regParameter);
                inputLayer.correctKernels(learningRate, regFunction, regParameter);
            }
            
            trainingSet.resetBuffer();
        }
        
        
        /*
        Node iNodeA = new Node();
        Node iNodeB = new Node();
        Node oNode = new Node();
        
        NodeConnection conA = new NodeConnection(0.55);
        NodeConnection conB = new NodeConnection(0.-37);
        BiasConnection conC = new BiasConnection(0.0);
        
        Activation actFunc = new IdentityActivation();
        
        conA.setInput(iNodeA);
        conA.setOutput(oNode);
        
        conB.setInput(iNodeB);
        conB.setOutput(oNode);
        
        conC.setOutput(oNode);
        
        for (int i = 0; i < numEpochs; i++) {
        
            while (trainingSet.hasNextBuffer(batchSize)) {
            
                for (int j = 0; j < batchSize; j++) {
                
                    LabeledDatum curDatum = (LabeledDatum)trainingSet.getNextBuffer();
                
                    iNodeA.addToActivationSum(rawScale.scaleDown(curDatum.getRaw())[0]);
                    iNodeB.addToActivationSum(rawScale.scaleDown(curDatum.getRaw())[1]);
                    
                    iNodeA.triggerActivation(actFunc);
                    iNodeB.triggerActivation(actFunc);
                    
                    conA.propagate();
                    conB.propagate();
                    conC.propagate();
                    
                    oNode.triggerActivation(actFunc);

                    System.out.printf("%f\n", Math.abs(labelScale.scaleDown(curDatum.getLabel())[0] - oNode.getActivationValue()));
                    
                    oNode.setInitialDelta(costFunction, labelScale.scaleDown(curDatum.getLabel())[0]);
                    
                    oNode.triggerDelta(actFunc);
                    
                    conA.backpropagate();
                    conB.backpropagate();
                    conC.backpropagate();
                    
                    iNodeA.triggerDelta(actFunc);
                    iNodeB.triggerDelta(actFunc);
                }
                
                conA.correct(learningRate, regFunction, regParameter);
                conB.correct(learningRate, regFunction, regParameter);
                conC.correct(learningRate, regFunction, regParameter);
            }
            
            trainingSet.resetBuffer();
        }
        */
        
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
