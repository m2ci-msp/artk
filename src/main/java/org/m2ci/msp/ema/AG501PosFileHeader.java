package org.m2ci.msp.ema;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

public class AG501PosFileHeader {

	final int NUM_LINES = 4;
	final Pattern LINE_1 = Pattern.compile("AG50xDATA_V002");
	final Pattern LINE_2 = Pattern.compile("00000070");
	final Pattern LINE_3 = Pattern.compile("NumberOfChannels=(\\d+)");
	final Pattern LINE_4 = Pattern.compile("SamplingFrequencyHz=(\\d+)");
	private Matcher matcher;
	ArrayList<String> lines = Lists.newArrayList();

	int numChannels;
	int samplingFrequency;

	public AG501PosFileHeader(File file) throws IOException {
		CharSource source = Files.asCharSource(file, Charsets.UTF_8);
		BufferedReader stream = source.openBufferedStream();
		while (lines.size() < NUM_LINES) {
			String line = stream.readLine();
			lines.add(line);
		}

		// set number of channels
		matcher = LINE_3.matcher(lines.get(2));
		matcher.find();
		numChannels = Integer.parseInt(matcher.group(1));

		// set sampling frequency
		matcher = LINE_4.matcher(lines.get(3));
		matcher.find();
		samplingFrequency = Integer.parseInt(matcher.group(1));
	}

	public int getNumberOfChannels() {
		return numChannels;
	}

	public int getSamplingFrequency() {
		return samplingFrequency;
	}

	public int getSize() {
		int size = 0;
		for (String line : lines) {
			size += (line + "\n").length();
		}
		return size;
	}

	public String toString() {
		return String.format("AG50xDATA_V002\n00000070\nNumberOfChannels=%d\nSamplingFrequencyHz=%d\n\u0000\u001A",
				getNumberOfChannels(), getSamplingFrequency());
	}
}
