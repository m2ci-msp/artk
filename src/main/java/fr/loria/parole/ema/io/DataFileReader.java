package fr.loria.parole.ema.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import org.apache.commons.io.IOUtils;

public class DataFileReader {
	protected ByteBuffer buffer;

	public DataFileReader(String fileName) throws IOException {
		this(new File(fileName));
	}

	public DataFileReader(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		FileChannel channel = stream.getChannel();
		long size = channel.size();
		buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	public DataFileReader(InputStream stream) throws IOException {
		byte[] byteArray = IOUtils.toByteArray(stream);
		buffer = ByteBuffer.wrap(byteArray);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

}
