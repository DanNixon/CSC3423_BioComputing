package Biocomputing;

import java.util.*;

/*
 * Instance.java
 *
 */

/**
 *
 */
public class Instance {
	int[] nominalValues;
	double []realValues;
	boolean []missing;
	int instanceClass;
	boolean isTrain;
	boolean hasMissing;
	int numAtt;
	
	public Instance(String def,boolean _isTrain) {
		StringTokenizer st=new StringTokenizer(def,",");
		numAtt = Attributes.getNumAttributes()+1;
		nominalValues=new int[numAtt];
		realValues=new double[numAtt];
		missing=new boolean[numAtt];
		isTrain=_isTrain;
		hasMissing=false;

		for(int i=0;i<numAtt;i++) missing[i]=false;

		int attributeCount=0;
		while (st.hasMoreTokens()) {
			if(attributeCount>numAtt) {
				System.out.println("Instance "+def+" has more attributes than defined "+numAtt+"<->"+attributeCount);
				System.exit(1);
			}
			String att=st.nextToken();

			if(att.equalsIgnoreCase("?")) {
				missing[attributeCount]=true;
				hasMissing=true;
			} else if(Attributes.getAttribute(attributeCount).getType()==Attribute.REAL) {
				try {
					realValues[attributeCount]=Double.parseDouble(att);
				} catch(NumberFormatException e) {
					System.out.println("Attribute "+attributeCount+" of "+def+" is not a real value");
					e.printStackTrace();
					System.exit(1);
				}
			} else if(Attributes.getAttribute(attributeCount).getType()==Attribute.NOMINAL) {
				nominalValues[attributeCount]=Attributes.getAttribute(attributeCount).convertNominalValue(att);
				if(nominalValues[attributeCount]==-1) {
					System.out.println("Attribute "+attributeCount+" of "+def+" is not a valid nominal value");
					System.exit(1);
				}
			}
			attributeCount++;
		}

		if(attributeCount!=numAtt) {
			System.out.println("Instance "+def+" has less attributes than defined");
			System.exit(1);
		}
		if(missing[numAtt-1]) {
			System.out.println("The class of the instance cannot be missing");
			System.exit(1);
		} else {
			instanceClass=nominalValues[numAtt-1];
		}
		
		if(isTrain) {
			for(int i=0;i<numAtt-1;i++) {
				if(!missing[i]) {
					if(Attributes.getAttribute(i).getType()==Attribute.REAL) {
						Attributes.getAttribute(i).enlargeBounds(realValues[i]);
					}
				}
			}
		}
	}

	public double getRealAttribute(int attr) {
		return realValues[attr];
	}

	public int getNominalAttribute(int attr) {
		return nominalValues[attr];
	}

	public void setRealAttribute(int attr,double value) {
		realValues[attr]=value;
	}
	public void setNominalAttribute(int attr,int value) {
		nominalValues[attr]=value;
	}


	public int[] getNominalAttributes() {
		return nominalValues;
	}

	public double[] getRealAttributes() {
		return realValues;
	}

	public boolean[] areAttributesMissing() {
		return missing;
	}

	public int getClassValue() {
		return instanceClass;
	}

	public boolean hasMissingValues() {
		return hasMissing;
	}

	public String toString() {
		String ins="";
		for(int i=0;i<numAtt-1;i++) {
			if(Attributes.getAttribute(i).getType()
				== Attribute.REAL) {
				ins+=realValues[i]+",";
			} else if(Attributes.getAttribute(i).getType()
				== Attribute.NOMINAL) {
				ins+=nominalValues[i]+",";
			}
		}
		ins+=instanceClass;
		return ins;
	}
}
