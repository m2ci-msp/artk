package org.m2ci.msp.ema;

import static org.fest.assertions.api.Assertions.*;

import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class EmaFileTest {

	private EmaFile emaFile;

	@Before
	public void setUp() {
		emaFile = new EmaFile() {
			@Override
			public void setData(SimpleMatrix newData) {
			}

			@Override
			protected EmaFile extractFrameRange(int firstFrame, int lastFrame) {
				return null;
			}
		};
	}

	@Test
	public void testFindFirstValueGreaterThanOrEqualTo() {
		ArrayList<Double> values = Lists.newArrayList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
		double threshold = 2.5;
		int lowIndex = emaFile.findFirstValueGreaterThanOrEqualTo(threshold, values);
		assertThat(lowIndex).isEqualTo(2); // 3.0 (index 2) is first over threshold
	}

	@Test
	public void testFindLastValueLessThanOrEqualTo() {
		ArrayList<Double> values = Lists.newArrayList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
		double threshold = 2.5;
		int lowIndex = emaFile.findLastValueLessThanOrEqualTo(threshold, values);
		assertThat(lowIndex).isEqualTo(1); // 2.0 (index 1) is first under threshold
	}
}
