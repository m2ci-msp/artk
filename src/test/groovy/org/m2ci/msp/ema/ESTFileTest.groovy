package org.m2ci.msp.ema

import org.ejml.simple.SimpleMatrix
import org.junit.*
import org.m2ci.msp.ema.est.ESTParser

class ESTFileTest {

    File estBinaryFile
    File posBinaryFile

    File unpackTestFile(String resourceName) {
        def tmpFile = File.createTempFile(resourceName, '.tmp')
        tmpFile.deleteOnExit()
        def stream = getClass().getResourceAsStream(resourceName)
        assert stream?.available()
        tmpFile.withOutputStream {
            it << stream.bytes
        }
        return tmpFile
    }

    @Before
    void unpackTestFiles() {
        estBinaryFile = unpackTestFile('/esttrack.bin')
        posBinaryFile = unpackTestFile('/ag500.pos')
    }

    @Test
    void testLoadFromBinaryChannelNames() {
        def posFile = new AG500PosFile(posBinaryFile)
        def estFile = new ESTParser().parse(estBinaryFile.path)
        def actual = estFile.channelMap.keySet().collect()
        def expected = ['timeStamps'] + posFile.channelNames
        assert actual == expected
    }

    @Test
    void testLoadFromBinaryData() {
        def posFile = new AG500PosFile(posBinaryFile)
        def estFile = new ESTParser().parse(estBinaryFile.path)
        def actual = estFile.channelMap.Ch1.theta
        def ch1 = posFile.getChannelIndex('Ch1')
        def theta = 4
        def expected = posFile.data.extractMatrix(0, SimpleMatrix.END, ch1 + theta, ch1 + theta + 1).matrix.data
        assert actual == expected
    }
}
