
public class MusicGenerator {

	private int sampleRate;
	private int bitsPerSample;
	private int numChannels;
	private int maxVol;
	private Tones toneGenerator;
	
	public MusicGenerator(int sampleRate, int bitsPerSample, int numChannels)
	{
		this.sampleRate = sampleRate;
		this.bitsPerSample = bitsPerSample;
		this.numChannels = numChannels;
		this.maxVol = (int) Math.pow(2, bitsPerSample-1)-1;
		toneGenerator = new Tones(sampleRate, numChannels);
	}
	
	public byte[] getMusic()
	{
		//return format(add(toneGenerator.toneFlat(440, 2, .1, 1, 100, Waves.WaveType.sin),
		//		toneGenerator.toneFlat(261.25, 60, 1, 10, 100, Waves.WaveType.sin)));
		double[][] AtoC = toneGenerator.toneBend(261.25, 440, 3, 60, 0, .1, 100, Waves.WaveType.sin);
		double[][] A = toneGenerator.toneFlat(261.25, 1, .1, 0, 100, Waves.WaveType.sin);
		return(format(add(A, AtoC, 1)));
	}
	
	private byte[] format(double[][] music)
	{
		double max = 0;
		for (int frame = 0; frame < music[0].length; frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				if(music[channel][frame] > max)
					max = music[channel][frame];
			}
		}
		
		byte[] ret = new byte[music.length * music[0].length * bitsPerSample / 8];
		for (int frame = 0; frame < music[0].length; frame++) {
			for (int channel = 0; channel < numChannels; channel++) {
				music[channel][frame] *= maxVol / max;
				for (int byt = 0; byt < bitsPerSample / 8; byt++) {
					ret[frame * numChannels * bitsPerSample / 8 + channel*bitsPerSample/8 + byt]
							= (byte) ((int)(music[channel][frame]) >> byt * 8);
				}
			}
		}
		return ret;
	}
	
	private double[][] add(double[][] mus1, double[][] mus2, double offsetSeconds)
	{
		int offsetFrames = (int) (offsetSeconds * sampleRate);
		double[][] ret = new double[numChannels][Math.max(mus1[0].length, mus2[0].length + offsetFrames)];
		for(int frame = 0; frame < mus1[0].length; frame++){
			for(int channel = 0; channel < numChannels; channel++) {
				ret[channel][frame] = mus1[channel][frame];
			}
		}
		for(int frame = 0; frame < mus2[0].length; frame++){
			for(int channel = 0; channel < numChannels; channel++) {
				ret[channel][frame+offsetFrames] += mus2[channel][frame];
			}
		}
		return ret;
	}
	
}
