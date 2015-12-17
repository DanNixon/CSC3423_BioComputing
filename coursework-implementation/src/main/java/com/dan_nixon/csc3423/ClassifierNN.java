package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Attributes;
import com.dan_nixon.csc3423.framework.Classifier;
import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import com.dan_nixon.csc3423.vis.NeuralNetworkFrame;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 * Classifier using a multi-layer neural network to solve an n-dimensional
 * problem.
 */
public class ClassifierNN extends Classifier
{
  /**
   * Converts an Instance in the coursework framework to a DataSetRow for
   * Neuroph.
   * @param inst Instance to convert
   * @return DataSetRow
   */
  public static DataSetRow instanceToRow(Instance inst)
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
      nnTrainingSet.addRow(instanceToRow(i));

    m_mlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH,
                                              dimensions, 10, 10, 1);

    NeuralNetworkFrame vis = null;
    if (visualise)
      vis = new NeuralNetworkFrame(m_mlPerceptron);

    m_mlPerceptron.getLearningRule().addListener(new NNLearnListener(vis));

    m_mlPerceptron.getLearningRule().setLearningRate(0.01);
    m_mlPerceptron.getLearningRule().setMaxIterations(10000);

    m_mlPerceptron.learn(nnTrainingSet);

    BackPropagation bp = (BackPropagation) m_mlPerceptron.getLearningRule();
    int iters = bp.getCurrentIteration();
    System.out.println("NN learning complete after " + iters + " iterations");
  }

  @Override
  public int classifyInstance(Instance i)
  {
    m_mlPerceptron.setInput(instanceToRow(i).getInput());
    m_mlPerceptron.calculate();
    int classification = (int) (m_mlPerceptron.getOutput()[0] + 0.5);
    return classification;
  }

  @Override
  public void printClassifier()
  {
    System.out.println(this);
  }

  /**
   * Outputs network topology as string.
   * Note that only the topology is output, also outputting the weights would
   * result in a huge string.
   * @return String representation of topology
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    sb.append("NeuralNetwork[");
    for (int i = 0; i < m_mlPerceptron.getLayersCount(); i++)
    {
      if (i > 0)
        sb.append(",");

      Layer l = m_mlPerceptron.getLayerAt(i);
      sb.append(l.getNeuronsCount());

      int numBias = 0;
      for (Neuron n : l.getNeurons())
      {
        if (n instanceof BiasNeuron)
          numBias++;
      }
      sb.append("(");
      sb.append(numBias);
      sb.append(")");
    }
    sb.append("]");

    return sb.toString();
  }

  private final MultiLayerPerceptron m_mlPerceptron;
}
