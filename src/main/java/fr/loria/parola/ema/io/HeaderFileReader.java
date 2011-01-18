package fr.loria.parola.ema.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class HeaderFileReader {
	private String[] names;

	/**
	 * @return the names
	 */
	public String[] getNames() {
		return names;
	}

	public HeaderFileReader(String fileName) throws IOException {
		this(new File(fileName));
	}

	public HeaderFileReader(File file) throws IOException {
		String raw = FileUtils.readFileToString(file);
		names = StringUtils.split(raw);
	}

}
