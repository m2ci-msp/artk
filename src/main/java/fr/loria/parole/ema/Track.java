package fr.loria.parole.ema;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.jet.math.tfloat.FloatFunctions;

public class Track {
	private String name;
	private FloatMatrix1D samples;

	public Track(String name, FloatMatrix1D floatMatrix1D) {
		setName(name);
		setSamples(floatMatrix1D);
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
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the data
	 */
	public FloatMatrix1D getSamples() {
		return samples;
	}

	/**
	 * @param ds
	 *            the data to set
	 */
	private void setSamples(FloatMatrix1D samples) {
		this.samples = samples;
	}
	
	public void addToSamples(float offset) {
		// TODO there has to be a better way to do this somewhere in colt!
//		float[] samples = getSamples().toArray();
		samples.assign(FloatFunctions.plus(offset));
		return;
	}

	public float getSample(int i) {
		return samples.get(i);
	}

	public int getNumberOfSamples() {
		return (int) samples.size();
	}

	public String toString() {
		return name + ": " + samples;
	}

	public boolean equals(Track other) {
		boolean isTrack = other instanceof Track;
		boolean nameEquals = this.name.equals(other.name);
		boolean samplesEquals = this.samples.equals(other.samples);
		return isTrack && nameEquals && samplesEquals;
	}
	
	public Track copy() {
		return new Track(getName(), getSamples().copy());
	}
}
