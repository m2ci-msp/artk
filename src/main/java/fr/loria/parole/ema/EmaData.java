package fr.loria.parole.ema;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix2D;
import cern.colt.matrix.tobject.ObjectMatrix1D;
import cern.colt.matrix.tobject.impl.DenseObjectMatrix1D;

import com.ibm.icu.lang.UCharacter;

public class EmaData {
	public static final String X = "X";
	public static final String Y = "Y";
	public static final String Z = "Z";
	public static final String PHI = String.valueOf((char) UCharacter.getCharFromName("GREEK CAPITAL LETTER PHI"));
	public static final String THETA = String.valueOf((char) UCharacter.getCharFromName("GREEK CAPITAL LETTER THETA"));
	public static final String RMS = "RMS";
	public static final String EXTRA = "Extra";

	public static final int NUM_DIMENSIONS_PER_COIL = 7;

	protected ObjectMatrix1D names = new DenseObjectMatrix1D(0);
	protected FloatMatrix2D data = new DenseFloatMatrix2D(0, 0);

	protected FloatMatrix1D getRow(int index) {
		return data.viewRow(index);
	}

	protected FloatMatrix1D getColumn(int index) {
		return data.viewColumn(index);
	}

	public int getNumberOfFrames() {
		return data.columns();
	}

	public String toString() {
		String string = String.format("Names: %s\nData: %s", names, data);
		return string;
	}

}
