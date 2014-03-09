package org.m2ci.msp.ema;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.m2ci.msp.ema.AG500PosFile;

import com.google.common.io.Resources;

public class AG500PosFileTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private static File file;

	private AG500PosFile posFile;

	@BeforeClass
	public static void oneTimeSetup() throws URISyntaxException {
		URI resource = Resources.getResource("ag500.pos").toURI();
		file = new File(resource);
	}

	@Before
	public void setUp() throws IOException {
		posFile = new AG500PosFile(file);
	}

	@Test
	public void testNumberOfChannels() {
		assertThat(posFile.getNumberOfChannels()).isEqualTo(12);
	}

	@Test
	public void testNumberOfFields() {
		assertThat(posFile.getNumberOfFieldsPerFrame()).isEqualTo(7 * 12);
	}

	@Test
	public void testData() {
		int dataCols = posFile.data.numCols();
		assertThat(dataCols).isEqualTo(84);
		int dataRows = posFile.data.numRows();
		assertThat(dataRows).isEqualTo(10);
		double firstValue = posFile.data.get(0, 0);
		assertThat(firstValue).isEqualTo(1);
		double lastValue = posFile.data.get(9, 83);
		assertThat(lastValue).isEqualTo(840);
	}

	@Test
	public void testSaveTxt() throws IOException, URISyntaxException {
		File tmpFile = tempFolder.newFile();
		posFile.saveTxt(tmpFile);
		URI resource = Resources.getResource("ag500.txt").toURI();
		File txtFile = new File(resource);
		assertThat(tmpFile).hasContentEqualTo(txtFile);
	}

	@Test
	public void testSaveBvh() throws IOException, URISyntaxException {
		File tmpFile = tempFolder.newFile();
		posFile.asBvh().writeTo(tmpFile);
		URI resource = Resources.getResource("ag500.bvh").toURI();
		File bvhFile = new File(resource);
		assertThat(tmpFile).hasContentEqualTo(bvhFile);
	}
}
