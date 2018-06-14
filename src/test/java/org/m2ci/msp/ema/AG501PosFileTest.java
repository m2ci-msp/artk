package org.m2ci.msp.ema;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.ejml.EjmlUnitTests;
import org.ejml.data.DMatrixRMaj;
import org.ejml.ops.MatrixIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class AG501PosFileTest {

    private static File file;

    private AG501PosFile posFile;

    @BeforeAll
    public static void oneTimeSetup() throws URISyntaxException {
        URI resource = Resources.getResource("ag501.pos").toURI();
        file = new File(resource);
    }

    @BeforeEach
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
        String headerLine = Files.asCharSource(txtFile, Charsets.US_ASCII).readFirstLine();
        ArrayList<String> headerFields = Lists.newArrayList(headerLine.split("\t"));
        assertThat(frameFieldNames).isEqualTo(headerFields);
    }

    @Test
    public void testExtractChannel() throws URISyntaxException, IOException {
        URI resource = Resources.getResource("ag501ch03.csv").toURI();
        DMatrixRMaj channel3 = MatrixIO.loadCSV(resource.getPath(), true);
        DMatrixRMaj extractedChannel = posFile.extractChannel(2).data.getMatrix();
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
        File tmpFile = File.createTempFile("expected", ".txt");
        posFile.asText().withPrecision(2).writeTo(tmpFile);
        URI resource = Resources.getResource("ag501.txt").toURI();
        File txtFile = new File(resource);
        assertThat(tmpFile).hasSameContentAs(txtFile);
    }

    @Test
    public void testSaveBvh() throws IOException, URISyntaxException {
        File tmpFile = File.createTempFile("expected", ".bvh");
        posFile.asBvh().withPrecision(1).writeTo(tmpFile);
        URI resource = Resources.getResource("ag501.bvh").toURI();
        File bvhFile = new File(resource);
        assertThat(tmpFile).hasSameContentAs(bvhFile);
    }

    @Test
    public void testSaveJson() throws IOException, URISyntaxException {
        File tmpFile = File.createTempFile("expected", ".json");
        posFile.asJson().withTimes().writeTo(tmpFile);
        URI resource = Resources.getResource("ag501.json").toURI();
        File jsonFile = new File(resource);
        assertThat(tmpFile).hasSameContentAs(jsonFile);
    }

    @Test
    public void testSavePos() throws IOException, URISyntaxException {
        File tmpFile = File.createTempFile("expected", ".pos");
        posFile.writeTo(tmpFile);
        URI resource = Resources.getResource("ag501.pos").toURI();
        File posFile = new File(resource);
        byte[] expected = Files.asByteSource(posFile).read();
        byte[] actual = Files.asByteSource(tmpFile).read();
        assertThat(actual).isEqualTo(expected);
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
