package fr.loria.parole.ema;

import static org.junit.Assert.*;

import org.junit.Test;


public class ChannelTest {

	@Test(expected=AssertionError.class)
	public void test_getNameNotEmpy() {
		Channel ch = new Channel("");
		assertFalse(ch.getName().isEmpty());
	}
}
