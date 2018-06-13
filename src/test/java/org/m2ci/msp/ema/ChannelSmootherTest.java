package org.m2ci.msp.ema;

import static org.fest.assertions.api.Assertions.*;

import java.util.Random;
import java.util.Vector;

import org.fest.assertions.data.Offset;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChannelSmootherTest {

    private ChannelSmoother smoother;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void initTest() {
        smoother = new ChannelSmoother(3);
        assertThat(smoother.getStandardDeviation()).isEqualTo(3);
        assertThat(smoother.getRadius()).isEqualTo(9);
        assertThat(smoother.getSizeOfWindow()).isEqualTo(2 * 9 + 1);
    }

    @Test
    public void mirrorTest() {

        smoother = new ChannelSmoother(1);

        Vector<Double> channel = new Vector<Double>();
        channel.add(1.);
        channel.add(2.);
        channel.add(3.);
        channel.add(4.);
        channel.add(5.);
        channel.add(6.);

        smoother.smoothChannel(channel);
        Vector<Double> channelWithBoundaries = smoother.getChannelWithBoundaries();
        assertThat(channelWithBoundaries.size()).isEqualTo(6 + 2 * 3);

        // manually generate mirrored result
        Vector<Double> expected = new Vector<Double>();

        expected.add(4.);
        expected.add(3.);
        expected.add(2.);
        expected.add(1.);
        expected.add(2.);
        expected.add(3.);
        expected.add(4.);
        expected.add(5.);
        expected.add(6.);
        expected.add(5.);
        expected.add(4.);
        expected.add(3.);

        for (int i = 0; i < 12; ++i) {
            assertThat(channelWithBoundaries.get(i)).isEqualTo(expected.get(i));
        }

    }

    @Test
    public void smoothIdentityTest() {

        smoother = new ChannelSmoother(1);

        Vector<Double> channel = new Vector<Double>();
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);

        Vector<Double> result = smoother.smoothChannel(channel);

        for (int i = 0; i < result.size(); ++i) {
            assertThat(result.get(i)).isEqualTo(channel.get(i));
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void signalTooShortTest() {
        smoother = new ChannelSmoother(9);

        Vector<Double> channel = new Vector<Double>();
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);
        channel.add(1.);
        smoother.smoothChannel(channel);
    }

    @Test
    public void smoothTest() {
        smoother = new ChannelSmoother(40);

        Vector<Double> channel = new Vector<Double>();

        int samples = 400;

        double mean = 0;

        Random rnd = new Random();

        for (int i = 0; i < samples; ++i) {
            double noisySample = 100 * rnd.nextDouble();
            channel.add(noisySample);

            mean += noisySample;

        }

        mean /= channel.size();

        Vector<Double> result = smoother.smoothChannel(channel);

        for (int i = 0; i < result.size(); ++i) {
            assertThat(result.get(i)).isEqualTo(mean, Offset.offset(10.));
        }

    }

}
