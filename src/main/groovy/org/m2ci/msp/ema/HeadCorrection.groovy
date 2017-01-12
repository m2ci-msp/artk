package org.m2ci.msp.ema

import org.ejml.simple.SimpleMatrix



class HeadCorrection {

	// Ema data structures
	SimpleMatrix data
	int numberOfFieldsPerChannel
	int numberOfChannels

	// reference channel indexes
	def frontIndex
	def leftIndex
	def rightIndex

	// for representing the transformation

	// origin of local coordinate system
	SimpleMatrix origin

	/* matrix for mapping a vector into the local coordinate system
	 *
	 * given a head, the orientation of coordinate system is:
	 *
	 * x-axis: left to right
	 * y-axis: front to back
	 * z-axis: bottom to top
	 *
	 */
	SimpleMatrix rotation

	HeadCorrection(AG500PosFile posFile, front, left, right) {

		this.numberOfFieldsPerChannel = posFile.getNumberOfFieldsPerChannel()
		this.numberOfChannels = posFile.data.numCols() / this.numberOfFieldsPerChannel
		this.data = posFile.data

		this.frontIndex = posFile.getChannelIndex(front)
		this.leftIndex = posFile.getChannelIndex(left)
		this.rightIndex = posFile.getChannelIndex(right)
		this.rotation = new SimpleMatrix(3, 3)

	}


	def performCorrection() {

		// iterate over the time
		(0 .. this.data.numRows() - 1).each { timeIndex ->

			// TODO: evaluate if time frame can be considered as stable

			// compute transformation matrix
			computeTransformation(timeIndex)

			// perform motion correction
			applyTransformation(timeIndex)

		}

	}

	void computeTransformation(timeIndex) {

		// get the reference positions
		def front = getPosition(timeIndex, frontIndex)
		def left = getPosition(timeIndex, leftIndex)
		def right = getPosition(timeIndex, rightIndex)

		computeOrigin(front, left, right)
		computeRotation(front, left, right)

	}

	void computeOrigin(front, left, right) {

		this.origin = front.plus(left.plus(right)).divide(3)

	}

	void computeRotation(front, left, right) {

		def leftToRight = right.minus(left)
		def frontToLeft = left.minus(front)

		def xAxis = normalize(leftToRight)
		def zAxis = normalize(cross(xAxis, frontToLeft))
		def yAxis = normalize(cross(zAxis, xAxis))

		(0 .. 2).each{ column ->
			this.rotation.set(0, column, xAxis.get(column, 0))
			this.rotation.set(1, column, yAxis.get(column, 0))
			this.rotation.set(2, column, zAxis.get(column, 0))
		}

	}

	void applyTransformation(timeIndex) {

		(0 .. this.numberOfChannels - 1).each { channelIndex ->

			def position = getPosition(timeIndex, channelIndex)
			def shiftedPosition = position.minus(this.origin)
			def transformedPosition = this.rotation.mult(shiftedPosition)

			setPosition(transformedPosition, timeIndex, channelIndex)

			// TODO: also transform Euler angles

		}
	}

	// helper method for getting the position of the given channel at a specific time
	def getPosition(timeIndex, channelIndex) {

		// compute start column of channel
		def columnStart = channelIndex * this.numberOfFieldsPerChannel

		// extract position vector from matrix
		def position =
				data.extractMatrix(
				// stay at the same time
				timeIndex,
				timeIndex + 1,
				// extract the three fields relevant for the position
				columnStart,
				columnStart + 3)

		// transpose vector
		position = position.transpose()

		return position

	}

	// helper method for setting the position of the given channel at a specific time
	void setPosition(position, timeIndex, channelIndex) {

		// compute start column of channel
		def columnStart = channelIndex * this.numberOfFieldsPerChannel

		// set values
		(0 .. 2).each{ index ->
			this.data.set(timeIndex, columnStart + index, position.get(index, 0))
		}

	}

	// helper method for computing the cross product
	def cross(u, v) {

		def result = new SimpleMatrix(3, 1)

		// u_2 * v_3 - u_3 * v_2
		result.set(0, 0, u.get(1) * v.get(2) - u.get(2) * v.get(1) )
		// u_3 * v_1 - u_1 * v_3
		result.set(1, 0, u.get(2) * v.get(0) - u.get(0) * v.get(2) )
		// u_1 * v_2 - u_2 * v_1
		result.set(2, 0, u.get(0) * v.get(1) - u.get(1) * v.get(0) )

		return result

	}

	// helper method for normalizing a vector
	def normalize(u) {

		def squaredLength = u.transpose().mult(u)
		def result = u.divide(Math.sqrt(squaredLength.get(0,0)))

		return result

	}

}
