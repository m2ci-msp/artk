package fr.loria.parola.ema.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

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

	public float[] getFlatData() {
		buffer.rewind();
		float[] data = new float[buffer.limit() / (Float.SIZE * Byte.SIZE)];
		for (int i = 0; i < data.length; i++) {
			data[i] = buffer.getFloat();
		}
		return data;
	}

	public float[][] getData(int ntracks) {
		float[] flatData = getFlatData();
		assert flatData.length % ntracks == 0;
		int nsamples = flatData.length / ntracks;
		float[][] data = new float[ntracks][];
		int d = 0;
		for (int s = 0; s < nsamples; s++) {
			for (int t = 0; t < ntracks; t++) {
				if (s == 0) {
					data[t] = new float[nsamples];
				}
				data[t][s] = flatData[d];
				d++;
			}
		}
		return data;
	}
	
}
