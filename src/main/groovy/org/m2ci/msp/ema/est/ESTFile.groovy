package org.m2ci.msp.ema.est

class ESTFile {

    /////////////////////////////////////////////////////////////////////////////

    ESTFile(fileName) {

        this.fileHandle = new File(fileName)

    }

    /////////////////////////////////////////////////////////////////////////////

    def getHeader() {

        def result = []
        def line = ""

        this.fileHandle.withReader(charSet) { reader ->

            while ((line = reader.readLine()) != "EST_Header_End") {
                result.add(line)
            }

        }

        return result

    }

    /////////////////////////////////////////////////////////////////////////////

    def getData() {

        def result = ""

        this.fileHandle.withReader(charSet) { reader ->

            while (reader.readLine() != "EST_Header_End") {
            }

            result = reader.text

        }

        return result

    }

    /////////////////////////////////////////////////////////////////////////////

    def fileHandle
    def charSet = "ISO-8859-1"

    /////////////////////////////////////////////////////////////////////////////

}
