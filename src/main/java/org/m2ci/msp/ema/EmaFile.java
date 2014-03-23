package org.m2ci.msp.ema;

import java.util.Collection;

public interface EmaFile {

	public int getNumberOfChannels();

	public Collection<String> getChannelNames();

	public void setChannelNames(Collection<String> newChannelNames);

	public EmaFile withChannelNames(Collection<String> newChannelNames);

	public int getSamplingFrequency();

}
