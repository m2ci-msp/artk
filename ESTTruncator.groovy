class ESTTruncator {

    /////////////////////////////////////////////////////////////////////////////

    ESTTruncator(estData) {
        this.estData = estData
    }

    /////////////////////////////////////////////////////////////////////////////

    def to(selection) {

        def newChannelMap = [:]

        selection.each { channelName, channelProperties ->

            newChannelMap[channelName] = [:]

            channelProperties.each { property ->

                newChannelMap[channelName][property] = this.estData.channelMap[channelName][property]

            }

        }

        this.estData.channelMap = newChannelMap

    }

    /////////////////////////////////////////////////////////////////////////////

    def estData

    /////////////////////////////////////////////////////////////////////////////

}
