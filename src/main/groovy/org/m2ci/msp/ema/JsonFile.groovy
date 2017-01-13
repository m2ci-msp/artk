package org.m2ci.msp.ema;

import org.ejml.data.MatrixIterator;
import org.ejml.simple.SimpleMatrix;

public class JsonFile extends TextFile {

        // TODO: output normals

	// FIXME: Using the data field of the superclass
	// results in a stackoverflow exception
	SimpleMatrix data

	JsonFile(SimpleMatrix data) {
		setData(data);
	}

	int getNumberOfFrames() {
		return data.numRows();
	}

	// fluent setters

	@Override
	JsonFile withSamplingFrequency(double newSamplingFrequency) {
		setSamplingFrequency(newSamplingFrequency);
		return this;
	}

	@Override
	JsonFile withChannelNames(ArrayList<String> newChannelNames) {
		setChannelNames(newChannelNames);
		return this;
	}

	@Override
	public void setData(SimpleMatrix newData) {
		this.data = newData;
		this.numberOfChannels = data.numCols() / getNumberOfFieldsPerChannel();
	}

	static int getNumberOfFieldsPerChannel() {
		return 6;
	}

	// output

	// FIXME: Output does not consider provided precision setting
	@Override
	public void writeTo(File file) {

		def builder = buildJson();
		file.withWriter{ output ->
			output << builder.toPrettyString()
		}

	}

	def buildJson() {

		// map storing the channels
		def channels = [:]

		this.channelNames.each{ channelName ->

			// build map for a single channel
			def positionData = getPositionData(channelName)
			def angleData = getAngleData(channelName)

			def channel = [:]

			channel.position = positionData
			channel.eulerAngles = angleData

			channels."$channelName" = channel

		}

		// create main map
		def json = [:]
		json.channels = channels

		if(writeTimes == true) {
			json.timestamps = getTimes()
		}

		json.samplingFrequency = this.samplingFrequency

		def builder = new groovy.json.JsonBuilder(json)

		return builder;

	}

	private def getPositionData(channelName) {

                // positional data is at offset 0 and contains 3 entries
                return extractData(channelName, 0, 3)

	}

	private def getAngleData(channelName) {

                // angle data is at offset 3 and contains 3 entries
                return extractData(channelName, 3, 3)

	}

        private def extractData(channelName, dataOffset, dataSize) {

		// first get the index of the channel
		def index = getChannelIndex(channelName);

		// compute start column of the data to be extracted
		def columnStart = index * getNumberOfFieldsPerChannel() + dataOffset;

		// extract submatrix view iterator of the wanted channel data
		def channelFields = data.iterator(
				// use row major ordering
				true,
				// start at first row
				0,
				// start at first column of channel
				columnStart,
				// use all time data
				data.numRows() - 1,
				// stop at last column of the data in the channel
				columnStart + dataSize - 1);

		// extract data
		def data = []

		while (channelFields.hasNext()) {
			data.add(channelFields.next());
		}

		return data;

	}


}
