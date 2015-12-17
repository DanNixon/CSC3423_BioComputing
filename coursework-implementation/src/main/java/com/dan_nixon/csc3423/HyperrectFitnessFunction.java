package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 * Fitness function for use with JGAP genetic algorithm.
 *
 * Defines fitness as number of correctly classified instances over total number
 * of classified instances (unclassified instances are not counted).
 */
public class HyperrectFitnessFunction extends FitnessFunction
{
  /**
   * Create new fitness function with given training set for evaluation.
   *
   * @param trainingSet Evaluation training set
   */
  public HyperrectFitnessFunction(InstanceSet trainingSet)
  {
    m_trainingSet = trainingSet;
  }

  @Override
  protected double evaluate(IChromosome c)
  {
    ClassifierHyperrectangle candidate = new ClassifierHyperrectangle(c);

    int correct = 0;
    int total = 0;

    for (Instance i : m_trainingSet.getInstances())
    {
      int classValue = candidate.classifyInstance(i);
      if (classValue == -1)
        continue;

      total++;
      if (classValue == i.getClassValue())
        correct++;
    }

    return (double) correct / (double) total;
  }

  private final InstanceSet m_trainingSet;
}
