package org.m2ci.speech;

import cern.colt.list.tfloat.FloatArrayList;
import cern.colt.matrix.tfloat.FloatFactory1D;
import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.jet.math.tfloat.FloatFunctions;
import cern.jet.stat.tfloat.FloatDescriptive;

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
		samples.assign(FloatFunctions.plus(offset));
		return;
	}

	public void addToSamples(FloatMatrix1D other) {
		samples.assign(other, FloatFunctions.plus);
		return;
	}

	public void multiplySamples(float factor) {
		samples.assign(FloatFunctions.mult(factor));
	}

	/**
	 * Perform in-place smoothing of the samples by a moving average of
	 * windowSize samples.
	 * 
	 * @param windowSize
	 */
	public void smoothSamples(int windowSize) {
		FloatMatrix1D smoothedSamples = samples.copy();
		int firstSample = windowSize / 2;
		int lastSample = (int) (smoothedSamples.size() - windowSize / 2);
		for (int i = firstSample; i < lastSample; i++) {
			int j = i - windowSize / 2;
			FloatMatrix1D range = samples.viewPart(j, windowSize);
			FloatArrayList values = FloatFactory1D.dense.toList(range);
			float value = FloatDescriptive.mean(values);
			smoothedSamples.set(i, value);
		}
		samples.assign(smoothedSamples);
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
