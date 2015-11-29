package com.dan_nixon.csc3423;

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
    // TODO
    return 0.0;
  }
  
  private final InstanceSet m_trainingSet;
}
