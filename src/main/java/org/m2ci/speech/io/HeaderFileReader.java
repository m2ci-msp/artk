package org.m2ci.speech.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import cern.colt.matrix.tobject.ObjectMatrix1D;
import cern.colt.matrix.tobject.impl.DenseObjectMatrix1D;

public class HeaderFileReader {
	private ObjectMatrix1D names;

	/**
	 * @return the names
	 */
	public ObjectMatrix1D getNames() {
		return names;
	}

	public HeaderFileReader(String fileName) throws IOException {
		this(new File(fileName));
	}

	public HeaderFileReader(File file) throws IOException {
		names = loadNames(file);
	}

	public HeaderFileReader(InputStream stream) throws IOException {
		names = loadNames(stream);
	}

	private ObjectMatrix1D loadNames(InputStream stream) throws IOException {
		String raw = IOUtils.toString(stream);
		return loadNames(raw);
	}

	private ObjectMatrix1D loadNames(File file) throws IOException {
		String raw = FileUtils.readFileToString(file);
		return loadNames(raw);
	}

	private ObjectMatrix1D loadNames(String string) throws IOException {
		String[] namesArray = StringUtils.split(string);
		ObjectMatrix1D names = new DenseObjectMatrix1D(namesArray);
		return names;
	}

}
