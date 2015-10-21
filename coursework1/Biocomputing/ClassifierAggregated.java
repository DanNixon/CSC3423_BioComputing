/**
 * classifier.java
 *
 */

package Biocomputing;

import java.util.*;

public class ClassifierAggregated {
	Vector<Classifier> classifiers;	

	public ClassifierAggregated() {
		classifiers = new Vector<Classifier>();
	}

	public void addSubSolution(Classifier cl) {
		classifiers.addElement(cl);
	}

	public void printClassifier() {
		for(int i=0;i<classifiers.size();i++) {
			System.out.print("cl"+i+":");
			classifiers.elementAt(i).printClassifier();
		}
	}

	public void computeStats(InstanceSet is) {
		Instance[] instances = is.getInstancesOrig();

		double totalInstances=instances.length;
		double numCorrect=0;
		double numMatched=0;
		int i,j;

		for(i=0;i<instances.length;i++) {
			for(j=0;j<classifiers.size();j++) {
				int pred = classifiers.elementAt(j).classifyInstance(instances[i]);
				if(pred!=-1) {
					numMatched++;
					if(instances[i].getClassValue()==pred) {
						numCorrect++;
					}
					break;
				}
			}
		}

		double errors=(numMatched-numCorrect)/totalInstances*100;
		double notClassified=(totalInstances-numMatched)/totalInstances*100;
		double accuracy=numCorrect/totalInstances*100;

		System.out.printf("Accuracy %.2f%%, error rate %.02f%%, not classified %.02f%%%n",accuracy,errors,notClassified);
	}
}
