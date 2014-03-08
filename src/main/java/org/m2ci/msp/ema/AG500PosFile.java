package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.BufferUnderflowException;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class AG500PosFile implements PosFile {

	SimpleMatrix data;

	protected AG500PosFile() {
	}

	public AG500PosFile(File file) throws IOException {
		data = read(file);
	}

	protected SimpleMatrix read(File file) throws IOException {
		MappedByteBuffer byteBuffer = Files.map(file);
		byteBuffer.position(getHeaderSize());
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		double[] fields = new double[getNumberOfFieldsPerFrame()];
		SimpleMatrix matrix = null;
		while (true) {
			try {
				for (int f = 0; f < fields.length; f++) {
					fields[f] = floatBuffer.get();
				}
				DenseMatrix64F frame = DenseMatrix64F.wrap(1, fields.length, fields);
				if (matrix == null) {
					matrix = new SimpleMatrix(frame);
				} else {
					matrix = matrix.combine(SimpleMatrix.END, 0, SimpleMatrix.wrap(frame));
				}
			} catch (BufferUnderflowException e) {
				break;
			}
		}
		return matrix;
	}

	public int getNumberOfChannels() {
		return 12;
	}

	public int getNumberOfFieldsPerChannel() {
		return Fields.values().length;
	}

	public int getSamplingFrequency() {
		return 200;
	}

	public int getNumberOfFieldsPerFrame() {
		int numChannels = getNumberOfChannels();
		int fieldsPerChannel = getNumberOfFieldsPerChannel();
		int fieldsPerFrame = numChannels * fieldsPerChannel;
		return fieldsPerFrame;
	}

	public int getNumberOfFrames() {
		return data.numRows();
	}

	public int getHeaderSize() {
		return 0;
	}

	public void saveTxt(File file) throws IOException {
		Writer writer = Files.asCharSink(file, Charsets.UTF_8).openBufferedStream();
		ArrayList<String> fields = Lists.newArrayListWithCapacity(getNumberOfFieldsPerFrame());
		for (int c = 0; c < getNumberOfChannels(); c++) {
			for (int f = 0; f < getNumberOfFieldsPerChannel(); f++) {
				fields.add(String.format("Ch%d_%s", c + 1, Fields.values()[f]));
			}
		}
		Joiner.on("\t").appendTo(writer, fields).append("\n");
		for (int row = 0; row < getNumberOfFrames(); row++) {
			fields.clear();
			for (int col = 0; col < getNumberOfFieldsPerFrame(); col++) {
				double value = data.get(row, col);
				fields.add(String.format("%.2f", value));
			}
			Joiner.on("\t").appendTo(writer, fields).append("\n");
		}
		writer.close();
	}

	public enum Fields {
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
