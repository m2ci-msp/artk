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
	 * Frame constructor
	 * 
	 * @param name
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param phi2
	 * @param theta2
	 * @param rms2
	 * @param extra2
	 */
	public Frame(String name, float x2, float y2, float z2, float phi2, float theta2, float rms2, float extra2) {
		setName(name);
		setX(x2);
		setY(y2);
		setZ(z2);
		setPhi(phi2);
		setTheta(theta2);
		setRms(rms2);
		setExtra(extra2);
	}

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
	 * @param x2
	 *            the x to set
	 */
	public void setX(float x2) {
		values.put(X, x2);
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return values.get(Y);
	}

	/**
	 * @param y2
	 *            the y to set
	 */
	public void setY(float y2) {
		values.put(Y, y2);
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

	public String toString() {
		return String.format("%s:\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n", getName(), X,
				getX(), Y, getY(), Z, getZ(), PHI, getPhi(), THETA, getTheta(), RMS, getRms(), EXTRA, getExtra());
	}

	public boolean equals(Frame other) {
		boolean isFrame = other instanceof Frame;
		boolean nameEquals = this.name.equals(other.name);
		boolean valuesEquals = this.values.equals(other.values);
		return isFrame && nameEquals && valuesEquals;
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
