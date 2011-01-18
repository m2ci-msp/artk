package fr.loria.parole.ema;

import java.util.Vector;

import org.junit.Test;
import static org.junit.Assert.*;

public class SweepTest {

	@Test
	public void test_getChannels() {
		Sweep s = new Sweep();
		assertNotNull(s.getChannels());
	}

	@Test
	public void test_setChannels() {
		Sweep s = new Sweep();
		Vector<Channel> ch = new Vector<Channel>();
		s.setChannels(ch);
		assertEquals(ch, s.getChannels());
	}
}
