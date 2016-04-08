package music;

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

	public byte[] format(double[][] music) {
		double max = 0;
		for (int frame = 0; frame < music[0].length; frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				if (music[channel][frame] > max)
					max = music[channel][frame];
			}
		}

		byte[] ret = new byte[music.length * music[0].length * bitsPerSample / 8];
		for (int frame = 0; frame < music[0].length; frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				music[channel][frame] *= maxVol / max;
				for (int byt = 0; byt < bitsPerSample / 8; byt++) {
					ret[frame * numChannels * bitsPerSample / 8 + channel * bitsPerSample / 8
							+ byt] = (byte) ((int) (music[channel][frame]) >> byt * 8);
				}
			}
		}
		return ret;
	}

	public double[][] add(double[][] mus1, double[][] mus2, double offsetSeconds, boolean noClick) {
		int offsetFrames = (int) (offsetSeconds * sampleRate);
		
		if(mus1 == null || mus1.length == 0 || mus1[0].length == 0) {
			mus1 = new double[numChannels][0];
		}
		
		double[][] ret = new double[numChannels][Math.max(mus1[0].length, mus2[0].length + offsetFrames)];
		for (int frame = 0; frame < mus1[0].length; frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				ret[channel][frame] = mus1[channel][frame];
			}
		}

		if (noClick && offsetSeconds > 0) {
			int noClickOffset;
			for (noClickOffset = offsetFrames; noClickOffset > 0; noClickOffset--) {
				boolean shouldBreak = false;
				for (int channel = 0; channel < numChannels; channel++) {
					if (ret[channel][noClickOffset] != 0)
						shouldBreak = true;
				}
				if (shouldBreak)
					break;
			}
			if (2 * ret[0][noClickOffset]
					- ret[0][noClickOffset - 1] < (ret[0][noClickOffset] - ret[0][noClickOffset - 1]) / 2)
				noClickOffset++;
			if ((double) (noClickOffset) / offsetFrames > .99 && noClickOffset < offsetFrames)
				offsetFrames = noClickOffset;
		}

		for (int frame = 0; frame < mus2[0].length; frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				ret[channel][frame + offsetFrames] += mus2[channel][frame];
			}
		}
		return ret;
	}
}
