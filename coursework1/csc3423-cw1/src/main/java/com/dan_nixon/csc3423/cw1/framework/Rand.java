/*
 * Rand.java
 *
 */

package Biocomputing;

import java.util.*;

public class Rand {

	private static MTwister random;

    /** Generates a new instance of Random */
	public static void initRand() {
		Random tmpRnd=new Random();
		long seed = tmpRnd.nextLong();
		random = new MTwister(seed);
		System.out.println("Random seed is "+seed);
	}

	public static void initRand(long seed) {
		random = new MTwister(seed);
		System.out.println("Random seed is "+seed);
	}


    /**
     *  Returns a random real between [0,1]
     */ 
	public static double getReal() {
		return random.genrand_real1();
	}

    /**
     *  Returns a random long between [uLow,uHigh]
     */
	public static int getInteger(int uLow, int uHigh) {
		return (uLow +
			(int) (random.genrand_real2() *
			       (uHigh + 1 - uLow)));
	}

}
