/*
 * Attribute.java
 *
 */
package Biocomputing;

import java.util.*;

/**
 *
 */
public class Attribute {
        public final static int NOMINAL = 0;
        public final static int REAL = 1;

	int type;
	String name;
	Vector<String> nominalValues;
	double min;
	double max;
	boolean firstTime;

	public Attribute() {
		type=-1;
	}

	public void setType(int _type) {
		if(type!=-1) {
			System.err.println("Type already fixed !!");
			System.exit(1);
		}
		type=_type;
		firstTime=true;

		if(type==NOMINAL) {
			nominalValues=new Vector<String>();
		}
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		name=new String(_name);
	}

	public double minAttribute() {
		return min;
	}

	public double maxAttribute() {
		return max;
	}

	public void enlargeBounds(double value) {
		if(type!=REAL) return;

		if(firstTime) {
			min=value;
			max=value;
			firstTime=false;
		} else {
			if(value<min) {
				min=value;
			}
			if(value>max) {
				max=value;
			}
		}
	}


	public void addNominalValue(String value) {
		if(type!=NOMINAL) return;
		nominalValues.addElement(new String(value));
	}

	public int getNumNominalValues() {
		if(type!=NOMINAL) return -1;
		return nominalValues.size();
	}
	public String getNominalValue(int value) {
		if(type!=NOMINAL) return null;
		return nominalValues.elementAt(value);
	}

	public int convertNominalValue(String value) {
		return nominalValues.indexOf(value);
	}

	public boolean equals(Attribute attr) {
		if(!name.equals(attr.name)) return false;
		if(attr.type!=type) return false;
		if(type==NOMINAL) {
			if(!nominalValues.equals(attr.nominalValues)) 
				return false;
		}
		return true;
	}
}
