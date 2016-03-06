
public class Tones {

	private int sampleRate;
	private int numChannels;
	private Waves waveGenerator;
	
	public Tones(int sampleRate, int numChannels)
	{
		this.sampleRate = sampleRate;
		this.numChannels = numChannels;
		waveGenerator = new Waves(sampleRate);
	}
	
	private double getRate(double time, double change)
	{
		if(time <= 0) return change;
		return change / (time * sampleRate);
	}
	
	public double[][] toneFlat(double hz, double duration,
			double buildIn, double buildOut, double volume, Waves.WaveType type)
	{
		return toneBend(hz, hz, duration, buildIn, buildOut, volume, type);
	}
	
	public double[][] toneBend(double hzStart, double hzEnd, double duration,
			double buildIn, double buildOut, double volume, Waves.WaveType type)
	{
		int numFrames = (int) (sampleRate * duration);
		
		double buildInRate = getRate(buildIn, volume);
		double buildOutRate = getRate(buildOut, volume);
		double vol = 0;
		int buildOutStart = (int) (numFrames - volume/buildOutRate);
		
		//We have to cut bendRate in half because the waves being compressed doubles the effect.
		double bendRate = getRate(duration, hzEnd-hzStart) / 2;
		double hz = hzStart;
		
		double[][] music = new double[numChannels][numFrames];
		for (int frame = 0; frame < numFrames; frame++) {
			if(frame >= buildOutStart) vol -= buildOutRate;
			else if(vol < volume) vol += buildInRate;
			hz += bendRate;
			for (int channel = 0; channel < numChannels; channel++) {
				music[channel][frame] = waveGenerator.getWave(hz, frame, type) * vol;
			}
		}
		return music;
	}
	
}
