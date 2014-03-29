package org.m2ci.msp.ema;

import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;

import com.google.common.collect.Lists;

public abstract class EmaFile {

	SimpleMatrix data;
	protected int numberOfChannels;
	protected ArrayList<String> channelNames;

	// channels

	protected void initChannelNames() {
		channelNames = Lists.newArrayListWithCapacity(getNumberOfChannels());
		for (int c = 1; c <= getNumberOfChannels(); c++) {
			channelNames.add("Ch" + c);
		}
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public ArrayList<String> getChannelNames() {
		return channelNames;
	}

	protected int getChannelIndex(String channelName) {
		int channelIndex = getChannelNames().indexOf(channelName);
		if (channelIndex < 0) {
			throw new IllegalArgumentException(String.format("No channel named %s can be found.", channelName));
		}
		return channelIndex;
	}

	public void setChannelNames(ArrayList<String> newChannelNames) {
		if (newChannelNames.size() != getNumberOfChannels()) {
			throw new IllegalArgumentException(String.format("Expected %d channel names, but got %d", getNumberOfChannels(),
					newChannelNames.size()));
		}
		channelNames = Lists.newArrayList(newChannelNames);
	}

	public int getNumberOfFrames() {
		return data.numRows();
	}

	abstract public void setData(SimpleMatrix newData);
}
