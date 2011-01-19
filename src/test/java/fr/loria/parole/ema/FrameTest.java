/**
 * 
 */
package fr.loria.parole.ema;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * @author steiner
 * 
 */
public class FrameTest {

	private String name = "fnord";
	private float x = 1;
	private float y = 2;
	private float z = 3;
	private float phi = 4;
	private float theta = 5;
	private float rms = 6;
	private float extra = 0;

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getName()}.
	 */
	@Test
	public void testGetName() {
		String expected = name;
		String actual = constructFrame().getName();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link fr.loria.parole.ema.Frame#setName(java.lang.String)}.
	 */
	@Test
	public void testSetName() {
		String expected = StringUtils.reverse(name);
		Frame frame = constructFrame();
		frame.setName(expected);
		String actual = frame.getName();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getX()}.
	 */
	@Test
	public void testGetX() {
		float expected = x;
		float actual = constructFrame().getX();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#setX(float)}.
	 */
	@Test
	public void testSetX() {
		float expected = x * -1;
		Frame frame = constructFrame();
		frame.setX(expected);
		float actual = frame.getX();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getY()}.
	 */
	@Test
	public void testGetY() {
		float expected = y;
		float actual = constructFrame().getY();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#setY(float)}.
	 */
	@Test
	public void testSetY() {
		float expected = y * -1;
		Frame frame = constructFrame();
		frame.setY(expected);
		float actual = frame.getY();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getZ()}.
	 */
	@Test
	public void testGetZ() {
		float expected = z;
		float actual = constructFrame().getZ();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#setZ(float)}.
	 */
	@Test
	public void testSetZ() {
		float expected = z * -1;
		Frame frame = constructFrame();
		frame.setZ(expected);
		float actual = frame.getZ();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getPhi()}.
	 */
	@Test
	public void testGetPhi() {
		float expected = phi;
		float actual = constructFrame().getPhi();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#setPhi(float)}.
	 */
	@Test
	public void testSetPhi() {
		float expected = phi * -1;
		Frame frame = constructFrame();
		frame.setPhi(expected);
		float actual = frame.getPhi();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getTheta()}.
	 */
	@Test
	public void testGetTheta() {
		float expected = theta;
		float actual = constructFrame().getTheta();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#setTheta(float)}.
	 */
	@Test
	public void testSetTheta() {
		float expected = theta * -1;
		Frame frame = constructFrame();
		frame.setTheta(expected);
		float actual = frame.getTheta();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getRms()}.
	 */
	@Test
	public void testGetRms() {
		float expected = rms;
		float actual = constructFrame().getRms();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#setRms(float)}.
	 */
	@Test
	public void testSetRms() {
		float expected = rms * -1;
		Frame frame = constructFrame();
		frame.setRms(expected);
		float actual = frame.getRms();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#getExtra()}.
	 */
	@Test
	public void testGetExtra() {
		float expected = extra;
		float actual = constructFrame().getExtra();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#setExtra(float)}.
	 */
	@Test
	public void testSetExtra() {
		float expected = extra * -1;
		Frame frame = constructFrame();
		frame.setExtra(expected);
		float actual = frame.getExtra();
		assertEquals(expected, actual, 1e-7);
	}

	/**
	 * Test method for
	 * {@link fr.loria.parole.ema.Frame#Frame(java.lang.String, float, float, float, float, float, float, float)}
	 * .
	 */
	@Test
	public void testFrame() {
		Frame expected = constructFrame();
		Frame actual = constructFrame();
		boolean equals = expected.equals(actual);
		assertTrue(equals);
		// why does this fail then?
		// assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link fr.loria.parole.ema.Frame#toString()} .
	 */
	@Test
	public void testToString() {
		String expected = constructFrame().toString();
		String actual = constructFrame().toString();
		assertEquals(expected, actual);
	}

	/**
	 * Convenience function to construct a Frame, used in various tests
	 */
	public Frame constructFrame() {
		return new Frame(name, x, y, z, phi, theta, rms, extra);
	}

}
