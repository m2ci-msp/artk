package org.m2ci.msp.ema

import org.ejml.simple.SimpleMatrix

class HeadCorrection {

	// Ema data structures
	SimpleMatrix data
	int numberOfFieldsPerChannel
	int numberOfChannels
	def referenceChannels


	// for representing the transformation

	// origin of local coordinate system
	SimpleMatrix origin

	// matrix for mapping a vector into the local coordinate system
	SimpleMatrix rotation

	HeadCorrection(EmaFile ema, referenceChannels) {

		this.numberOfFieldsPerChannel = 6
		this.numberOfChannels = ema.data.numCols() / this.numberOfFieldsPerChannel
		this.data = ema.data
		
		this.referenceChannels = []

		referenceChannels.each { channelName ->
			this.referenceChannels.add(ema.getChannelIndex(channelName))
		}
	}


	def performCorrection() {

		// iterate over the time
		(0 .. this.data.numRows() - 1).each { timeIndex ->

			// compute transformation matrix
			computeTransformation(timeIndex)

			// perform motion correction
			applyTransformation(timeIndex)

		}
		
	}

	void computeTransformation(timeIndex) {

		// get the reference positions
		def referencePositions =
				this.referenceChannels.collect{ channelIndex ->
					getPosition(timeIndex, channelIndex)
				}

		def mean = computeMean(referencePositions)
		def rotation = computeRotation(referencePositions, mean)

		this.origin = mean
		this.rotation = rotation

	}

	def computeMean(referencePositions) {

		def mean = new SimpleMatrix(3, 1)

		referencePositions.each { position ->
			mean = mean.plus(position)
		}

		mean = mean.divide(referencePositions.size())

		return mean
	}

	// uses PCA to derive the rotation matrix
	def computeRotation(referencePositions, mean) {

		def dyadicProducts = referencePositions.collect{ position ->
			def centered = position.minus(mean)
			return centered.mult(centered.transpose())
		}

		def structureTensor = new SimpleMatrix(3, 3)

		dyadicProducts.each{ product ->
			structureTensor = structureTensor.plus(product)
		}

		return structureTensor.svd().getV()

	}

	void applyTransformation(timeIndex) {

		(0 .. this.numberOfChannels - 1).each { channelIndex ->

			def position = getPosition(timeIndex, channelIndex)
			def transformedPosition = this.rotation.mult(position.minus(this.origin))

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

		assert(position.vector == true)
		
		// transpose vector
		position = position.transpose()

		return position

	}

	void setPosition(position, timeIndex, channelIndex) {

		// compute start column of channel
		def columnStart = channelIndex * this.numberOfFieldsPerChannel

		(0 .. 2).each{ index ->
			this.data.set(timeIndex, columnStart + index, position.get(index, 0))
		}

	}

}
