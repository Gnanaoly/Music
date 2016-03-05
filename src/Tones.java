
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
		return toneBend(hz, hz, 0, duration, buildIn, buildOut, volume, type);
	}
	
	public double[][] toneBend(double hzStart, double hzEnd, double bendTime, double duration,
			double buildIn, double buildOut, double volume, Waves.WaveType type)
	{
		int numFrames = (int) (sampleRate * duration);
		
		double buildInRate = getRate(buildIn, volume);
		double buildOutRate = getRate(buildOut, volume);
		double vol = 0;
		int buildOutStart = (int) (numFrames - volume/buildOutRate);
		
		double bendRate = getRate(bendTime, hzEnd-hzStart);
		double hz = hzStart;
		
		double[][] music = new double[numChannels][numFrames];
		for (int frame = 0; frame < numFrames; frame++) {
			if(frame >= buildOutStart) vol -= buildOutRate;
			else if(vol < volume) vol += buildInRate;
			if(hz != hzEnd) hz += bendRate;
			if(Math.signum(hz - hzEnd) == Math.signum(bendRate)) hz = hzEnd;
			for (int channel = 0; channel < numChannels; channel++) {
				music[channel][frame] = waveGenerator.getWave(hz, frame, bendRate, type) * vol;
			}
		}
		return music;
	}
	
}
