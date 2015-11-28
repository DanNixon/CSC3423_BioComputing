package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Attributes;
import com.dan_nixon.csc3423.framework.Classifier;
import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.quick.QuickPropagation;

class ClassifierNN extends Classifier
{
  public ClassifierNN(InstanceSet trainingSet)
  {
    int numInstances = trainingSet.numInstances();
    int dimensions = Attributes.getNumAttributes();

    // Generate training data set
    double[][] vars = new double[numInstances][dimensions];
    double[][] ideals = new double[numInstances][1];

    for (int i = 0; i < numInstances; i++) {
      Instance inst = trainingSet.getInstance(i);

      for (int j = 0; j < dimensions; j++) {
        vars[i][j] = inst.getRealAttribute(j);
      }

      ideals[i][0] = inst.getClassValue();
    }

    MLDataSet nnTrainingSet = new BasicMLDataSet(vars, ideals);

    // Configure network
    m_network = new BasicNetwork();
    m_network.addLayer(new BasicLayer(null, true, dimensions));
    m_network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 35));
    m_network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
    m_network.getStructure().finalizeStructure();
    m_network.reset();

    // Train network
    final QuickPropagation train = new QuickPropagation(m_network, nnTrainingSet);
    do {
      train.iteration(500);
      System.out.println(train.getError());
    } while (train.getError() > 0.001);
    train.finishTraining();
  }

  @Override
  public int classifyInstance(Instance inst)
  {
    int dimensions = Attributes.getNumAttributes();
    
    double[] params = new double[dimensions];
    for (int i = 0; i < dimensions; i++)
      params[i] = inst.getRealAttribute(i);
    
    MLData input = new BasicMLData(params);
    final MLData output = m_network.compute(input);
    
    return (int) output.getData(0);
  }

  @Override
  public void printClassifier()
  {
    System.out.println(m_network);
  }
  
  BasicNetwork m_network;
}
