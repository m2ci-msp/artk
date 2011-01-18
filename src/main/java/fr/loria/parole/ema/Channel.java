package fr.loria.parole.ema;

import java.util.Iterator;
import java.util.Vector;

public class Channel implements Iterable<Frame> {
	private String name;
	private Track x;
	private Track y;
	private Track z;
	private Track phi;
	private Track theta;
	private Track rms;
	private Track extra;

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
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(Track x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public Track getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(Track y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public Track getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(Track z) {
		this.z = z;
	}

	/**
	 * @return the phi
	 */
	public Track getPhi() {
		return phi;
	}

	/**
	 * @param phi
	 *            the phi to set
	 */
	public void setPhi(Track phi) {
		this.phi = phi;
	}

	/**
	 * @return the theta
	 */
	public Track getTheta() {
		return theta;
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(Track theta) {
		this.theta = theta;
	}

	/**
	 * @param rms
	 *            the rms to set
	 */
	public void setRms(Track rms) {
		this.rms = rms;
	}

	/**
	 * @return the rms
	 */
	public Track getRms() {
		return rms;
	}

	/**
	 * @return the extra
	 */
	public Track getExtra() {
		return extra;
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(Track extra) {
		this.extra = extra;
	}

	public Frame getFrame(int i) {
		float x = this.x.getSample(i);
		float y = this.y.getSample(i);
		float z = this.z.getSample(i);
		float phi = this.phi.getSample(i);
		float theta = this.theta.getSample(i);
		float rms = this.rms.getSample(i);
		float extra = this.extra.getSample(i);
		return new Frame(name, x, y, z, phi, theta, rms, extra);
	}

	public String toString() {
		return String.format("%s\n  %s\n  %s\n  %s\n  %s\n  %s\n  %s\n  %s", name, x, y, z, phi, theta, rms, extra);
	}

	public Iterator<Frame> iterator() {
		Vector<Frame> frames = new Vector(100);
		Iterator<Frame> frameIterator = frames.iterator();
		return frameIterator;
	}

}
