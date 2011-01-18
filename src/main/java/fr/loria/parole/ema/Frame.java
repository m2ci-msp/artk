package fr.loria.parole.ema;


import com.ibm.icu.lang.UCharacter;

public class Frame {
	public final String name;
	public final float x;
	public final float y;
	public final float z;
	public final float phi;
	public final float theta;
	public final float rms;
	public final float extra;

	public Frame(String name, float x, float y, float z, float phi, float theta, float rms, float extra) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.phi = phi;
		this.theta = theta;
		this.rms = rms;
		this.extra = extra;
	}

	public String toString() {
		char phiChar = (char) UCharacter.getCharFromName("GREEK CAPITAL LETTER PHI");
		char thetaChar = (char) UCharacter.getCharFromName("GREEK CAPITAL LETTER THETA");
		return String
				.format("%s:\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n%7s: %10f\n",
						name, "X", x, "Y", y, "Z", z, phiChar, phi, thetaChar, theta, "RMS", rms, "Extra", extra);
	}

	public float[] getDifference(Frame other) {
		float[] diff = new float[7];
		diff[0] = other.x - this.x;
		diff[1] = other.y - this.y;
		diff[2] = other.z - this.z;
		diff[3] = other.phi - this.phi;
		diff[4] = other.theta - this.theta;
		diff[5] = other.rms - this.rms;
		diff[6] = other.extra - this.extra;
		return diff;
	}
}
