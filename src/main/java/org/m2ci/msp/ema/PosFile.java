package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public abstract class PosFile implements EmaFile {

	SimpleMatrix data;
	protected ArrayList<String> channelNames;

	protected void initChannelNames() {
		channelNames = Lists.newArrayListWithCapacity(getNumberOfChannels());
		for (int c = 1; c <= getNumberOfChannels(); c++) {
			channelNames.add("Ch_" + c);
		}
	}

	public PosFile withChannelNames(ArrayList<String> newChannelNames) {
		if (newChannelNames.size() != getNumberOfChannels()) {
			throw new IllegalArgumentException(String.format("Expected %d channel names, but got %d", getNumberOfChannels(),
					newChannelNames.size()));
		}
		Collections.copy(channelNames, newChannelNames);
		return this;
	}

	public static PosFile loadFrom(File file) throws IOException {
		String firstLine = Files.readFirstLine(file, Charsets.US_ASCII);
		if (firstLine.startsWith("AG50xDATA")) {
			return new AG501PosFile(file);
		} else {
			return new AG500PosFile(file);
		}
	}

}
