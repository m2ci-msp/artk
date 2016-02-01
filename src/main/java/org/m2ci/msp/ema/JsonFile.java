package org.m2ci.msp.ema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ejml.data.MatrixIterator;
import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class JsonFile extends TextFile {

	public JsonFile(SimpleMatrix data) {
		setData(data);
	}

	public int getNumberOfFrames() {
		return data.numRows();
	}

	// fluent setters

	public JsonFile withSamplingFrequency(double newSamplingFrequency) {
		setSamplingFrequency(newSamplingFrequency);
		return this;
	}

	public JsonFile withChannelNames(ArrayList<String> newChannelNames) {
		setChannelNames(newChannelNames);
		return this;
	}

	@Override
	public void setData(SimpleMatrix newData) {
		data = newData;
		numberOfChannels = data.numCols() / getNumberOfFieldsPerChannel();
	}

	public int getNumberOfFieldsPerChannel() {
		return 6;
	}

	// output

	@Override
	public void writeTo(File file) throws IOException {

		groovy.json.JsonBuilder builder = buildJson();
		BufferedWriter writer = Files.newWriter(file, Charsets.UTF_8);
		writer.append(builder.toPrettyString());
		writer.close();

	}

	public groovy.json.JsonBuilder buildJson() {

		groovy.json.JsonBuilder builder = new groovy.json.JsonBuilder();

		// data structure for storing the channels
		Map<String, Map<String, ArrayList<String>>> channels = new HashMap<String, Map<String, ArrayList<String>>>();

		for (String channelName : this.getChannelNames()) {

			// build map for a single channel
			ArrayList<String> positionData = new ArrayList<String>();
			ArrayList<String> angleData = new ArrayList<String>();

			this.getChannelData(channelName, positionData, angleData);

			Map<String, ArrayList<String>> channel = new HashMap<String, ArrayList<String>>();

			channel.put("position", positionData);
			channel.put("angle", angleData);

			channels.put(channelName, channel);

		}

		builder.call(channels);

		return builder;

	}

	private void getChannelData(String channelName, ArrayList<String> positionData, ArrayList<String> angleData) {

		String format = String.format("%%.%df", precision);

		// first get the index of the channel
		final int index = getChannelIndex(channelName);

		// compute column
		final int columnStart = index * getNumberOfFieldsPerChannel();

		// extract submatrix view of the wanted channel data
		MatrixIterator channelFields = data.iterator(
				// use row major ordering
				true,
				// start at first row
				0,
				// start at first column of channel
				columnStart,
				// use all time data
				data.numRows() - 1,
				// stop at last column of the channel
				columnStart + getNumberOfFieldsPerChannel() - 1);

		// channel data consists of 6 fields:
		// 3 for positional data and 3 for the Euler angles
		while (channelFields.hasNext()) {

			// read positional data
			for (int i = 0; i < 3; ++i) {
				positionData.add(String.format(format, channelFields.next()));
			}

			// read angle data
			for (int i = 0; i < 3; ++i) {
				angleData.add(String.format(format, channelFields.next()));
			}

		} // end while

		return;

	}

}
