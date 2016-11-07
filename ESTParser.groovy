class ESTParser {

    ESTParser() {
    }

    def parse(fileName) {

        def estFile = new ESTFile(fileName)
        def estData = new ESTData()

        def headerParser = new ESTHeaderParser(estFile, estData)
        headerParser.parse()

        def contentParser = new ESTContentParser(estFile, estData)
        contentParser.parse()

        return estData

    }

}
