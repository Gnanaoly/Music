package music;

import java.util.ArrayList;

public class MusicUtil {

	private int sampleRate;
	private int bitsPerSample;
	private int numChannels;
	private int maxVol;

	public MusicUtil(int sampleRate, int bitsPerSample, int numChannels) {
		this.sampleRate = sampleRate;
		this.bitsPerSample = bitsPerSample;
		this.numChannels = numChannels;
		this.maxVol = (int) Math.pow(2, bitsPerSample - 1) - 1;
		maxVol /= 2;
	}

	public byte[] format(ArrayList<double[]> music) {
		double max = 0;
		for (int frame = 0; frame < music.size(); frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				if (music.get(frame)[channel] > max)
					max = music.get(frame)[channel];
			}
		}

		byte[] ret = new byte[numChannels * music.size() * bitsPerSample / 8];
		for (int frame = 0; frame < music.size(); frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				music.get(frame)[channel] *= maxVol / max;
				for (int byt = 0; byt < bitsPerSample / 8; byt++) {
					ret[frame * numChannels * bitsPerSample / 8 + channel * bitsPerSample / 8 + byt]
							= (byte) ((int) (music.get(frame)[channel]) >> byt * 8);
				}
			}
		}
		return ret;
	}

	public void add(ArrayList<double[]> music, ArrayList<double[]> add, double offsetSeconds, boolean noClick) {
		int offsetFrames = (int) (offsetSeconds * sampleRate);
		
		for(int i = music.size(); i < add.size() + offsetFrames; i++) {
			music.add(new double[numChannels]);
		}

		if (noClick && offsetSeconds > 0) {
			int noClickOffset;
			for (noClickOffset = offsetFrames; noClickOffset > 0; noClickOffset--) {
				boolean shouldBreak = false;
				for (int channel = 0; channel < numChannels; channel++) {
					if (music.get(noClickOffset)[channel] != 0)
						shouldBreak = true;
				}
				if (shouldBreak)
					break;
			}
			if (2 * music.get(noClickOffset)[0] - music.get(noClickOffset-1)[0]
					< (music.get(noClickOffset)[0] - music.get(noClickOffset - 1)[0]) / 2)
				noClickOffset++;
			if ((double) (noClickOffset) / offsetFrames > .99 && noClickOffset < offsetFrames)
				offsetFrames = noClickOffset;
		}

		for (int frame = 0; frame < add.size(); frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				music.get(frame + offsetFrames)[channel] += add.get(frame)[channel];
			}
		}
	}
}
