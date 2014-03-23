package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;

public class AG501PosFile extends AG500PosFile {

	AG501PosFileHeader header;

	public AG501PosFile(File file) throws IOException {
		header = new AG501PosFileHeader(file);
		numberOfChannels = header.getNumberOfChannels();
		data = read(file);
		initChannelNames();
	}

	@Override
	public int getSamplingFrequency() {
		return header.getSamplingFrequency();
	}

	@Override
	public int getHeaderSize() {
		int size = header.getSize() + 2;
		return size;
	}
}
