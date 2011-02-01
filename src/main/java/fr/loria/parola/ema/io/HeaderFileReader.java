package fr.loria.parola.ema.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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
	
	private ObjectMatrix1D loadNames(File file) throws IOException {
		String raw = FileUtils.readFileToString(file);
		String[] namesArray = StringUtils.split(raw);		
		ObjectMatrix1D names = new DenseObjectMatrix1D(namesArray);
		return names;
	}

}
