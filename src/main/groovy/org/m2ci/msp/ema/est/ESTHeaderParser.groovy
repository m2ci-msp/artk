package org.m2ci.msp.ema.est

class ESTHeaderParser {

    /////////////////////////////////////////////////////////////////////////////

    ESTHeaderParser(estFile, estData) {

        this.estData = estData

        // get header
        this.headerData = estFile.getHeader()

    }

    /////////////////////////////////////////////////////////////////////////////

    public def parse() {

        this.parseMetaData()
        this.parseChannelData()

    }

    /////////////////////////////////////////////////////////////////////////////

    private def parseMetaData() {

        // empty meta data map
        this.estData.metaData = [:]

        this.headerData.each { line ->

            if (line.startsWith("Channel") == false) {

                def metaTokens = line.tokenize()

                if (metaTokens.size() == 2) {

                    this.estData.metaData[metaTokens[0]] = metaTokens[1]

                }


            } // end if channel
        } // end header each

    } // end parseMetaData

    /////////////////////////////////////////////////////////////////////////////

    private def parseChannelData() {

        // empty the map for storing the read channel data
        this.estData.channelMap = [:]

        // empty the correspondence list between data position and channel map
        this.estData.correspondences = []

        // first channel is always the time stamp
        this.estData.channelMap["timeStamp"] = [values: []]

        // add correspondence
        this.estData.correspondences.add(this.estData.channelMap["timeStamp"]["values"])

        // second channel is always the value 1
        this.estData.oneList = []

        // add correspondence
        this.estData.correspondences.add(this.estData.oneList)

        this.headerData.each { line ->

            if (line.startsWith("Channel")) {

                // get channel id data (starts after whitespace after keyword Channel)
                def channelData = line.tokenize()[1].tokenize('_')
                def channelName = ""
                def channelProperty = ""

                // id data is separated by '_' with the last token describing the property
                // if present
                if (channelData.size() > 1) {
                    // channel property name is always the last token
                    channelProperty = channelData.pop()

                    // channel name is given by the rest
                    channelName = channelData.join('_')

                } else {
                    // special case: property name not present
                    channelName = channelData[0]

                    // use values as property name
                    channelProperty = "values"

                }

                // create property map for channel if not present
                if (this.estData.channelMap[channelName] == null) {
                    this.estData.channelMap[channelName] = [:]
                }

                // create list for property in current channel
                this.estData.channelMap[channelName][channelProperty] = []

                // also create correspondence
                this.estData.correspondences.add(this.estData.channelMap[channelName][channelProperty])

            } // end if
        } // end header.each

    } // end parseChannelData

    /////////////////////////////////////////////////////////////////////////////

    def estData

    def headerData
    /////////////////////////////////////////////////////////////////////////////

}
