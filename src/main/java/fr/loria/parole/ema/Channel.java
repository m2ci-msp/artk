package fr.loria.parole.ema;

import java.util.HashMap;

import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tobject.ObjectMatrix1D;

public class Channel extends Sweep {

	public Channel(ObjectMatrix1D names, FloatMatrix2D data) {
		super(names, data);
		// TODO robust checking
		setX(getTrack(0));
		setY(getTrack(1));
		setZ(getTrack(2));
		setPhi(getTrack(3));
		setTheta(getTrack(4));
		setRms(getTrack(5));
		setExtra(getTrack(6));
		return;
	}

	private HashMap<String, Track> tracks = new HashMap<String, Track>(EmaData.NUM_DIMENSIONS_PER_COIL);

	/**
	 * @return the name
	 */
	public String getName() {
		// TODO: evil hack:
		String trackName = (String) names.get(0);
		String[] nameParts = trackName.split("_");
		return nameParts[0];
	}

	/**
	 * @return the x
	 */
	public Track getX() {
		return tracks.get(EmaData.X);
	}

	/**
	 * @param x2
	 *            the x to set
	 */
	public void setX(Track x) {
		tracks.put(EmaData.X, x);
	}

	/**
	 * @return the y
	 */
	public Track getY() {
		return tracks.get(EmaData.Y);
	}

	/**
	 * @param y2
	 *            the y to set
	 */
	public void setY(Track y) {
		tracks.put(EmaData.Y, y);
	}

	/**
	 * @return the z
	 */
	public Track getZ() {
		return tracks.get(EmaData.Z);
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(Track z) {
		tracks.put(EmaData.Z, z);
	}

	/**
	 * @return the phi
	 */
	public Track getPhi() {
		return tracks.get(EmaData.PHI);
	}

	/**
	 * @param phi
	 *            the phi to set
	 */
	public void setPhi(Track phi) {
		tracks.put(EmaData.PHI, phi);
	}

	/**
	 * @return the theta
	 */
	public Track getTheta() {
		return tracks.get(EmaData.THETA);
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(Track theta) {
		tracks.put(EmaData.THETA, theta);
	}

	/**
	 * @return the rms
	 */
	public Track getRms() {
		return tracks.get(EmaData.RMS);
	}

	/**
	 * @param rms
	 *            the rms to set
	 */
	public void setRms(Track rms) {
		tracks.put(EmaData.RMS, rms);
	}

	/**
	 * @return the extra
	 */
	public Track getExtra() {
		return tracks.get(EmaData.EXTRA);
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(Track extra) {
		tracks.put(EmaData.EXTRA, extra);
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

	@Override
	public String toString() {
		String string = String.format("Name: %s\nData: %s", getName(), data);
		return string;
	}
	
}
