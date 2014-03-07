package org.m2ci.msp.ema;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.m2ci.msp.ema.AG501PosFile;

import com.google.common.io.Resources;

public class AG501PosFileTest {

	private AG501PosFile posFile;

	private static File file;

	@BeforeClass
	public static void oneTimeSetup() throws URISyntaxException {
		URI resource = Resources.getResource("ag501.pos").toURI();
		file = new File(resource);
	}

	@Before
	public void setUp() throws IOException {
		posFile = new AG501PosFile(file);
	}

	@Test
	public void testNumberOfChannels() {
		assertThat(posFile.getNumberOfChannels()).isEqualTo(posFile.header.numChannels);
	}

	@Test
	public void testData() {
		int numFields = posFile.getNumberOfFieldsPerFrame();
		int dataCols = posFile.data.numCols();
		assertThat(dataCols).isEqualTo(numFields);
	}
}
