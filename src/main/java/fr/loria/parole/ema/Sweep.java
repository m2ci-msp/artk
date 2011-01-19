package fr.loria.parole.ema;

import java.io.IOException;
import java.util.Vector;

import cern.colt.matrix.tfloat.FloatMatrix2D;

import fr.loria.parola.ema.io.HeaderFileReader;
import fr.loria.parola.ema.io.PosFileReader;

public class Sweep {
	private Vector<Channel> channels;

	/**
	 * @return the channels
	 */
	public Vector<Channel> getChannels() {
		return channels;
	}

	/**
	 * @param channels
	 *            the channels to set
	 */
	public void setChannels(Vector<Channel> channels) {
		this.channels = channels;
	}

	public Channel getChannelByName(String name) {
		for (Channel ch : channels) {
			if (ch.getName().equals(name)) {
				return ch;
			}
		}
		return null;
	}

	public Sweep() {
		this.channels = new Vector<Channel>();
	}

	public void loadFromFiles(String headerFileName, String posFileName) throws IOException {
		HeaderFileReader headerFileReader = new HeaderFileReader(headerFileName);
		String[] trackNames = headerFileReader.getNames();
		int numTracks = trackNames.length;
		PosFileReader posFileReader = new PosFileReader(posFileName);
		FloatMatrix2D data = posFileReader.getSamples2D(numTracks);

		Track[] tracks = new Track[numTracks];
		for (int t = 0; t < numTracks; t++) {
			tracks[t] = new Track(trackNames[t], data.viewRow(t).toArray());
		}

		int t = 0;
		for (int c = 0; c < numTracks / 7; c++) { // we should have 7 tracks per
													// channel
			Channel ch = new Channel(tracks[t].getName());
			ch.setX(tracks[t]);
			ch.setY(tracks[t + 1]);
			ch.setZ(tracks[t + 2]);
			ch.setPhi(tracks[t + 3]);
			ch.setTheta(tracks[t + 4]);
			ch.setRms(tracks[t + 5]);
			ch.setExtra(tracks[t + 6]);
			channels.add(ch);
			t += 7;
		}

		return;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		Sweep s = new Sweep();
		s.loadFromFiles(args[0], args[1]);
		Channel foo = s.getChannelByName("Ch1_X");
//		Frame bar = foo.getFrame(5);
		int x = 0;
	}
}
