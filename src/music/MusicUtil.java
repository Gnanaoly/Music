package music;

import java.util.List;
import java.util.ListIterator;

public class MusicUtil {

	private int sampleRate;
	private int bitsPerSample;
	private int numChannels;
	private double maxVol;

	public MusicUtil(int sampleRate, int bitsPerSample, int numChannels) {
		this.sampleRate = sampleRate;
		this.bitsPerSample = bitsPerSample;
		this.numChannels = numChannels;
		this.maxVol = Math.pow(2, bitsPerSample - 1) - 1;
	}

	public byte[] format(List<double[]> music) {
		double max = 0;
		ListIterator<double[]> it = music.listIterator();
		while(it.hasNext()) {
			double[] current = it.next();
			for (int channel = 0; channel < numChannels; channel++) {
				if (Math.abs(current[channel]) > max)
					max = Math.abs(current[channel]);
			}
		}

		it = music.listIterator();
		byte[] ret = new byte[numChannels * music.size() * bitsPerSample / 8];
		int frame = 0;
		while(it.hasNext()) {
			double[] current = it.next();
			for (int channel = 0; channel < numChannels; channel++) {
				current[channel] *= maxVol / max;
				for (int byt = 0; byt < bitsPerSample / 8; byt++) {
					ret[frame * numChannels * bitsPerSample / 8 + channel * bitsPerSample / 8
							+ byt] = (byte) ((int) (current[channel]) >> byt * 8);
				}
			}
			frame++;
		}
		return ret;
	}

	public void add(List<double[]> music, List<double[]> add, double offsetSeconds) {
		int offsetFrames = (int) (offsetSeconds * sampleRate);

		ListIterator<double[]> musIt;
		ListIterator<double[]> addIt;
		
		if(music.size() < offsetFrames) {
			musIt = music.listIterator(music.size());
			while(music.size() < offsetFrames) {
				musIt.add(new double[numChannels]);
			}
		}
		
		musIt = music.listIterator(offsetFrames);
		addIt = add.listIterator();
		while(addIt.hasNext()) {
			if (! musIt.hasNext()) {
				musIt.add(addIt.next());
			} else {
				double[] musCurrent = musIt.next();
				double[] addCurrent = addIt.next();
				for (int channel = 0; channel < numChannels; channel++) {
					musCurrent[channel] += addCurrent[channel];
				}
			}
		}
	}
}
