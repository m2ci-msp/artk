package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;

import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public abstract class PosFile extends EmaFile {

	// channels and channel names

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

	public static PosFile loadFrom(String path) throws IOException {
		File file = new File(path);
		return loadFrom(file);
	}

	abstract public int getNumberOfFieldsPerChannel();

	public void setData(SimpleMatrix newData) {
		data = newData;
		numberOfChannels = data.numCols() / getNumberOfFieldsPerChannel();
		updateTimes();
	}

	@Override
	public void setSamplingFrequency(double newSamplingFrequency) {
		throw new UnsupportedOperationException("The sampling frequency cannot be changed");
	}
}
