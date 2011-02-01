package fr.loria.parole.ema;

import java.io.IOException;
import org.apache.commons.lang.ArrayUtils;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tobject.ObjectMatrix1D;

import fr.loria.parola.ema.io.HeaderFileReader;
import fr.loria.parola.ema.io.PosFileReader;

public class Sweep extends EmaData {

	public Sweep(String headerFileName, String posFileName) throws IOException {
		loadFromFiles(headerFileName, posFileName);
	}

	protected Sweep(ObjectMatrix1D names, FloatMatrix2D data) {
		this.names = names;
		this.data = data;
	};

	private String getName(int index) {
		return (String) names.get(index);
	}

	private int[] getNameIndicesStartingWith(String prefix) {
		int[] selection = null;
		for (int n = 0; n < names.size(); n++) {
			String name = (String) names.get(n);
			if (name.startsWith(prefix)) {
				selection = ArrayUtils.add(selection, n);
			}
		}
		return selection;
	}

	public ObjectMatrix1D getNamesStartingWith(String prefix) {
		int[] indices = getNameIndicesStartingWith(prefix);
		ObjectMatrix1D selection = names.viewSelection(indices);
		return selection;
	}

	public FloatMatrix2D getTracksStartingWith(String prefix) {
		int[] indices = getNameIndicesStartingWith(prefix);
		FloatMatrix2D selection = data.viewSelection(indices, null);
		return selection;
	}

	public Track getTrack(int index) {
		Track track = new Track(getName(index), getRow(index));
		return track;
	}

	public Frame getFrame(int index) {
		FloatMatrix1D samples = getColumn(index);
		Frame frame = new Frame(index, names, samples);
		return frame;
	}

	public Channel getChannel(String name) {
		ObjectMatrix1D names = getNamesStartingWith(name);
		FloatMatrix2D data = getTracksStartingWith(name);
		Channel channel = new Channel(names, data);
		return channel;
	}

	public Channel[] getChannels() {
		// TODO hard-coding this is an evil hack!
		Channel[] channels = new Channel[12];
		for (int i = 0; i < channels.length; i++) {
			String channelName = String.format("Ch%d_", i + 1);
			Channel channel = getChannel(channelName);
			channels[i] = channel;
		}
		return channels;
	}

	private void loadFromFiles(String headerFileName, String posFileName) throws IOException {
		HeaderFileReader headerFileReader = new HeaderFileReader(headerFileName);
		names = headerFileReader.getNames();

		int numTracks = (int) names.size();

		PosFileReader posFileReader = new PosFileReader(posFileName);
		data = posFileReader.getSamples(numTracks);

		return;
	}

	public void translateData(float xOffset, float yOffset, float zOffset) {
		Channel[] channels = getChannels();
		for (Channel channel : channels) {
			channel.getX().addToSamples(xOffset);
			channel.getY().addToSamples(yOffset);
			channel.getZ().addToSamples(zOffset);
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		Sweep s = new Sweep(args[0], args[1]);
		Channel channel = s.getChannel("Ch1_");
		Frame frame = channel.getFrame(0);
		return;
	}

}
