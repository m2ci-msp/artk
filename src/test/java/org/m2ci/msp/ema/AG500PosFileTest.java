package org.m2ci.msp.ema;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.m2ci.msp.ema.AG500PosFile;

import com.google.common.io.Resources;

public class AG500PosFileTest {

	private static File file;

	private AG500PosFile posFile;

	@BeforeClass
	public static void oneTimeSetup() throws URISyntaxException {
		URI resource = Resources.getResource("ag500.pos").toURI();
		file = new File(resource);
	}

	@Before
	public void setUp() throws Exception {
		posFile = new AG500PosFile(file);
	}

	@Test
	public void testNumberOfFields() {
		assertThat(posFile.getNumberOfFieldsPerFrame()).isEqualTo(7 * 12);
	}

	@Test
	public void testData() {
		int numFields = posFile.getNumberOfFieldsPerFrame();
		int dataCols = posFile.data.numCols();
		assertThat(dataCols).isEqualTo(numFields);
		int dataRows = posFile.data.numRows();
		assertThat(dataRows).isEqualTo(10);
		double firstValue = posFile.data.get(0, 0);
		assertThat(firstValue).isEqualTo(1);
		double lastValue = posFile.data.get(9, 83);
		assertThat(lastValue).isEqualTo(840);
	}
}
