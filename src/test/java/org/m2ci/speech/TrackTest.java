/**
 * 
 */
package org.m2ci.speech;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.m2ci.speech.Track;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix1D;

/**
 * @author steiner
 * 
 */
public class TrackTest {

	private String name;
	private int numSamples;
	private FloatMatrix1D samples;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		name = "fnord";
		numSamples = 100;
		samples = new DenseFloatMatrix1D(numSamples);
	}

	/**
	 * Test method for
	 * {@link org.m2ci.speech.Track#Track(java.lang.String, float[])}.
	 */
	@Test
	public void testTrack() {
		Track expected = constructTrack();
		Track actual = constructTrack();
		boolean isEqual = expected.equals(actual);
		assertTrue(isEqual);
	}

	/**
	 * Test method for {@link org.m2ci.speech.Track#getName()}.
	 */
	@Test
	public void testGetName() {
		String expected = name;
		String actual = constructTrack().getName();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.m2ci.speech.Track#getSamples()}.
	 */
	@Test
	public void testGetSamples() {
		FloatMatrix1D expected = samples;
		FloatMatrix1D actual = constructTrack().getSamples();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.m2ci.speech.Track#getSample(int)}.
	 */
	@Test
	public void testGetSample() {
		Track track = constructTrack();
		for (int i = 0; i < track.getNumberOfSamples(); i++) {
			float expected = samples.get(i);
			float actual = track.getSample(i);
			assertEquals(expected, actual, 1e-7);
		}
	}

	/**
	 * Test method for {@link org.m2ci.speech.Track#getNumberOfSamples()}.
	 */
	@Test
	public void testGetNumberOfSamples() {
		int expected = numSamples;
		int actual = constructTrack().getNumberOfSamples();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link org.m2ci.speech.Track#toString()}.
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
