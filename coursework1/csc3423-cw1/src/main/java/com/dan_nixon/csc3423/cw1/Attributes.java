/*
 * Attributes.java
 *
 */
package Biocomputing;
import java.util.*;

/**
 *
 */
public class Attributes {
	
	private static Vector<Attribute> attributes=new Vector<Attribute>();
	private static boolean hasNominal=false;
	private static boolean hasReal=false;

	public static int numClasses;
	
	public static void addAttribute(Attribute attr) {
		attributes.addElement(attr);
		if(attr.getType()==Attribute.NOMINAL) hasNominal=true;
		if(attr.getType()==Attribute.REAL) hasReal=true;
	}

	public static boolean hasNominalAttributes() {
		return hasNominal;
	}
	public static boolean hasRealAttributes() {
		return hasReal;
	}
	
	public static Attribute getAttribute(String _name) {
	   int pos=attributes.indexOf(_name);
	   return attributes.elementAt(pos);
	}
	
	public static Attribute getAttribute(int pos) {
	   return attributes.elementAt(pos);
	}

	public static int getNumAttributes() {
		// The class attribute (last one) does not count
		return attributes.size()-1;
	}

	public static void endOfHeader() {
		Attribute a=attributes.lastElement();
		numClasses=a.getNumNominalValues();
	}
}
