class ESTNaNRemover {

    /////////////////////////////////////////////////////////////////////////////

    ESTNaNRemover(estData) {
        this.estData = estData
    }

    /////////////////////////////////////////////////////////////////////////////

    def remove() {

        this.estData.channelMap.each { channelName, channelProperties ->

            channelProperties.each { propertyName, values ->
                this.removeNaN(values)
            }

        }

    }

    /////////////////////////////////////////////////////////////////////////////

    private def removeNaN(valueList) {

        def originalValues = valueList.clone()

        (0..originalValues.size() - 1).each { index ->

            if (originalValues[index] == Float.NaN) {
                valueList[index] = this.interpolate(originalValues, index)
            } else {
                valueList[index] = originalValues[index]
            }

        }

    }

    /////////////////////////////////////////////////////////////////////////////

    private def interpolate(valueList, index) {

        def leftIndex = -1
        def rightIndex = -1

        // find index of left neighbor that is not a NaN
        (index..0).each { left ->

            if (valueList[left] != Float.NaN) {
                leftIndex = left
                return
            }

        }

        // find index of right neighbor that is not a NaN
        (index..valueList.size() - 1).each { right ->

            if (valueList[right] != Float.NaN) {
                rightIndex = right
                return
            }
        }

        if (leftIndex == -1 || rightIndex == -1) {
            throw GroovyRuntimeException("Can not remove NaN value.")
        }

        // compute distance
        def distance = (double) rightIndex - leftIndex
        def leftPoint = valueList[leftIndex]
        def rightPoint = valueList[rightIndex]

        // perform linear interpolation
        def result = leftPoint + (index - leftIndex) / distance * (rightPoint - leftPoint)

        return result

    }

    /////////////////////////////////////////////////////////////////////////////

    def estData

    /////////////////////////////////////////////////////////////////////////////

}
