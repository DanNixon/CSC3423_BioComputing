/*
 * InstanceSet.java
 *
 */

/**
 *
 */
package Biocomputing;

import Biocomputing.*;
import java.util.*;

public class InstanceSet {
	
	private Instance[] instanceSet;
	private Instance[] origSet;
	private String header;
	
	/** Creates a new instance of InstanceSet */
	public InstanceSet(String fileName,boolean isTrain) {
		ParserARFF parser=new ParserARFF(fileName,isTrain);
		parser.parseHeader();
		header=parser.getHeader();
		String instance;
		Vector<Instance> tempSet=new Vector<Instance>(1000,100000);
		while((instance=parser.getInstance())!=null) {
			tempSet.addElement(new Instance(instance,isTrain));
		}

		int sizeInstance=tempSet.size();
		instanceSet=new Instance[sizeInstance];
		origSet=new Instance[sizeInstance];
		for (int i=0; i<sizeInstance; i++) {
			instanceSet[i]=tempSet.elementAt(i);
			origSet[i]=instanceSet[i];
		}
	}


	public int removeInstances(Classifier cl) {
		Vector<Instance> tempSet=new Vector<Instance>(instanceSet.length);
		int i;
		for(i=0;i<instanceSet.length;i++) {
			int pred = cl.classifyInstance(instanceSet[i]);
			if(pred==-1) {
				tempSet.addElement(instanceSet[i]);
			}
		}
		
		int newSize=tempSet.size();
		Instance []newSet = new Instance[newSize];
		for (i=0; i<newSize; i++) {
			newSet[i]=tempSet.elementAt(i);
		}
		int removed = instanceSet.length-newSize;
		instanceSet=newSet;

		return removed;
	}
	
	/**
	 *  Moves cursor to the next instance.
	 */
	public int numInstances() {
		return instanceSet.length;
	}

	public int numInstancesOrig() {
		return origSet.length;
	}
	
	/**
	 *  Gets the instance that is located at the cursor position.
	 */
	public Instance getInstance(int whichInstance) {
		return instanceSet[whichInstance];
	}
	public Instance getInstanceOrig(int whichInstance) {
		return origSet[whichInstance];
	}

	public Instance[] getInstances() {
		return instanceSet;
	}

	public Instance[] getInstancesOrig() {
		return origSet;
	}

	public String getHeader() {
		return header;
	}
}
