package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Attributes;
import com.dan_nixon.csc3423.framework.Classifier;
import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.SigmoidDeltaRule;
import org.neuroph.util.TransferFunctionType;

class ClassifierNN extends Classifier
{
  public static DataSetRow InstanceToRow(Instance inst)
  {
    int dimensions = Attributes.getNumAttributes();

    // Create list of inputs
    double[] inputs = new double[dimensions];
    for (int i = 0; i < dimensions; i++)
      inputs[i] = inst.getRealAttribute(i);

    // Create row
    DataSetRow row = new DataSetRow(inputs,
                                    new double[]{inst.getClassValue()});

    return row;
  }
  
  public ClassifierNN(InstanceSet trainingSet)
  {
    int dimensions = Attributes.getNumAttributes();
    
    DataSet nnTrainingSet = new DataSet(dimensions, 1);
    for (Instance i : trainingSet.getInstances())
      nnTrainingSet.addRow(InstanceToRow(i));
    
    m_mlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,
                                              dimensions, 10, 1);
    
    m_mlPerceptron.learn(nnTrainingSet);
  }
  
  @Override
  public int classifyInstance(Instance i)
  {
    m_mlPerceptron.setInput(InstanceToRow(i).getInput());
    m_mlPerceptron.calculate();
    int classification = (int) m_mlPerceptron.getOutput()[0];
    return classification;
  }

  @Override
  public void printClassifier()
  {
    // TODO
    System.out.println("TODO");
  }
  
  private MultiLayerPerceptron m_mlPerceptron;
}