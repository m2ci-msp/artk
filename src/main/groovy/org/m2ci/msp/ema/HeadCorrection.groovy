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

	HeadCorrection(AG500PosFile posFile, referenceChannels) {

		this.numberOfFieldsPerChannel = posFile.getNumberOfFieldsPerChannel()
		this.numberOfChannels = posFile.data.numCols() / this.numberOfFieldsPerChannel
		this.data = posFile.data

		this.referenceChannels = []

		referenceChannels.each { channelName ->
			this.referenceChannels.add(posFile.getChannelIndex(channelName))
		}
		
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

		// make sure that the transposed matrix is returned
		def result = new SimpleMatrix(structureTensor.svd().getSVD().getV(null, true))

		return result

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

}
