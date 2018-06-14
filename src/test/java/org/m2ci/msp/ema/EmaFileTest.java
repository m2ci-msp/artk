package org.m2ci.msp.ema;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmaFileTest {

    private EmaFile emaFile;

    @BeforeEach
    public void setUp() {
        emaFile = new EmaFile() {
            @Override
            public void setData(SimpleMatrix newData) {
            }

            @Override
            protected EmaFile extractFrameRange(int firstFrame, int lastFrame) {
                return null;
            }

            @Override
            public TextFile asText() {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Test
    public void testFindFirstValueGreaterThanOrEqualTo() {
        ImmutableList<Double> values = ImmutableList.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
        double threshold = 2.5;
        int lowIndex = emaFile.findFirstValueGreaterThanOrEqualTo(threshold, values);
        assertThat(lowIndex).isEqualTo(2); // 3.0 (index 2) is first over threshold
    }

    @Test
    public void testFindLastValueLessThanOrEqualTo() {
        ImmutableList<Double> values = ImmutableList.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
        double threshold = 2.5;
        int lowIndex = emaFile.findLastValueLessThanOrEqualTo(threshold, values);
        assertThat(lowIndex).isEqualTo(1); // 2.0 (index 1) is first under threshold
    }
}
