package org.m2ci.speech.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix1D;

public class PosFileReader extends DataFileReader {

	public PosFileReader(String dataFileName) throws IOException {
		super(dataFileName);
	}

	public PosFileReader(InputStream dataStream) throws IOException {
		super(dataStream);
	}

	public FloatMatrix1D getFlatSamples() {
		buffer.rewind();
		FloatBuffer floatBuffer = buffer.asFloatBuffer();
		int numFloats = floatBuffer.capacity();
		float[] samples = new float[numFloats];
		floatBuffer.get(samples);
		FloatMatrix1D matrix = new DenseFloatMatrix1D(samples);
		return matrix;
	}

	public FloatMatrix2D getSamples(int numberOfTracks) {
		FloatMatrix1D flatData = getFlatSamples();
		int numberOfSamples = (int) (flatData.size() / numberOfTracks);
		FloatMatrix2D data = flatData.reshape(numberOfTracks, numberOfSamples);
		return data;
	}

}
