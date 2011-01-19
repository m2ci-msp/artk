package fr.loria.parole.ema;

import com.ibm.icu.lang.UCharacter;

public class Descriptor {
	public static final String X = "X";
	public static final String Y = "Y";
	public static final String Z = "Z";
	public static final String PHI = String.valueOf((char) UCharacter.getCharFromName("GREEK CAPITAL LETTER PHI"));
	public static final String THETA = String.valueOf((char) UCharacter.getCharFromName("GREEK CAPITAL LETTER THETA"));
	public static final String RMS = "RMS";
	public static final String EXTRA = "Extra";

	public static final int NUM_DIMENSIONS_PER_COIL = 7;
}
