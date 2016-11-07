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

        // get content
        def data = this.estFile.getData()

        if (this.estData.metaData["DataType"] == "binary") {

            def byteOrder = ByteOrder.LITTLE_ENDIAN


            switch (this.estData.metaData["ByteOrder"]) {

                case "01":
                    byteOrder = ByteOrder.LITTLE_ENDIAN
                    break

                case "02":
                    byteOrder = ByteOrder.BIG_ENDIAN
                    break

                default:
                    throw GroovyRuntimeException("Unknown ByteOrder.")
                    break

            }


            def bytes = data.getBytes("ISO-8859-1")

            def byteBuffer = ByteBuffer.wrap(bytes).order(byteOrder).asFloatBuffer()

            // read data
            (1..byteBuffer.capacity()).each {
                this.estData.correspondences[it % this.estData.correspondences.size() - 1].add(byteBuffer.get())
            }

        } // end if
        else {
            throw GroovyRuntimeException("Can only read binary data.")
        }

    }

    /////////////////////////////////////////////////////////////////////////////

    def estFile
    def estData

    /////////////////////////////////////////////////////////////////////////////

}
