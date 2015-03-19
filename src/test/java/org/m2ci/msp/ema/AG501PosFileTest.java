package org.m2ci.msp.ema;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.EjmlUnitTests;
import org.ejml.ops.MatrixIO;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.m2ci.msp.ema.AG501PosFile;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class AG501PosFileTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private static File file;

	private AG501PosFile posFile;

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
	public void testNumberOfFields() {
		assertThat(posFile.getNumberOfFieldsPerFrame()).isEqualTo(7 * posFile.getNumberOfChannels());
	}

	@Test
	public void testFrameFieldNames() throws URISyntaxException, IOException {
		ArrayList<String> frameFieldNames = posFile.getFrameFieldNames();
		File txtFile = new File(Resources.getResource("ag501.txt").toURI());
		String headerLine = Files.readFirstLine(txtFile, Charsets.US_ASCII);
		ArrayList<String> headerFields = Lists.newArrayList(headerLine.split("\t"));
		assertThat(frameFieldNames).isEqualTo(headerFields);
	}

	@Test
	public void testExtractChannel() throws URISyntaxException, IOException {
		URI resource = Resources.getResource("ag501ch03.csv").toURI();
		DenseMatrix64F channel3 = MatrixIO.loadCSV(resource.getPath());
		DenseMatrix64F extractedChannel = posFile.extractChannel(2).data.getMatrix();
		EjmlUnitTests.assertEquals(extractedChannel, channel3, 0.01);
	}

	@Test
	public void testData() {
		int dataCols = posFile.data.numCols();
		assertThat(dataCols).isEqualTo(112);
		int dataRows = posFile.data.numRows();
		assertThat(dataRows).isEqualTo(10);
		double firstValue = posFile.data.get(0, 0);
		assertThat(firstValue).isEqualTo(1);
		double lastValue = posFile.data.get(9, 111);
		assertThat(lastValue).isEqualTo(1120);
	}

	@Test
	public void testSaveTxt() throws IOException, URISyntaxException {
		File tmpFile = tempFolder.newFile();
		posFile.asText().withPrecision(2).writeTo(tmpFile);
		URI resource = Resources.getResource("ag501.txt").toURI();
		File txtFile = new File(resource);
		assertThat(tmpFile).hasContentEqualTo(txtFile);
	}

	@Test
	public void testSaveBvh() throws IOException, URISyntaxException {
		File tmpFile = tempFolder.newFile();
		posFile.asBvh().withPrecision(1).writeTo(tmpFile);
		URI resource = Resources.getResource("ag501.bvh").toURI();
		File bvhFile = new File(resource);
		assertThat(tmpFile).hasContentEqualTo(bvhFile);
	}

	@Test
	public void testTimeExtraction() {
		double xmin = 0.004;
		double xmax = 0.02;
		AG501PosFile segment = (AG501PosFile) posFile.extractTimeRange(xmin, xmax);
		assertThat(segment.getFirstSampleTime()).isGreaterThanOrEqualTo(xmin);
		assertThat(segment.getLastSampleTime()).isLessThanOrEqualTo(xmax);
	}
	
	@Test
	public void smoothTest() throws IOException, URISyntaxException {
		posFile.withSmoothedChannels(1);
		
		// FIXME: write a meaningful test
		
	}
}
