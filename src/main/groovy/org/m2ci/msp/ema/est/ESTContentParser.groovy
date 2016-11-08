package org.m2ci.msp.ema.est

import java.nio.ByteBuffer
import java.nio.ByteOrder

class ESTContentParser {

    /////////////////////////////////////////////////////////////////////////////

    ESTContentParser(estFile, estData) {
        this.estFile = estFile
        this.estData = estData
    }

    /////////////////////////////////////////////////////////////////////////////

    def parse() {

        switch (this.estData.metaData["DataType"]) {

            case "binary":
                parseBinary()
                break

            case "ascii":
                parseASCII()
                break

            default:
                throw GroovyRuntimeException("Unknown data type.")
                break

        }

    }

    /////////////////////////////////////////////////////////////////////////////

    private def parseBinary() {

        def byteOrder = ByteOrder.LITTLE_ENDIAN

        switch (this.estData.metaData["ByteOrder"]) {

            case "01":
                byteOrder = ByteOrder.LITTLE_ENDIAN
                break

            case "02":
                byteOrder = ByteOrder.BIG_ENDIAN
                break

            default:
                throw GroovyRuntimeException("Unknown byte order.")
                break

        }

        def data = this.estFile.getData()

        def bytes = data.getBytes(ESTFile.charSet)

        def byteBuffer = ByteBuffer.wrap(bytes).order(byteOrder).asFloatBuffer()

        // read data
        (0..byteBuffer.capacity() - 1).each {
            // use modulo operation to calculate position of currently read data
            this.estData.correspondences[it % this.estData.correspondences.size()].add(byteBuffer.get())
        }


    }

    /////////////////////////////////////////////////////////////////////////////

    private def parseASCII() {

        def data = this.estFile.getData()

        data.eachLine { line ->
            if (line.startsWith(this.estData.metaData["CommentChar"]) == false) {
                def tokens = line.tokenize();

                (0..tokens.size() - 1).each { index ->
                    this.estData.correspondences[index].add(tokens[index].toFloat())
                }

            } // end if

        } // end eachLine

    }

    /////////////////////////////////////////////////////////////////////////////

    def estFile
    def estData

    /////////////////////////////////////////////////////////////////////////////

}
