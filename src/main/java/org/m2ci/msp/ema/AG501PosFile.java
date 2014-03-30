package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;

import com.google.common.collect.Lists;

public class AG501PosFile extends AG500PosFile {

	AG501PosFileHeader header;

	protected AG501PosFile() {
	}

	public AG501PosFile(File file) throws IOException {
		header = new AG501PosFileHeader(file);
		numberOfChannels = header.getNumberOfChannels();
		setData(read(file));
		initChannelNames();
	}

	@Override
	public double getSamplingFrequency() {
		return header.getSamplingFrequency();
	}

	private AG501PosFileHeader getHeader() {
		return header;
	}

	private void setHeader(AG501PosFileHeader newHeader) {
		header = newHeader;
	}

	private AG501PosFile withHeader(AG501PosFileHeader newHeader) {
		setHeader(newHeader);
		return this;
	}

	@Override
	public int getHeaderSize() {
		int size = header.getSize() + 2;
		return size;
	}

	@Override
	public AG501PosFile withData(SimpleMatrix data) {
		setData(data);
		return this;
	}

	@Override
	public AG501PosFile withTimeOffset(double offset) {
		setTimeOffset(offset);
		return this;
	}

	@Override
	public AG501PosFile withChannelNames(ArrayList<String> newChannelNames) {
		setChannelNames(newChannelNames);
		return this;
	}

	@Override
	public AG501PosFile extractChannel(int channelIndex) {
		return new AG501PosFile().withHeader(getHeader()).withData(extractChannelData(channelIndex))
				.withChannelNames(Lists.newArrayList(channelNames.get(channelIndex)));
	}

	@Override
	protected AG501PosFile extractFrameRange(int firstFrame, int lastFrame) {
		int firstCol = 0;
		int lastCol = data.numCols();
		SimpleMatrix extractMatrix = data.extractMatrix(firstFrame, lastFrame, firstCol, lastCol);
		double offset = times.get(firstFrame) - 0.5 / getSamplingFrequency();
		AG501PosFile extraction = new AG501PosFile().withHeader(getHeader()).withData(extractMatrix).withTimeOffset(offset)
				.withChannelNames(getChannelNames());
		return extraction;
	}
}
