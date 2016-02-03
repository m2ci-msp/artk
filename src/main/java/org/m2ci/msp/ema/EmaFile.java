package org.m2ci.msp.ema;

import java.util.ArrayList;
import java.util.Collections;

import org.ejml.simple.SimpleMatrix;

import com.google.common.collect.Lists;

public abstract class EmaFile {

	SimpleMatrix data;
	protected int numberOfChannels;
	protected ArrayList<String> channelNames;

	ArrayList<Double> times;
	double timeOffset = 0;
	double samplingFrequency = 1;

	// channels

	protected void initChannelNames() {
		ArrayList<String> names = Lists.newArrayListWithCapacity(getNumberOfChannels());
		for (int c = 1; c <= getNumberOfChannels(); c++) {
			names.add("Ch" + c);
		}
		setChannelNames(names);
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public ArrayList<String> getChannelNames() {
		return channelNames;
	}

	public int getChannelIndex(String channelName) {
		int channelIndex = getChannelNames().indexOf(channelName);
		if (channelIndex < 0) {
			throw new IllegalArgumentException(String.format("No channel named %s can be found.", channelName));
		}
		return channelIndex;
	}

	public void setChannelNames(ArrayList<String> newChannelNames) {
		if (newChannelNames.size() != getNumberOfChannels()) {
			throw new IllegalArgumentException(String.format("Expected %d channel names, but got %d", getNumberOfChannels(),
					newChannelNames.size()));
		}
		channelNames = Lists.newArrayList(newChannelNames);
	}

	public int getNumberOfFrames() {
		return data.numRows();
	}

	abstract public void setData(SimpleMatrix newData);

	public SimpleMatrix getData() {
		return data;
	}
	
	public double getSamplingFrequency() {
		return samplingFrequency;
	}

	public void setSamplingFrequency(double newSamplingFrequency) {
		samplingFrequency = newSamplingFrequency;
	}

	public double getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(double newTimeOffset) {
		timeOffset = newTimeOffset;
		updateTimes();
	}

	protected void updateTimes() {
		if (times == null || times.isEmpty()) {
			times = Lists.newArrayList(Collections.nCopies(getNumberOfFrames(), 0.0));
		}
		double time = 0.5 / getSamplingFrequency() + getTimeOffset();
		for (int t = 0; t < times.size(); t++) {
			times.set(t, time);
			time += 1 / getSamplingFrequency();
		}
	}

	public double getFirstSampleTime() {
		return times.get(0);
	}

	public double getLastSampleTime() {
		return times.get(times.size() - 1);
	}

	protected int findFirstValueGreaterThanOrEqualTo(double value, ArrayList<Double> values) {
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) >= value) {
				return i;
			}
		}
		return -1;
	}

	protected int findLastValueLessThanOrEqualTo(double value, ArrayList<Double> values) {
		for (int i = values.size() - 1; i >= 0; i--) {
			if (values.get(i) <= value) {
				return i;
			}
		}
		return -1;
	}

	abstract protected EmaFile extractFrameRange(int firstFrame, int lastFrame);

	public EmaFile extractTimeRange(double startTime, double endTime) {
		int firstFrame = findFirstValueGreaterThanOrEqualTo(startTime, times);
		int lastFrame = findLastValueLessThanOrEqualTo(endTime, times);
		if (firstFrame > lastFrame) {
			throw new IllegalArgumentException(String.format("Requested times are out of range, should be between %f and %f",
					times.get(0), times.get(times.size() - 1)));
		}
		return extractFrameRange(firstFrame, lastFrame);
	}

	public abstract TextFile asText();
}
