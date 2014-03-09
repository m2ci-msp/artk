package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class BvhFile {

	float samplingFrequency;
	SimpleMatrix data;
	private ArrayList<String> channelNames;

	public BvhFile(SimpleMatrix data) {
		this.data = data;
	}

	public int getNumberOfChannels() {
		return data.numCols() / getNumberOfFieldsPerChannel();
	}

	public static int getNumberOfFieldsPerChannel() {
		return Fields.values().length;
	}

	public int getNumberOfFrames() {
		return data.numRows();
	}

	// fluent setters

	public BvhFile withSamplingFrequency(float newSamplingFrequency) {
		this.samplingFrequency = newSamplingFrequency;
		return this;
	}

	public BvhFile withChannelNames(ArrayList<String> newChannelNames) {
		if (channelNames == null || channelNames.isEmpty()) {
			channelNames = newChannelNames;
			return this;
		}
		if (newChannelNames.size() != getNumberOfChannels()) {
			throw new IllegalArgumentException(String.format("Expected %d channel names, but got %d", getNumberOfChannels(),
					newChannelNames.size()));
		}
		Collections.copy(channelNames, newChannelNames);
		return this;
	}

	// output

	public void writeTo(File file) throws IOException {
		StringBuilder bvh = new StringBuilder();
		bvh.append("HIERARCHY\n");
		for (int c = 0; c < getNumberOfChannels(); c++) {
			bvh.append(String.format("ROOT %s\n", channelNames.get(c))).append("{\n");
			bvh.append(String.format("\tOFFSET\t%.0f\t%.0f\t%.0f\n", 0f, 0f, 0f));
			bvh.append(String.format("\tCHANNELS %d ", getNumberOfFieldsPerChannel()));
			Joiner.on(" ").appendTo(bvh, Fields.values()).append("\n");
			bvh.append("\tEnd Site\n").append("\t{\n");
			bvh.append(String.format("\t\tOFFSET\t%.0f\t%.0f\t%.0f\n", 0f, 0f, -1f)).append("\t}\n").append("}\n");
		}
		bvh.append("MOTION\n").append(String.format("Frames:\t%d\n", getNumberOfFrames()));
		bvh.append(String.format("Frame Time:\t%f\n", 1 / samplingFrequency));
		for (int row = 0; row < data.numRows(); row++) {
			for (int c = 0; c < getNumberOfChannels(); c++) {
				int col = c * getNumberOfFieldsPerChannel();
				Joiner.on("\t").appendTo(bvh, data.iterator(true, row, col, row, col + 5)).append("\t");
			}
			bvh.setLength(bvh.length() - 1);
			bvh.append("\n");
		}
		Files.write(bvh, file, Charsets.US_ASCII);
	}

	public enum Fields {
		XPOSITION {
			public String toString() {
				return "XPosition";
			}
		},
		YPOSITION {
			public String toString() {
				return "YPosition";
			}
		},
		ZPOSITION {
			public String toString() {
				return "ZPosition";
			}
		},
		XROTATION {
			public String toString() {
				return "XRotation";
			}
		},
		YROTATION {
			public String toString() {
				return "YRotation";
			}
		},
		ZROTATION {
			public String toString() {
				return "ZRotation";
			}
		}
	}
}
