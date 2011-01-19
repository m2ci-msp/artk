package fr.loria.parole.ema;

import java.util.HashMap;

import com.ibm.icu.lang.UCharacter;

public class Frame {
	private String name;
	private HashMap<String, Float> values = new HashMap<String, Float>();
	private String X = "X";
	private String Y = "Y";
	private String Z = "Z";
	private String PHI = String.valueOf((char) UCharacter.getCharFromName("GREEK CAPITAL LETTER PHI"));
	private String THETA = String.valueOf((char) UCharacter.getCharFromName("GREEK CAPITAL LETTER THETA"));
	private String RMS = "RMS";
	private String EXTRA = "Extra";

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return values.get(X);
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		values.put(X, x);
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return values.get(Y);
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		values.put(Y, y);
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return values.get(Z);
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(float z) {
		values.put(Z, z);
	}

	/**
	 * @return the phi
	 */
	public float getPhi() {
		return values.get(PHI);
	}

	/**
	 * @param phi
	 *            the phi to set
	 */
	public void setPhi(float phi) {
		values.put(PHI, phi);
	}

	/**
	 * @return the theta
	 */
	public float getTheta() {
		return values.get(THETA);
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(float theta) {
		values.put(THETA, theta);
	}

	/**
	 * @return the rms
	 */
	public float getRms() {
		return values.get(RMS);
	}

	/**
	 * @param rms
	 *            the rms to set
	 */
	public void setRms(float rms) {
		values.put(RMS, rms);
	}

	/**
	 * @return the extra
	 */
	public float getExtra() {
		return values.get(EXTRA);
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(float extra) {
		values.put(EXTRA, extra);
	}

	/**
	 * Frame constructor
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param z
	 * @param phi
	 * @param theta
	 * @param rms
	 * @param extra
	 */
	public Frame(String name, float x, float y, float z, float phi, float theta, float rms, float extra) {
		setName(name);
		setX(x);
		setY(y);
		setZ(z);
		setPhi(phi);
		setTheta(theta);
		setRms(rms);
		setExtra(extra);
	}

	public String toString() {
		return String.format("%s:\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n", getName(), X,
				getX(), Y, getY(), Z, getZ(), PHI, getPhi(), THETA, getTheta(), RMS, getRms(), EXTRA, getExtra());
	}

	public boolean equals(Frame other) {
		boolean namesEqual = this.name.equals(other.name);
		boolean valuesEqual = this.values.equals(other.values);
		return namesEqual && valuesEqual;
	}

	// public float[] getDifference(Frame other) {
	// float[] diff = new float[7];
	// diff[0] = other.x - this.x;
	// diff[1] = other.y - this.y;
	// diff[2] = other.z - this.z;
	// diff[3] = other.phi - this.phi;
	// diff[4] = other.theta - this.theta;
	// diff[5] = other.rms - this.rms;
	// diff[6] = other.extra - this.extra;
	// return diff;
	// }
}
