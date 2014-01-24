package org.m2ci.speech;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.m2ci.speech.io.HeaderFileReader;
import org.m2ci.speech.io.PosFileReader;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tobject.ObjectMatrix1D;

public class Sweep extends EmaData {

	public Sweep(String headerFileName, String posFileName) throws IOException {
		loadFromFiles(headerFileName, posFileName);
	}

	public Sweep(URL headerFileUrl, URL posFileUrl) throws IOException, URISyntaxException {
		loadFromFiles(headerFileUrl, posFileUrl);
	}

	public Sweep(InputStream headerFileStream, InputStream posFileStream) throws IOException {
		loadFromStreams(headerFileStream, posFileStream);
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
		Frame frame = new Frame(names, samples);
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

	/**
	 * Warning, this is read-only!
	 * 
	 * @return
	 */
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

	public Frame[] getFrames() {
		Frame[] frames = new Frame[this.getNumberOfFrames()];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = this.getFrame(i);
		}
		return frames;
	}

	public Iterator<Frame> getFrameIterator() {
		List<Frame> frameList = Arrays.asList(this.getFrames());
		return frameList.iterator();
	}

	private void loadFromFiles(URL headerFileUrl, URL posFileUrl) throws IOException, URISyntaxException {
		String headerFilePath = headerFileUrl.toURI().getPath();
		String posFilePath = posFileUrl.toURI().getPath();
		loadFromFiles(headerFilePath, posFilePath);
	}

	private void loadFromFiles(String headerFileName, String posFileName) throws IOException {
		HeaderFileReader headerFileReader = new HeaderFileReader(headerFileName);
		PosFileReader posFileReader = new PosFileReader(posFileName);
		loadFromReaders(headerFileReader, posFileReader);
	}

	private void loadFromStreams(InputStream headerFileStream, InputStream posFileStream) throws IOException {
		HeaderFileReader headerFileReader = new HeaderFileReader(headerFileStream);
		PosFileReader posFileReader = new PosFileReader(posFileStream);
		loadFromReaders(headerFileReader, posFileReader);
	}

	private void loadFromReaders(HeaderFileReader headerFileReader, PosFileReader posFileReader) {
		names = headerFileReader.getNames();
		int numTracks = (int) names.size();
		data = posFileReader.getSamples(numTracks);
	}

	public void translateData(float xOffset, float yOffset, float zOffset) {
		Channel[] channels = getChannels();
		for (Channel channel : channels) {
			channel.getX().addToSamples(xOffset);
			channel.getY().addToSamples(yOffset);
			channel.getZ().addToSamples(zOffset);
		}
	}

	/**
	 * 
	 * @param angleRadians
	 *            angle in radians
	 */
	public void rotateDataRadians(float angleRadians) {
		float sinTheta = (float) Math.sin(angleRadians);
		float cosTheta = (float) Math.cos(angleRadians);

		Channel[] channels = getChannels();
		for (Channel channel : channels) {
			for (int f = 0; f < channel.getNumberOfFrames(); f++) {
				Frame frame = channel.getFrame(f);
				float xPrime = frame.getX() * cosTheta - frame.getY() * sinTheta;
				float yPrime = frame.getX() * sinTheta + frame.getY() * cosTheta;
				frame.setX(xPrime);
				frame.setY(yPrime);

				frame.setPhi((float) Math.toDegrees(angleRadians));
			}
		}
		return;
	}

	public void smoothData(int windowSize) {
		for (Track track : getTracks()) {
			track.smoothSamples(windowSize);
		}
		return;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		InputStream args0 = new BufferedInputStream(new FileInputStream(args[0]));
		InputStream args1 = new BufferedInputStream(new FileInputStream(args[1]));
		Sweep s = new Sweep(args0, args1);
		return;
	}

}
