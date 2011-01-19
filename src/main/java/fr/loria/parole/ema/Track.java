package fr.loria.parole.ema;

import java.util.Arrays;

public class Track {
	private String name;
	private float[] samples;

	public Track(String name, float[] ds) {
		setName(name);
		setSamples(ds);
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
	public float[] getSamples() {
		return samples;
	}

	/**
	 * @param ds
	 *            the data to set
	 */
	private void setSamples(float[] ds) {
		this.samples = ds;
	}

	public float getSample(int i) {
		return samples[i];
	}

	public int getNumberOfSamples() {
		return samples.length;
	}

	public String toString() {
		return name + ": " + Arrays.toString(samples);
	}

	public boolean equals(Track other) {
		boolean isTrack = other instanceof Track;
		boolean nameEquals = this.name.equals(other.name);
		boolean samplesEquals = Arrays.equals(this.samples, other.samples);
		return isTrack && nameEquals && samplesEquals;
	}
}
