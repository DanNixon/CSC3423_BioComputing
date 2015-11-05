/**
 * Classifier for the RandomSphere toy representation
 */

package com.dan_nixon.csc3423.framework;

public class ClassifierRandomSphere extends Classifier implements Cloneable {
	double []center;
	double radius;
	int classOfSphere;
	int numAtt;

	public ClassifierRandomSphere(InstanceSet trainingSet) {
		//Choosing and fetching the instance from the training set that will be the centre of the sphere
		int seedInstance=Rand.getInteger(0,trainingSet.numInstances()-1);
		Instance ins=trainingSet.getInstance(seedInstance);


		//Copying the centre of the sphere from the instance
		numAtt=Attributes.getNumAttributes();
		center = new double[numAtt];
		for(int i=0;i<numAtt;i++) {
			center[i]=ins.getRealAttribute(i);
		}

		//Initialising the radius to be a number between 0 and 2
		radius = Rand.getReal()*4;

		// Setting up the class of the sphere to be the majority class of the examples within
		classOfSphere=computeMajorityClass(trainingSet);
	}

	private int computeMajorityClass(InstanceSet trainingSet) {
		int []classCounts = new int[Attributes.numClasses];
		Instance []instances = trainingSet.getInstances();

		// Counting the instances of each class within the sphere
		for(int i=0;i<instances.length;i++) {
			int pred = classifyInstance(instances[i]);
			if(pred!=-1) {
				classCounts[instances[i].getClassValue()]++;
			}
		}

		// Identifying the maximum class count
		int clMax=0;
		int numMax=classCounts[0];
		for(int i=1;i<Attributes.numClasses;i++) {
			if(classCounts[i]>numMax) {
				clMax=i;
				numMax=classCounts[i];
			}
		}

		return clMax;		
	}

	public int classifyInstance(Instance ins) {
		double dist=0;
		
		for(int i=0;i<numAtt;i++) {
			dist+=Math.pow((center[i]-ins.getRealAttribute(i)),2);
		}
		dist=Math.sqrt(dist);

		if(dist<=radius) {
			return classOfSphere;
		}
		return -1;
	}
	
	public void printClassifier() {
		String cl="Center ";
		for(int i=0;i<numAtt;i++) {
			cl+=center[i]+",";
		}
		cl+=" radius "+radius+", class "+classOfSphere;

		System.out.println(cl);
	}

}
