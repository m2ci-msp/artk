package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class AG500PosFile extends PosFile {

	protected AG500PosFile() {
	}

	public AG500PosFile(File file) throws IOException {
		numberOfChannels = 12;
		data = read(file);
		initChannelNames();
	}

	protected SimpleMatrix read(File file) throws IOException {
		// read little-endian file to float array
		MappedByteBuffer byteBuffer = Files.map(file);
		byteBuffer.position(getHeaderSize());
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		float[] floats = new float[floatBuffer.remaining()];
		floatBuffer.get(floats);
		// convert float array to double array
		double[] doubles = new double[floats.length];
		for (int f = 0; f < floats.length; f++) {
			doubles[f] = floats[f];
		}
		// create data matrix
		int numCols = getNumberOfFieldsPerFrame();
		int numRows = doubles.length / numCols;
		assert numCols * numRows == doubles.length;
		DenseMatrix64F data = DenseMatrix64F.wrap(numRows, numCols, doubles);
		return SimpleMatrix.wrap(data);
	}

	public AG500PosFile withChannelNames(ArrayList<String> newChannelNames) {
		setChannelNames(newChannelNames);
		return this;
	}

	// parameters

	public int getSamplingFrequency() {
		return 200;
	}

	// data

	public int getNumberOfFieldsPerChannel() {
		return Fields.values().length;
	}

	public int getNumberOfFieldsPerFrame() {
		int numChannels = getNumberOfChannels();
		int fieldsPerChannel = getNumberOfFieldsPerChannel();
		int fieldsPerFrame = numChannels * fieldsPerChannel;
		return fieldsPerFrame;
	}

	public ArrayList<String> getFieldNames() {
		ArrayList<String> names = Lists.newArrayListWithCapacity(getNumberOfFieldsPerChannel());
		for (Fields field : Fields.values()) {
			names.add(field.toString());
		}
		return names;
	}

	public ArrayList<String> getFrameFieldNames() {
		ArrayList<String> names = Lists.newArrayListWithCapacity(getNumberOfFieldsPerFrame());
		for (String channel : getChannelNames()) {
			for (String field : getFieldNames()) {
				String name = String.format("%s_%s", channel, field);
				names.add(name);
			}
		}
		return names;
	}

	public AG500PosFile withData(SimpleMatrix newData) {
		setData(newData);
		return this;
	}

	public AG500PosFile extractChannel(String channelName) {
		int channelIndex = getChannelIndex(channelName);
		return extractChannel(channelIndex);
	}

	public AG500PosFile extractChannel(int channelIndex) {
		int firstColumn = channelIndex * getNumberOfFieldsPerChannel();
		int lastColumn = firstColumn + getNumberOfFieldsPerChannel();
		SimpleMatrix extractedData = data.extractMatrix(0, SimpleMatrix.END, firstColumn, lastColumn);
		ArrayList<String> extractedNames = Lists.newArrayList(channelNames.subList(channelIndex, channelIndex + 1));
		return new AG500PosFile().withData(extractedData).withChannelNames(extractedNames);
	}

	public ArrayList<AG500PosFile> extractChannels(ArrayList<String> channelNames) {
		ArrayList<AG500PosFile> posFiles = Lists.newArrayListWithCapacity(channelNames.size());
		for (String channelName : channelNames) {
			AG500PosFile posFile = extractChannel(channelName);
			posFiles.add(posFile);
		}
		return posFiles;
	}

	public ArrayList<AG500PosFile> extractAllChannels() {
		return extractChannels(channelNames);
	}

	// fluent converters

	public TextFile asText() {
		TextFile txt = new TextFile(data).withChannelNames(getFrameFieldNames());
		return txt;
	}

	public BvhFile asBvh() {
		int numRows = getNumberOfFrames();
		int numCols = getNumberOfChannels() * BvhFile.getNumberOfFieldsPerChannel();
		SimpleMatrix bvhData = new SimpleMatrix(numRows, numCols);
		for (int channel = 0; channel < getNumberOfChannels(); channel++) {
			int sourceCol = channel * getNumberOfFieldsPerChannel();
			SimpleMatrix channelData = this.data.extractMatrix(0, SimpleMatrix.END, sourceCol, sourceCol + 5);
			int targetCol = channel * BvhFile.getNumberOfFieldsPerChannel();
			bvhData.insertIntoThis(0, targetCol, channelData);
		}
		BvhFile bvh = new BvhFile(bvhData).withSamplingFrequency(getSamplingFrequency()).withChannelNames(channelNames);
		return bvh;
	}

	protected enum Fields {
		X, Y, Z, PHI {
			public String toString() {
				return "phi";
			}
		},
		THETA {
			public String toString() {
				return "theta";
			}
		},
		RMS, EXTRA {
			public String toString() {
				return "Extra";
			}
		}
	}
}
