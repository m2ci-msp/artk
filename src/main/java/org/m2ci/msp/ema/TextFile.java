package org.m2ci.msp.ema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import org.ejml.data.MatrixIterator;
import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class TextFile extends EmaFile {

	protected int precision = 6;

	protected TextFile() {
	}

	public TextFile(SimpleMatrix newData) {
		data = newData;
	}

	public int getNumberOfChannels() {
		return data.numCols();
	}

	public ArrayList<String> getChannelNames() {
		return channelNames;
	}

	public TextFile withChannelNames(ArrayList<String> newChannelNames) {
		setChannelNames(newChannelNames);
		return this;
	}

	public void setPrecision(int newPrecision) {
		precision = newPrecision;
	}

	public TextFile withPrecision(int newPrecision) {
		setPrecision(newPrecision);
		return this;
	}

	protected void writeHeader(Writer writer) throws IOException {
		Joiner.on("\t").appendTo(writer, getChannelNames()).append("\n");
	}

	protected void writeData(Writer writer) throws IOException {
		String format = String.format("%%.%df", precision);
		for (int row = 0; row < data.numRows(); row++) {
			ArrayList<String> formattedFields = Lists.newArrayListWithCapacity(data.numRows());
			int numCols = data.numCols() - 1;
			MatrixIterator fields = data.iterator(true, row, 0, row, numCols);
			while (fields.hasNext()) {
				formattedFields.add(String.format(format, fields.next()));
			}
			Joiner.on("\t").appendTo(writer, formattedFields).append("\n");
		}
	}

	public void writeTo(File file) throws IOException {
		BufferedWriter writer = Files.newWriter(file, Charsets.UTF_8);
		writeHeader(writer);
		writeData(writer);
		writer.close();
	}

	public void setData(SimpleMatrix newData) {
		data = newData;
		numberOfChannels = data.numCols();
	}
}
