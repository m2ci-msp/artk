package fr.loria.parole.ema;


import java.util.Arrays;

public class Track {
	private final String name;
	private float[] data;

	public Track(String name) {
		this.name = name;
	}

	public Track(String name, float[] data) {
		this.name = name;
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public float[] getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(float[] data) {
		this.data = data;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + ": " + Arrays.toString(data);
	}
	
	public float getSample(int i) {
		return data[i];
	}
}
