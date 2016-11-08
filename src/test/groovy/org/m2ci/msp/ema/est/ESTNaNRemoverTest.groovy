package org.m2ci.msp.ema.est

import org.junit.*

class ESTNaNRemoverTest {

    ArrayList<Float> values
    ESTNaNRemover remover

    @Before
    void setUp() {
        values = -5..5
        remover = new ESTNaNRemover()
    }

    @Test
    void testRemoveNaNPassThrough() {
        def expected = values.clone()
        def actual = values.clone()
        remover.removeNaN(actual)
        assert actual == expected
    }

    @Test
    void testRemoveNaN() {
        def expected = values.clone()
        values[5] = Float.NaN
        def actual = values.clone()
        remover.removeNaN(actual)
        assert actual == expected
    }

    @Test
    void testRemoveNaNAtStart() {
        def expected = values.clone()
        values[0] = Float.NaN
        def actual = values.clone()
        // NaN will be filled with first non-NaN to the right
        expected[0] = expected[1]
        remover.removeNaN(actual)
        assert actual == expected
    }

    @Test
    void testRemoveNaNAtEnd() {
        def expected = values.clone()
        values[-1] = Float.NaN
        def actual = values.clone()
        // NaN will be filled with first non-NaN to the left
        expected[-1] = expected[-2]
        remover.removeNaN(actual)
        assert actual == expected
    }
}
