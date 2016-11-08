package org.m2ci.msp.ema.est

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

        def leftIndex = null
        def rightIndex = null

        // find index of left neighbor that is not a NaN
        leftIndex = (index..0).find { valueList[it] != Float.NaN }

        // find index of right neighbor that is not a NaN
        rightIndex = (index..valueList.size() - 1).find { valueList[it] != Float.NaN }

        if (leftIndex == null || rightIndex == null) {
            throw GroovyRuntimeException("Can not remove NaN value.")
        }

        // compute distance
        def distance = (double) rightIndex - leftIndex
        def leftPoint = valueList[leftIndex]
        def rightPoint = valueList[rightIndex]

        // perform linear interpolation
        def result = leftPoint + (index - leftIndex) * (rightPoint - leftPoint) / distance

        return result

    }

    /////////////////////////////////////////////////////////////////////////////

    def estData

    /////////////////////////////////////////////////////////////////////////////

}
