package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Attributes;
import com.dan_nixon.csc3423.framework.Classifier;
import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 * Classifier using a multi-layer neural network to solve an n-dimensional
 * problem.
 */
class ClassifierNN extends Classifier
{
  /**
   * Listener to provide feedback during the learning process.
   */
  static class LearningListener implements LearningEventListener
  {
    public void handleLearningEvent(LearningEvent event)
    {
      BackPropagation bp = (BackPropagation) event.getSource();
      StringBuilder sb = new StringBuilder();
      sb.append("Iteration: ");
      sb.append(bp.getCurrentIteration());
      sb.append(", error: ");
      sb.append(bp.getTotalNetworkError());
      System.out.println(sb);
    }
  }
  
  /**
   * Converts an Instance in the coursework framework to a DataSetRow for
   * Neuroph.
   * @param inst Instance to convert
   * @return DataSetRow
   */
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
  
  /**
   * Create a new neural network classifier and train the network with a
   * training data set.
   * @param trainingSet Training data set
   */
  public ClassifierNN(InstanceSet trainingSet)
  {
    int dimensions = Attributes.getNumAttributes();
    
    DataSet nnTrainingSet = new DataSet(dimensions, 1);
    for (Instance i : trainingSet.getInstances())
      nnTrainingSet.addRow(InstanceToRow(i));
    
    m_mlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH,
                                              dimensions, 20, 20, 1);
    
    m_mlPerceptron.getLearningRule().addListener(new LearningListener());
    
    m_mlPerceptron.getLearningRule().setLearningRate(0.01);
    m_mlPerceptron.getLearningRule().setMaxIterations(10000);
    
    m_mlPerceptron.learn(nnTrainingSet);
  }
  
  @Override
  public int classifyInstance(Instance i)
  {
    m_mlPerceptron.setInput(InstanceToRow(i).getInput());
    m_mlPerceptron.calculate();
    int classification = (int) (m_mlPerceptron.getOutput()[0] + 0.5);
    return classification;
  }

  @Override
  public void printClassifier()
  {
    // TODO
    System.out.println("TODO");
  }
  
  private final MultiLayerPerceptron m_mlPerceptron;
}