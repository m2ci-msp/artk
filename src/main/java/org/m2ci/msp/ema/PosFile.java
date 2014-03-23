package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public abstract class PosFile implements EmaFile {

	SimpleMatrix data;
	protected int numberOfChannels;
	protected ArrayList<String> channelNames;

	// channels and channel names

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	protected void initChannelNames() {
		channelNames = Lists.newArrayListWithCapacity(getNumberOfChannels());
		for (int c = 1; c <= getNumberOfChannels(); c++) {
			channelNames.add("Ch" + c);
		}
	}

	public ArrayList<String> getChannelNames() {
		return channelNames;
	}

	public void setChannelNames(Collection<String> newChannelNames) {
		if (newChannelNames.size() != getNumberOfChannels()) {
			throw new IllegalArgumentException(String.format("Expected %d channel names, but got %d", getNumberOfChannels(),
					newChannelNames.size()));
		}
		channelNames = Lists.newArrayList(newChannelNames);
	}

	abstract public PosFile withChannelNames(Collection<String> newChannelNames);

	protected int getChannelIndex(String channelName) {
		int channelIndex = getChannelNames().indexOf(channelName);
		if (channelIndex < 0) {
			throw new IllegalArgumentException(String.format("No channel named %s can be found.", channelName));
		}
		return channelIndex;
	}

	public int getNumberOfFrames() {
		return data.numRows();
	}

	abstract public int getNumberOfFieldsPerChannel();

	abstract public int getNumberOfFieldsPerFrame();

	public int getHeaderSize() {
		return 0;
	}

	public static PosFile loadFrom(File file) throws IOException {
		String firstLine = Files.readFirstLine(file, Charsets.US_ASCII);
		if (firstLine.startsWith("AG50xDATA")) {
			return new AG501PosFile(file);
		} else {
			return new AG500PosFile(file);
		}
	}

	protected void setData(SimpleMatrix newData) {
		data = newData;
		numberOfChannels = data.numCols() / getNumberOfFieldsPerChannel();
	}

}
