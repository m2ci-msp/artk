package org.m2ci.msp.ema;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.io.Resources;

public class AG501PosFileHeaderTest {

    private AG501PosFileHeader header;

    @BeforeEach
    public void setUp() throws IOException, URISyntaxException {
        URI resource = Resources.getResource("ag501.pos").toURI();
        File file = new File(resource);
        header = new AG501PosFileHeader(file);
    }

    @Test
    public void headerFormatTest() {
        assertThat(header.lines.get(0)).matches(header.LINE_1);
        assertThat(header.lines.get(1)).matches(header.LINE_2);
        assertThat(header.lines.get(2)).describedAs("Channels").matches(header.LINE_3);
        assertThat(header.lines.get(3)).describedAs("Frequency").matches(header.LINE_4);
    }

    @Test
    public void numChannelsTest() {
        int channels = header.getNumberOfChannels();
        assertThat(channels).isEqualTo(16);
    }

    @Test
    public void samplingFrequencyTest() {
        int frequency = header.getSamplingFrequency();
        assertThat(frequency).isEqualTo(250);
    }

    @Test
    public void sizeTest() {
        int size = header.getSize();
        assertThat(size).isEqualTo(68);
    }

    @Test
    public void toStringTest() {
        byte[] expected = "AG50xDATA_V002\n00000070\nNumberOfChannels=16\nSamplingFrequencyHz=250\n\u0000\u001A".getBytes();
        byte[] actual = header.toString().getBytes();
        assertThat(actual).isEqualTo(expected);
    }

}
