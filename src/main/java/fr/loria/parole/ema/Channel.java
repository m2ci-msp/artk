package fr.loria.parole.ema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Channel implements Iterable<Frame> {
	private String name;
	private HashMap<String, Track> tracks = new HashMap<String, Track>(Descriptor.NUM_DIMENSIONS_PER_COIL);
	
	private Vector<Frame> frames = new Vector<Frame>(200, 200);

	public Channel(String name) {
		assert !name.isEmpty();
		this.name = name;
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
	public Track getX() {
		return tracks.get(Descriptor.X);
	}

	/**
	 * @param x2
	 *            the x to set
	 */
	public void setX(Track x) {
		tracks.put(Descriptor.X, x);
	}

	/**
	 * @return the y
	 */
	public Track getY() {
		return tracks.get(Descriptor.Y);
	}

	/**
	 * @param y2
	 *            the y to set
	 */
	public void setY(Track y) {
		tracks.put(Descriptor.Y, y);
	}

	/**
	 * @return the z
	 */
	public Track getZ() {
		return tracks.get(Descriptor.Z);
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(Track z) {
		tracks.put(Descriptor.Z, z);
	}

	/**
	 * @return the phi
	 */
	public Track getPhi() {
		return tracks.get(Descriptor.PHI);
	}

	/**
	 * @param phi
	 *            the phi to set
	 */
	public void setPhi(Track phi) {
		tracks.put(Descriptor.PHI, phi);
	}

	/**
	 * @return the theta
	 */
	public Track getTheta() {
		return tracks.get(Descriptor.THETA);
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(Track theta) {
		tracks.put(Descriptor.THETA, theta);
	}

	/**
	 * @return the rms
	 */
	public Track getRms() {
		return tracks.get(Descriptor.RMS);
	}

	/**
	 * @param rms
	 *            the rms to set
	 */
	public void setRms(Track rms) {
		tracks.put(Descriptor.RMS, rms);
	}

	/**
	 * @return the extra
	 */
	public Track getExtra() {
		return tracks.get(Descriptor.EXTRA);
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(Track extra) {
		tracks.put(Descriptor.EXTRA, extra);
	}

	// public Frame getFrame(int i) {
	// Track x = this.x.getSample(i);
	// Track y = this.y.getSample(i);
	// Track z = this.z.getSample(i);
	// Track phi = this.phi.getSample(i);
	// Track theta = this.theta.getSample(i);
	// Track rms = this.rms.getSample(i);
	// Track extra = this.extra.getSample(i);
	// return new Frame(name, x, y, z, phi, theta, rms, extra);
	// }

	public String toString() {
		return String.format("%s\n  %s\n  %s\n  %s\n  %s\n  %s\n  %s\n  %s", getName(), getX(), getY(), getZ(), getPhi(),
				getTheta(), getRms(), getExtra());
	}

	public Iterator<Frame> iterator() {
		Iterator<Frame> frameIterator = frames.iterator();
		return frameIterator;
	}
	
	private void updateFrames() {
		
	}

}
