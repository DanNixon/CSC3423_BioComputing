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
import org.jgap.impl.CompositeGene;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;

class ClassifierHyperrectangle extends Classifier
{
  public static final int MAX_ITERATIONS = 100;
  public static final double EXIT_THRESHOLD = 0.95;
  
  public static Gene[] createSampleGenes(Configuration conf, int dimensions) throws InvalidConfigurationException
  {
    Gene[] sampleGenes = new Gene[dimensions + 1];
    
    // Vertex position genes
    for (int i = 0; i < dimensions; i++)
    {
      CompositeGene g = new CompositeGene(conf);
      g.addGene(new DoubleGene(conf, -6.0, 6.0));
      g.addGene(new DoubleGene(conf, -6.0, 6.0));
      // TODO: add validator
      sampleGenes[i] = g;
    }
    
    // Classification gene
    sampleGenes[sampleGenes.length - 1] = new IntegerGene(conf, 0, 1);
    
    return sampleGenes;
  }
  
  public ClassifierHyperrectangle(InstanceSet trainingSet) throws InvalidConfigurationException
  { 
    int dimensions = Attributes.getNumAttributes();
    
    // Configuration
    DefaultConfiguration.reset();
    Configuration conf = new DefaultConfiguration();
    conf.setPopulationSize(100);
    
    // Setup fitness function
    FitnessFunction fit = new HyperrectangleClassificationFitnessFunction(trainingSet);
    conf.setFitnessFunction(fit);
   
    // Setup chromosome
    Chromosome sample = new Chromosome(conf, createSampleGenes(conf, dimensions));
    conf.setSampleChromosome(sample);
    
    // Generate initial population
    Genotype population = Genotype.randomInitialGenotype(conf);

    // Evolve
    IChromosome bestSolution = null;
    for (int i = 0; i < MAX_ITERATIONS; i++)
    {
      population.evolve();
      bestSolution = population.getFittestChromosome();
      
      double bestFitness = bestSolution.getFitnessValue();
      if (bestFitness > EXIT_THRESHOLD)
        break;
    }
    
    // Record best
    updateFromChromosome(bestSolution);
  }
  
  public ClassifierHyperrectangle(IChromosome c)
  {
    updateFromChromosome(c);
  }

  @Override
  public int classifyInstance(Instance inst)
  {
    int dimensions = Attributes.getNumAttributes();
    for (int i = 0; i < dimensions; i++)
    {
      double instPosI = inst.getRealAttribute(i);
      
      double rectUpperI = m_verticies[i][1];
      double rectLowerI = m_verticies[i][0];
      
      if (instPosI > rectUpperI || instPosI < rectLowerI)
        return -1;
    }
    
    return m_classValue;
  }

  @Override
  public void printClassifier()
  {
    System.out.println(this);
  }
  
  public double[][] getVerticies()
  {
    return m_verticies;
  }
  
  public double[] getVertex(int i)
  {
    return m_verticies[i];
  }
  
  public int getClassValue()
  {
    return m_classValue;
  }
  
  private void updateFromChromosome(IChromosome c)
  {
    Gene[] genes = c.getGenes();
    int dimensions = genes.length - 1;
    
    m_verticies = new double[2][dimensions];
    
    for (int i = 0; i < dimensions; i++)
    {
      CompositeGene comp = (CompositeGene) genes[i];
      m_verticies[i][0] = (Double) comp.getGenes().get(0).getAllele();
      m_verticies[i][1] = (Double) comp.getGenes().get(1).getAllele();
    }
    
    m_classValue = (Integer) genes[dimensions].getAllele();
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Hyperrectangle[class=");
    sb.append(m_classValue);
    sb.append(",verticies=");
    sb.append(Arrays.toString(m_verticies));
    sb.append("]");
    return sb.toString();
  }
  
  private int m_classValue;
  private double[][] m_verticies;
}