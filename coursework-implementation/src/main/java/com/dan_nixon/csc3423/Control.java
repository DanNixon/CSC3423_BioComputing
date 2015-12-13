/*
 * Control.java
 */

package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.*;
import com.dan_nixon.csc3423.vis.HyperrectangleFrame;
import org.jgap.InvalidConfigurationException;

/**
 * The type of classifier to use.
 */
enum ClassifierType
{
  CLASSIFIER_RANDOMSPHERE,
  CLASSIFIER_GAHYPERRECTANGLE,
  CLASSIFIER_NEURALNET
}

public class Control {
  // README: Set classifer type here
  static final ClassifierType CLASSIFIER_TYPE = ClassifierType.CLASSIFIER_NEURALNET;
  // README: Set to enable/disable visualisations
  static final boolean VISUALISE = false;

  /** Creates a new instance of Control */
  public Control() {
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Rand.initRand();
    // Specifyig a random seed will allow you to repeat experiments with the same exact conditions
    //Rand.initRand(0);

    // Start clock
    long t1=System.currentTimeMillis();

    // Loading the training data, initialising the object that will collect the classifiers
    InstanceSet trainingSet = new InstanceSet(args[0],true);
    ClassifierAggregated solution = new ClassifierAggregated();
    int countIterations=0;
    System.out.println();
    
    // Create the hyperrectangle visualisation if it is needed
    if (CLASSIFIER_TYPE == ClassifierType.CLASSIFIER_GAHYPERRECTANGLE && VISUALISE)
      m_hyperrectFrame = new HyperrectangleFrame(trainingSet);

    // We will train classifiers while there are instances in the training set to cover
    while(trainingSet.numInstances()>0) {
      // Calling a (nature-inspired) optimisation method to generate one classifiers
      Classifier subSolution = generateSubsolution(trainingSet);
      
      // Printing the stats on training data of that classifier
      System.out.print("Classifier of iteration "+countIterations+". ");
      subSolution.computeStats(trainingSet);

      // Adding the classifier to the collection and removing the instances from the training set that have been covered by that classifier
      solution.addSubSolution(subSolution);
      int numRemoved = trainingSet.removeInstances(subSolution);
      System.out.println("Iteration "+countIterations+", removed "+numRemoved+" instances, instances left "+trainingSet.numInstances());

      // Printing the stats of the current collection of classifiers on training data
      System.out.print("Overall stats at iteration "+countIterations+". ");
      solution.computeStats(trainingSet);
      countIterations++;
      System.out.println();
    }

    // Printing the final collection of classifiers
    System.out.println("Final classifier");
    solution.printClassifier();
    System.out.println();

    // Computing the stats of the final collection of classifiers on test data
    InstanceSet testSet = new InstanceSet(args[1],false);
    System.out.println("Stats on test data");
    solution.computeStats(testSet);
    System.out.println();

    // End clock and printing the time it took the algorithm to run
    long t2=System.currentTimeMillis();
    System.out.println("Total time: "+((t2-t1)/1000.0));
  }

  public static Classifier generateSubsolution(InstanceSet trainingSet)
  {
    switch (CLASSIFIER_TYPE)
    {
      case CLASSIFIER_RANDOMSPHERE:
        return new ClassifierRandomSphere(trainingSet);
      case CLASSIFIER_NEURALNET:
        return new ClassifierNN(trainingSet, VISUALISE);
      case CLASSIFIER_GAHYPERRECTANGLE:
        try
        {
          ClassifierHyperrectangle c = new ClassifierHyperrectangle(trainingSet);
          if (m_hyperrectFrame != null)
          {
            m_hyperrectFrame.addClassifier(c);
            m_hyperrectFrame.repaint();
          }
          return c;
        }
        catch (InvalidConfigurationException ice)
        {
          return null;
        }
    }
    
    return null;
  }
  
  public static HyperrectangleFrame m_hyperrectFrame;
}
