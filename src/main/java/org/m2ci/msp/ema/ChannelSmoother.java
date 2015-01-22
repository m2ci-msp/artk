package org.m2ci.msp.ema;
import java.util.Vector;


public class ChannelSmoother {
	
	private double m_standardDeviation;
	private int m_sizeOfWindow;
	private Vector<Double> m_gaussianKernel;
	private int m_radius;
	
	ChannelSmoother( double standardDeviation ) {
		
		m_standardDeviation = standardDeviation;
		
		final double multiplier = 3;
		
		// rounded up size in each direction of truncated Gaussian
		m_radius = (int) (multiplier * standardDeviation + 0.5);
		
		m_sizeOfWindow = 2 * m_radius + 1;
		
		m_gaussianKernel = new Vector<Double>(m_sizeOfWindow);
		
		createGaussian();
		
	}
	
	public Vector<Double> smoothChannel(Vector<Double> channel) {
		
		Vector<Double> channelWithBoundaries = addBoundariesToChannel(channel);
		
		int sampleAmount = channel.size();
		
		Vector<Double> result = new Vector<Double>();
		result.setSize(sampleAmount);
		
		for( int i = m_radius; i < sampleAmount + m_radius; ++i) {
			
			final int originalIndex = i - m_radius;
			
			double smoothedVersion = 0;
			
			// perform the weighting in the window around the current sample
			for( int r = -m_radius; r <= m_radius; ++r) {
				
				final int indexInGaussian = r + m_radius;
				final double valueOfGaussian = m_gaussianKernel.get(indexInGaussian);
				final double originalValue = channelWithBoundaries.get(i + r);
								
				smoothedVersion += valueOfGaussian * originalValue;
	
			}
			
			result.set(originalIndex, smoothedVersion);
		}
		
		
		return result;
		
	}
	
	private Vector<Double> addBoundariesToChannel(Vector<Double> original) {
		
		// create a copy
		Vector<Double> result = new Vector<Double>();
		
		result.setSize(original.size() + 2 * m_radius);
		
		// copy original
		for(int i = 0; i < original.size(); ++i) {
			
			// add boundary
			result.set(i + m_radius, original.get(i));
		}
		
		// now mirror at the boundaries
		
		for(int i = 1; i <= m_radius; ++i) {
			
			// mirror on the left
			int mirrorPoint = m_radius;
			double valueToMirror = result.get(mirrorPoint + i);
			
			result.set(mirrorPoint - i, valueToMirror);
			
			// mirror on the right
			mirrorPoint = original.size() + m_radius - 1;
			valueToMirror = result.get(mirrorPoint - i);
			
			result.set(mirrorPoint + i, valueToMirror);
			
		}
		
		
		return result;
	}
	
	private void createGaussian() {		
		
		final double pi = Math.PI;
			
		// two time saver variables
		final double ts1 = 2 * m_standardDeviation * m_standardDeviation;
		final double ts2 = 1. / (Math.sqrt(ts1 * pi));
		
		// normalization factor
		double norm = 0;
		
		// precompute values of 1-D Gaussian
		for( int i = 0; i < m_sizeOfWindow; ++i) {
			
			// center Gaussian
			final double x = Math.pow(m_radius - i, 2.);
			final double value = ts2 * Math.exp(- x / ts1);
			
			m_gaussianKernel.set(i, value);
			
			// add weight
			norm += value;
			
		}
		
		// normalize truncated Gaussian
		for( int i = 0; i < m_sizeOfWindow; ++i) {
			
			final double oldValue = m_gaussianKernel.get(i);
			final double newValue = oldValue / norm;
			
			m_gaussianKernel.set(i,  newValue);
		}		
			
	}

}
