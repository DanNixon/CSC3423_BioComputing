package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Attributes;
import com.dan_nixon.csc3423.framework.Classifier;
import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import com.dan_nixon.csc3423.vis.NeuralNetworkFrame;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 * Classifier using a multi-layer neural network to solve an n-dimensional
 * problem.
 */
class ClassifierNN extends Classifier
{
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
   * @param visualise If visualisation should be enabled
   */
  public ClassifierNN(InstanceSet trainingSet, boolean visualise)
  {
    int dimensions = Attributes.getNumAttributes();

    DataSet nnTrainingSet = new DataSet(dimensions, 1);
    for (Instance i : trainingSet.getInstances())
      nnTrainingSet.addRow(InstanceToRow(i));

    m_mlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH,
                                              dimensions, 20, 20, 1);

    NeuralNetworkFrame vis = null;
    if (visualise)
      vis = new NeuralNetworkFrame(m_mlPerceptron);

    m_mlPerceptron.getLearningRule().addListener(new NNLearnListener(vis));

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
