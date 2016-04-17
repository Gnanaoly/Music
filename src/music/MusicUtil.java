package music;

import java.util.ArrayList;

public class MusicUtil {

	private int sampleRate;
	private int bitsPerSample;
	private int numChannels;
	private double maxVol;

	public MusicUtil(int sampleRate, int bitsPerSample, int numChannels) {
		this.sampleRate = sampleRate;
		this.bitsPerSample = bitsPerSample;
		this.numChannels = numChannels;
		this.maxVol = Math.pow(2, bitsPerSample - 1)-1;
	}

	public byte[] format(ArrayList<double[]> music) {
		double max = 0;
		for (int frame = 0; frame < music.size(); frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				if (Math.abs(music.get(frame)[channel]) > max)
					max = Math.abs(music.get(frame)[channel]);
			}
		}

		byte[] ret = new byte[numChannels * music.size() * bitsPerSample / 8];
		for (int frame = 0; frame < music.size(); frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				music.get(frame)[channel] *= maxVol / max;
				for (int byt = 0; byt < bitsPerSample / 8; byt++) {
					ret[frame * numChannels * bitsPerSample / 8 + channel * bitsPerSample / 8
							+ byt] = (byte) ((int) (music.get(frame)[channel]) >> byt * 8);
				}
			}
		}
		return ret;
	}

	public void add(ArrayList<double[]> music, ArrayList<double[]> add, double offsetSeconds) {
		int offsetFrames = (int) (offsetSeconds * sampleRate);

		for (int i = music.size(); i < add.size() + offsetFrames; i++) {
			music.add(new double[numChannels]);
		}

		for (int frame = 0; frame < add.size(); frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				music.get(frame + offsetFrames)[channel] += add.get(frame)[channel];
			}
		}
	}
}
