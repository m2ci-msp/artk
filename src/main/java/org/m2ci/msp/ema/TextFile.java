package org.m2ci.msp.ema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import org.ejml.data.DMatrixIterator;
import org.ejml.simple.SimpleMatrix;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class TextFile extends EmaFile {

    protected int precision = 6;
    protected boolean writeTimes = false;

    protected TextFile() {
    }

    public TextFile(SimpleMatrix data) {
        setData(data);
    }

    public ArrayList<String> getChannelNames() {
        return channelNames;
    }

    public TextFile withChannelNames(ArrayList<String> newChannelNames) {
        setChannelNames(newChannelNames);
        return this;
    }

    public void setPrecision(int newPrecision) {
        precision = newPrecision;
    }

    public TextFile withPrecision(int newPrecision) {
        setPrecision(newPrecision);
        return this;
    }

    public TextFile withTimes() {
        writeTimes = true;
        updateTimes();
        return this;
    }

    protected void writeHeader(Writer writer) throws IOException {
        if (writeTimes) {
            writer.append("Time\t");
        }
        Joiner.on("\t").appendTo(writer, getChannelNames()).append("\n");
    }

    protected void writeData(Writer writer) throws IOException {
        String format = String.format("%%.%df", precision);
        for (int row = 0; row < data.numRows(); row++) {
            if (writeTimes) {
                writer.append(String.format(format, times.get(row))).append("\t");
            }
            int numCols = data.numCols();
            ArrayList<String> formattedFields = Lists.newArrayListWithCapacity(numCols);
            DMatrixIterator fields = data.iterator(true, row, 0, row, numCols - 1);
            while (fields.hasNext()) {
                formattedFields.add(String.format(format, fields.next()));
            }
            Joiner.on("\t").appendTo(writer, formattedFields).append("\n");
        }
    }

    public void writeTo(File file) throws IOException {
        BufferedWriter writer = Files.newWriter(file, Charsets.UTF_8);
        writeHeader(writer);
        writeData(writer);
        writer.close();
    }

    public void writeTo(String path) throws IOException {
        File file = new File(path);
        writeTo(file);
    }

    public void setData(SimpleMatrix newData) {
        data = newData;
        numberOfChannels = data.numCols();
    }

    public TextFile withData(SimpleMatrix data) {
        setData(data);
        return this;
    }

    public TextFile withSamplingFrequency(double newSamplingFrequency) {
        setSamplingFrequency(newSamplingFrequency);
        return this;
    }

    public TextFile withTimeOffset(double newTimeOffset) {
        setTimeOffset(newTimeOffset);
        return this;
    }

    @Override
    protected TextFile extractFrameRange(int firstFrame, int lastFrame) {
        int firstCol = 0;
        int lastCol = data.numCols();
        SimpleMatrix extractMatrix = data.extractMatrix(firstFrame, lastFrame, firstCol, lastCol);
        double offset = times.get(firstFrame) - 0.5 / getSamplingFrequency();
        TextFile extraction = new TextFile().withData(extractMatrix).withChannelNames(getChannelNames())
                .withSamplingFrequency(getSamplingFrequency()).withTimeOffset(offset).withPrecision(precision);
        if (writeTimes) {
            extraction = extraction.withTimes();
        }
        return extraction;
    }

    public TextFile asText() {
        return this;
    }
}
