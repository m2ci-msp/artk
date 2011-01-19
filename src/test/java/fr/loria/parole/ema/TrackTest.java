/**
 * 
 */
package fr.loria.parole.ema;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * @author steiner
 * 
 */
public class TrackTest {

	private String name;
	private int numSamples;
	private float[] samples;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		name = "fnord";
		numSamples = 100;
		samples = new float[numSamples];
		Arrays.fill(samples, 1);
	}

	/**
	 * Test method for
	 * {@link fr.loria.parole.ema.Track#Track(java.lang.String, float[])}.
	 */
	@Test
	public void testTrack() {
		Track expected = constructTrack();
		Track actual = constructTrack();
		boolean isEqual = expected.equals(actual);
		assertTrue(isEqual);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Track#getName()}.
	 */
	@Test
	public void testGetName() {
		String expected = name;
		String actual = constructTrack().getName();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Track#getSamples()}.
	 */
	@Test
	public void testGetSamples() {
		float[] expected = samples;
		float[] actual = constructTrack().getSamples();
		assertArrayEquals(expected, actual, 1e-7f);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Track#getSample(int)}.
	 */
	@Test
	public void testGetSample() {
		Track track = constructTrack();
		for (int i = 0; i < track.getNumberOfSamples(); i++) {
			float expected = samples[i];
			float actual = track.getSample(i);
			assertEquals(expected, actual, 1e-7);
		}
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Track#getNumberOfSamples()}.
	 */
	@Test
	public void testGetNumberOfSamples() {
		int expected = numSamples;
		int actual = constructTrack().getNumberOfSamples();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Track#toString()}.
	 */
	@Test
	public void testToString() {
		String expected = constructTrack().toString();
		String actual = constructTrack().toString();
		assertEquals(expected, actual);
	}

	/**
	 * Convenience function to construct a Track, used in various tests
	 */
	public Track constructTrack() {
		return new Track(name, samples);
	}

	
}
