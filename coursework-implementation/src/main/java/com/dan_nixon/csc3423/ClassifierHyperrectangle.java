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

/**
 * Classifier using a genetic algorithm and hyperrectangle knowledge
 * representation to solve an n-dimensional classification problem.
 */
public class ClassifierHyperrectangle extends Classifier
{
  public static final int MAX_ITERATIONS = 100;
  public static final int POPULATION_SIZE = 100;
  public static final double TARGET_LEARN_ACCURACY = 0.95;

  /**
   * Creates an array of Genes describing the hyperrectangle needed for
   * classification of a given dimensionality.
   *
   * @param conf JGAP configuration
   * @param dimensions Number of dimensions
   * @param bounds Bounds for dimensional genes
   * @return Array of Genes
   * @throws InvalidConfigurationException
   */
  public static Gene[] createSampleGenes(Configuration conf, int dimensions, double[][] bounds) throws InvalidConfigurationException
  {
    Gene[] sampleGenes = new Gene[dimensions + 1];

    // Vertex position genes
    for (int i = 0; i < dimensions; i++)
    {
      CompositeGene g = new CompositeGene(conf);

      g.addGene(new DoubleGene(conf, bounds[i][0], bounds[i][1]));
      g.addGene(new DoubleGene(conf, bounds[i][0], bounds[i][1]));

      sampleGenes[i] = g;
    }

    // Classification gene
    sampleGenes[sampleGenes.length - 1] = new IntegerGene(conf, 0, 1);

    return sampleGenes;
  }

  /**
   * Gets the upper and lower bounds for each dimension of an InstanceSet.
   *
   * Array is in format: [dimension][0:lower, 1:upper]
   *
   * @param is InstanceSet to get bounds of
   * @return Array of bounds
   */
  public static double[][] getInstanceSetBounds(InstanceSet is)
  {
    int dimensions = Attributes.getNumAttributes();
    double bounds[][] = new double[dimensions][2];

    for (int i = 0; i < dimensions; i++)
    {
      bounds[i][0] = Double.MAX_VALUE;
      bounds[i][1] = Double.MIN_VALUE;
    }

    for (Instance inst : is.getInstances())
    {
      for (int i = 0; i < dimensions; i++)
      {
        double v = inst.getRealAttribute(i);

        if (v < bounds[i][0])
          bounds[i][0] = v;

        if (v > bounds[i][1])
          bounds[i][1] = v;
      }
    }

    return bounds;
  }

  /**
   * Adds padding to an array of bounds.
   *
   * Subtracting from lower bounds and adding to upper bounds.
   *
   * @param bounds Array of bounds
   * @param padding Amount of padding
   * @return Array of bounds
   */
  public static double[][] padBounds(double[][] bounds, double padding)
  {
    int dimensions = bounds[0].length;

    for (int i = 0; i < dimensions; i++)
    {
      bounds[i][0] = bounds[i][0] - padding;
      bounds[i][1] = bounds[i][1] + padding;
    }

    return bounds;
  }

  /**
   * Learn a new classifier from a training set.
   *
   * @param trainingSet Training set
   * @throws InvalidConfigurationException
   */
  public ClassifierHyperrectangle(InstanceSet trainingSet) throws InvalidConfigurationException
  {
    int dimensions = Attributes.getNumAttributes();

    // Configuration
    DefaultConfiguration.reset();
    Configuration conf = new DefaultConfiguration();
    conf.setPopulationSize(POPULATION_SIZE);

    // Setup fitness function
    FitnessFunction fit = new HyperrectFitnessFunction(trainingSet);
    conf.setFitnessFunction(fit);

    // Determine the bounds of the training set per axis and add a little
    // padding to them
    double[][] bounds = getInstanceSetBounds(trainingSet);
    bounds = padBounds(bounds, 1.0);

    // Setup chromosome
    Chromosome sample = new Chromosome(conf, createSampleGenes(conf, dimensions, bounds));
    conf.setSampleChromosome(sample);

    // Generate initial population
    Genotype population = Genotype.randomInitialGenotype(conf);

    // Evolve
    int i = 0;
    IChromosome bestSolution = null;
    for (i = 1; i < MAX_ITERATIONS + 1; i++)
    {
      population.evolve();
      bestSolution = population.getFittestChromosome();

      double bestFitness = bestSolution.getFitnessValue();
      if (bestFitness > TARGET_LEARN_ACCURACY)
        break;
    }
    ms_totalIterations += i;
    System.out.println("GA learning exited after " + i + " iterations");
    System.out.println("(total iterations so far: " + ms_totalIterations + ")");

    // Record best
    updateFromChromosome(bestSolution);
  }

  /**
   * Create a classifier from Genes contained in a Chromosome.
   *
   * @param c CHromosome
   */
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

      double rectUpperI = m_dimensions[i][1];
      double rectLowerI = m_dimensions[i][0];

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

  /**
   * Retrieves the array of all dimensions.
   *
   * In format: [[dimension][0:lower, 1:upper]
   *
   * @return Array of vertices
   */
  public double[][] getDimensions()
  {
    return m_dimensions;
  }

  /**
   * Retrieves a dimension by index.
   *
   * In format: [0:lower, 1:upper]
   *
   * @param i Dimension index
   * @return Dimension
   */
  public double[] getDimension(int i)
  {
    return m_dimensions[i];
  }

  /**
   * Gets the class value of this classifier.
   *
   * @return Class value
   */
  public int getClassValue()
  {
    return m_classValue;
  }

  /**
   * Sets the dimensions and class value from the values of Genes in a
   * Chromosome.
   *
   * @param c Chromosome
   */
  private void updateFromChromosome(IChromosome c)
  {
    Gene[] genes = c.getGenes();
    int dimensions = genes.length - 1;

    m_dimensions = new double[dimensions][2];

    for (int i = 0; i < dimensions; i++)
    {
      CompositeGene comp = (CompositeGene) genes[i];
      m_dimensions[i][0] = (Double) comp.getGenes().get(0).getAllele();
      m_dimensions[i][1] = (Double) comp.getGenes().get(1).getAllele();
    }

    m_classValue = (Integer) genes[dimensions].getAllele();
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Hyperrectangle[class=");
    sb.append(m_classValue);
    sb.append(",verticies=[");
    for (int i = 0; i < m_dimensions.length; i++)
    {
      if (i > 0)
        sb.append(",");
      sb.append(Arrays.toString(m_dimensions[i]));
    }
    sb.append("]]");
    return sb.toString();
  }

  /**
   * Gets the number of ierations performed over all executions of the
   * classifier.
   *
   * @return Total iterations
   */
  public static int getTotalIterations()
  {
    return ms_totalIterations;
  }

  private int m_classValue;
  private double[][] m_dimensions;
  private static int ms_totalIterations;
}
