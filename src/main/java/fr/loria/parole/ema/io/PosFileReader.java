package fr.loria.parole.ema.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix1D;

public class PosFileReader {
	private ByteBuffer buffer;

	public PosFileReader(String fileName) throws IOException {
		this(new File(fileName));
	}

	public PosFileReader(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		FileChannel channel = stream.getChannel();
		long size = channel.size();
		buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
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
