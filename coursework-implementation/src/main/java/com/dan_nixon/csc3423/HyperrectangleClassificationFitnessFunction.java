package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class HyperrectangleClassificationFitnessFunction extends FitnessFunction
{
  public HyperrectangleClassificationFitnessFunction(InstanceSet trainingSet)
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
