package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.Attributes;
import com.dan_nixon.csc3423.framework.Classifier;
import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import java.util.Arrays;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

class ClassifierHyperrectangle extends Classifier
{
  public static final int MAX_ITERATIONS = 100;
  public static final double EXIT_THRESHOLD = 0.95;
  
  public ClassifierHyperrectangle(InstanceSet trainingSet) throws InvalidConfigurationException
  { 
    // Configuration
    Configuration conf = new DefaultConfiguration();
    conf.setPopulationSize(100);
    
    // Setup fitness function
    FitnessFunction fit = new HyperrectangleClassificationFitnessFunction(trainingSet);
    conf.setFitnessFunction(fit);
   
    // Setup chromosome
    int dimensions = Attributes.getNumAttributes();
    Gene[] sampleGenes = new Gene[dimensions * 2];
    for (int i = 0; i < dimensions * 2; i+=2)
    {
      // Position
      sampleGenes[i] = new DoubleGene(conf);
      // Size
      sampleGenes[i] = new DoubleGene(conf);
    }
    Chromosome sample = new Chromosome(conf, sampleGenes);
    conf.setSampleChromosome(sample);
    
    // Generate initial population
    Genotype population = Genotype.randomInitialGenotype(conf);
    
    for (int i = 0; i < MAX_ITERATIONS; i++)
    {
      population.evolve();
      m_bestSolution = population.getFittestChromosome();
      
      double bestFitness = m_bestSolution.getFitnessValue();
      if (bestFitness > EXIT_THRESHOLD)
        break;
    }
  }

  @Override
  public int classifyInstance(Instance i)
  {
    // TODO
    return -1;
  }

  @Override
  public void printClassifier()
  {
    System.out.println(this);
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    // TODO
    return sb.toString();
  }
  
  private IChromosome m_bestSolution;
}