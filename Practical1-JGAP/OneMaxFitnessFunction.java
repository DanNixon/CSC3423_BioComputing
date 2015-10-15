
import org.jgap.*;
import org.jgap.impl.*;

public class OneMaxFitnessFunction extends FitnessFunction {

	public OneMaxFitnessFunction() {
	}

	public double evaluate( IChromosome ind ) {
		int numBits = ind.size();
		int countOnes = 0;

		for(int i=0;i<numBits;i++) {
			countOnes+=((Integer)ind.getGene(i).getAllele()).intValue();
		}

		return (double)countOnes/(double)numBits;
	}

	public static String getPhenotype(IChromosome ind) {
		int numBits = ind.size();
		String phenotype="";

		for(int i=0;i<numBits;i++) {
			phenotype+=((Integer)ind.getGene(i).getAllele()).intValue();
		}

		return phenotype;
	}


	public static void main(String[] args) throws Exception {
		// Start with a DefaultConfiguration, which comes setup with the
		// most common settings.
		// -------------------------------------------------------------
		Configuration conf = new DefaultConfiguration();
	
		// Creates an object of the fitness function class
		FitnessFunction myFunc = new OneMaxFitnessFunction();
		conf.setFitnessFunction( myFunc );
	
		// Now we need to tell the Configuration object how we want our
		// Chromosomes to be setup. We do that by actually creating a
		// sample Chromosome and then setting it on the Configuration
		// object.
		// 
		// The number of bits of the OneMax problem is specified as
		// a command line argument. We have to create an array of the
		// Gene object of such size, and create objects of the class
		// IntegerGene which is the suitable class for specifying bits
		// The parameters of the IntegerGene class are the configuration
		// object and the lower and upper bound for the integer value
		// --------------------------------------------------------------
		int problemSize = Integer.parseInt(args[0]);
		Gene[] sampleGenes = new Gene[ problemSize ];
		for(int i=0;i<problemSize;i++) {
			sampleGenes[i] = new IntegerGene(conf, 0, 1);
		}
	
		Chromosome sampleChromosome = new Chromosome(conf, sampleGenes );
		conf.setSampleChromosome( sampleChromosome );
	
		// Finally, we need to tell the Configuration object how many
		// Chromosomes we want in our population. The more Chromosomes,
		// the larger the number of potential solutions (which is good
		// for finding the answer), but the longer it will take to evolve
		// the population each round. We'll set the population size to
		// 500 here.
		// --------------------------------------------------------------
		conf.setPopulationSize( 100 );
	
		Genotype population = Genotype.randomInitialGenotype( conf );
		IChromosome bestSolutionSoFar;
	
		int numIterations = Integer.parseInt(args[1]);
		for( int i = 0; i < numIterations; i++ ) {
			population.evolve();
			bestSolutionSoFar = population.getFittestChromosome();
		
			System.out.println("Iteration "+i+". The best individual is "
				+OneMaxFitnessFunction.getPhenotype(bestSolutionSoFar)
				+" with fitness "+bestSolutionSoFar.getFitnessValue());
		}
	}
}
