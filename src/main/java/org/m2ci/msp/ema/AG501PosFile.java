package org.m2ci.msp.ema;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

import org.ejml.simple.SimpleMatrix;

import com.google.common.collect.Lists;

public class AG501PosFile extends AG500PosFile {

    AG501PosFileHeader header;

    protected AG501PosFile() {
    }

    private Vector<Double> extractField(int fieldIndex) {

        SimpleMatrix channelFieldDataMatrix = data.extractVector(false, fieldIndex);

        Vector<Double> channelFieldDataVector = new Vector<Double>();

        for (int i = 0; i < channelFieldDataMatrix.numRows(); ++i) {
            channelFieldDataVector.add(channelFieldDataMatrix.get(i, 0));
        }

        return channelFieldDataVector;
    }

    public AG501PosFile withSmoothedChannels(double sigma) {

        int column = 0;
        for (int channel = 0; channel < numberOfChannels; ++channel) {

            for (Fields field : Fields.values()) {

                // do not smooth RMS and EXTRA fields
                if (field == Fields.RMS || field == Fields.EXTRA) {
                    ++column;
                    continue;
                }

                // smooth the field data
                Vector<Double> currentField = extractField(column);
                ChannelSmoother smoother = new ChannelSmoother(sigma);
                Vector<Double> smoothedData = smoother.smoothChannel(currentField);

                // write back to data
                for (int i = 0; i < smoothedData.size(); ++i) {
                    data.set(i, column, smoothedData.get(i));
                }

                // proceed with next field
                ++column;

            }
        }

        return this;
    }

    public AG501PosFile(File file) throws IOException {
        header = new AG501PosFileHeader(file);
        numberOfChannels = header.getNumberOfChannels();
        setData(read(file));
        initChannelNames();
    }

    @Override
    public double getSamplingFrequency() {
        return header.getSamplingFrequency();
    }

    private AG501PosFileHeader getHeader() {
        return header;
    }

    private void setHeader(AG501PosFileHeader newHeader) {
        header = newHeader;
    }

    private AG501PosFile withHeader(AG501PosFileHeader newHeader) {
        setHeader(newHeader);
        return this;
    }

    @Override
    public int getHeaderSize() {
        int size = header.getSize() + 2;
        return size;
    }

    @Override
    protected void writeHeader(OutputStream stream) throws IOException {
        byte[] bytes = getHeader().toString().getBytes();
        stream.write(bytes);
        return;
    }

    @Override
    public AG501PosFile withData(SimpleMatrix data) {
        setData(data);
        return this;
    }

    @Override
    public AG501PosFile withTimeOffset(double offset) {
        setTimeOffset(offset);
        return this;
    }

    @Override
    public AG501PosFile withChannelNames(ArrayList<String> newChannelNames) {
        setChannelNames(newChannelNames);
        return this;
    }

    @Override
    public AG501PosFile extractChannel(int channelIndex) {
        return new AG501PosFile().withHeader(getHeader()).withData(extractChannelData(channelIndex))
                .withChannelNames(Lists.newArrayList(channelNames.get(channelIndex)));
    }

    @Override
    protected AG501PosFile extractFrameRange(int firstFrame, int lastFrame) {
        int firstCol = 0;
        int lastCol = data.numCols();
        SimpleMatrix extractMatrix = data.extractMatrix(firstFrame, lastFrame, firstCol, lastCol);
        double offset = times.get(firstFrame) - 0.5 / getSamplingFrequency();
        AG501PosFile extraction = new AG501PosFile().withHeader(getHeader()).withData(extractMatrix).withTimeOffset(offset)
                .withChannelNames(getChannelNames());
        return extraction;
    }
}
