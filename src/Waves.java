
public class Waves {
	
	private int sampleRate;
	
	public enum WaveType {
		sin,
		square,
		saw,
		triangle;
	}

	public Waves(int sampleRate)
	{
		this.sampleRate = sampleRate;
	}
	
	public double getWave(double hertz, double frame, WaveType type)
	{
		switch(type)
		{
		case sin: return sin(hertz, frame);
		case square: return square(hertz, frame);
		case saw: return saw(hertz, frame);
		case triangle: return triangle(hertz, frame);
		default: return sin(hertz, frame);
		}
	}
	
	//Returns a value between 0 and 1, inclusive
	public double sin(double hertz, double frame)
	{
		return (Math.sin(hertz * frame *(2 * Math.PI / sampleRate)) + 1)/2;
	}
	
	//Returns a value between 0 and 1, inclusive
	public double square(double hertz, double frame)
	{
		if(sin(hertz, frame) > .5)
			return 1;
		return 0;
	}
	
	//Returns a value between 0 and 1, inclusive
	public double saw(double hertz, double frame)
	{
		double period = sampleRate/hertz;
		frame %= period;
		return frame/period;
	}
	
	//Returns a value between 0 and 1, inclusive
	public double triangle(double hertz, double frame)
	{
		double period = sampleRate/hertz;
		frame %= period;
		double amp = 2 * frame/period;
		if(amp < 1)
			return amp;
		return 2-amp;
	}
	
}
