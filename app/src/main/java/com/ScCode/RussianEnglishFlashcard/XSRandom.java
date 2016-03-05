package com.ScCode.RussianEnglishFlashcard;

/**
 * Created by ScottShottGG on 26-Sep-14.
 */
import java.util.Random;

/**
 * A subclass of java.util.random that implements the
 * Xorshift random number generator
 */

/*
 * English-Russian Flashcard Developer Comments:
 *
 * All creadit to http://demesos.blogspot.com/2011/09/replacing-java-random-generator.html
 * I am not using this but I may.
 *
 * Big thanks to this Demesos
 */
public class XSRandom extends Random
{
	private long seed;

	public XSRandom(long seed)
	{
		this.seed = seed;
	}

	protected int next(int nbits)
	{
		long x = seed;
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		seed = x;
		x &= ((1L << nbits) - 1);
		return (int) x;
	}
}
