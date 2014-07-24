package org.m2ci.msp.ema;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class BvhFile extends TextFile {

	private int root = -1;

	public BvhFile(SimpleMatrix data) {
		setData(data);
	}

	public static int getNumberOfFieldsPerChannel() {
		return Fields.values().length;
	}

	public ArrayList<String> getFieldNames() {
		ArrayList<String> names = Lists.newArrayListWithCapacity(getNumberOfFieldsPerChannel());
		for (Fields field : Fields.values()) {
			names.add(field.toString());
		}
		return names;
	}

	public int getNumberOfFrames() {
		return data.numRows();
	}

	// fluent setters

	public BvhFile withSamplingFrequency(double newSamplingFrequency) {
		setSamplingFrequency(newSamplingFrequency);
		return this;
	}

	public BvhFile withChannelNames(ArrayList<String> newChannelNames) {
		setChannelNames(newChannelNames);
		return this;
	}

	public BvhFile withRoot(String rootName) {
		setRoot(rootName);
		return this;
	}

	public BvhFile withRoot(int rootIndex) {
		setRoot(rootIndex);
		return this;
	}

	public void setRoot(String rootName) {
		setRoot(getChannelIndex(rootName));
	}

	public void setRoot(int rootIndex) {
		root = rootIndex;
	}

	@Override
	public void setData(SimpleMatrix newData) {
		data = newData;
		numberOfChannels = data.numCols() / getNumberOfFieldsPerChannel();
	}

	// output

	@Override
	protected void writeHeader(Writer bvh) throws IOException {
		bvh.append("HIERARCHY\n");
		for (int c = 0; c < getNumberOfChannels(); c++) {
			bvh.append(formatHeaderRoot(channelNames.get(c)));
		}
		bvh.append("MOTION\n").append(String.format("Frames:\t%d\n", getNumberOfFrames()));
		bvh.append(String.format("Frame Time:\t%f\n", 1 / getSamplingFrequency()));
	}

	private String formatHeaderRoot(String name) throws IOException {
		StringWriter out = new StringWriter();
		out.append(String.format("ROOT %s\n", name)).append("{\n");
		out.append(String.format("\tOFFSET\t%.0f\t%.0f\t%.0f\n", 0f, 0f, 0f));
		out.append(String.format("\tCHANNELS %d ", getNumberOfFieldsPerChannel()));
		Joiner.on(" ").appendTo(out, getFieldNames()).append("\n");
		out.append("\tEnd Site\n").append("\t{\n");
		out.append(String.format("\t\tOFFSET\t%.0f\t%.0f\t%.0f\n", 0f, 0f, -1f)).append("\t}\n").append("}\n");
		return out.toString();
	}

	private enum Fields {
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
