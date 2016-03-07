
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
		Waves.WaveType type = Waves.WaveType.square;
		double[][] AtoC = toneGenerator.toneBend(261.25, 440, 2, 0, 0, 100, type);
		double[][] A = toneGenerator.toneFlat(261.25, 1, .1, 0, 100, type);
		double[][] C = toneGenerator.toneFlat(440, 1, 0, 0, 100, type);
		double[][] CtoN = toneGenerator.toneBend(440, 400, 2, 0, 0, 100, type);
		double[][] N = toneGenerator.toneFlat(400, 1, 0, .1, 100, type);
		
		return format(add(add(add(add(A, AtoC, 1), C, 3), CtoN, 4), N, 6));
	}
	
	private double[][] declick(double[][] music)
	{
		for (int channel = 0; channel < numChannels; channel++) {
			for (int frame = 0; frame < music[0].length; frame++) {
				//if(music[channel])
			}
		}
		return music;
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
		
		int noClickOffset;
		for(noClickOffset = offsetFrames; noClickOffset > 0; noClickOffset--)
		{
			boolean shouldBreak = false;
			for(int channel = 0; channel < numChannels; channel++) {
				if(ret[channel][noClickOffset] != 0)
					shouldBreak = true;
			}
			if(shouldBreak) break;
		}
		if(2 * ret[0][noClickOffset] - ret[0][noClickOffset-1] < (ret[0][noClickOffset] - ret[0][noClickOffset-1]) / 2)
			noClickOffset++;
		if((double) (noClickOffset) / offsetFrames > .99)
			offsetFrames = noClickOffset;
		
		for(int frame = 0; frame < mus2[0].length; frame++){
			for(int channel = 0; channel < numChannels; channel++) {
				ret[channel][frame+offsetFrames] += mus2[channel][frame];
			}
		}
		return ret;
	}
	
}
