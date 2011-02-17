package fr.loria.parole.ema;

import java.io.IOException;
import org.apache.commons.lang.ArrayUtils;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tobject.ObjectMatrix1D;

import fr.loria.parole.ema.io.HeaderFileReader;
import fr.loria.parole.ema.io.PosFileReader;

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
		// TODO this does not guarantee the implicit assumption that the names
		// will be in the order x, z, y, ...!
		int[] selection = null;
		for (int n = 0; n < names.size(); n++) {
			String name = (String) names.get(n);
			if (name.startsWith(prefix)) {
				selection = ArrayUtils.add(selection, n);
			}
		}
		return selection;
	}

	private int[] getNameIndicesEndingWith(String prefix) {
		// TODO this does not guarantee the implicit assumption that the names
		// will be in the order x, z, y, ...!
		int[] selection = null;
		for (int n = 0; n < names.size(); n++) {
			String name = (String) names.get(n);
			if (name.endsWith(prefix)) {
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

	public ObjectMatrix1D getNamesEndingWith(String prefix) {
		int[] indices = getNameIndicesEndingWith(prefix);
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

	public Track[] getTracks() {
		int numTracks = getNumberOfTracks();
		Track[] tracks = new Track[numTracks];
		for (int i = 0; i < numTracks; i++) {
			Track track = getTrack(i);
			tracks[i] = track;
		}
		return tracks;
	}

	public Channel[] getChannels() {
		ObjectMatrix1D xNames = getNamesEndingWith(X);
		Channel[] channels = new Channel[(int) xNames.size()];
		for (int i = 0; i < channels.length; i++) {
			// TODO ugly hack
			String channelName = ((String) xNames.get(i)).replace("_X", "");
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

	public void smoothData(int windowSize) {
		for (Track track : getTracks()) {
			track.smoothSamples(windowSize);
		}
		return;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		Sweep s = new Sweep(args[0], args[1]);
		Channel[] channels = s.getChannels();
		return;
	}

}
