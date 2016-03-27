import java.util.Random;

public class Waves {
	
	private int sampleRate;
	private Random rand;
	
	public enum WaveType {
		sin,
		square,
		saw,
		triangle;
	}

	public Waves(int sampleRate)
	{
		this.sampleRate = sampleRate;
		rand = new Random();
	}
	
	//Returns a value between -1 and 1, inclusive. Returns 0 for frame = 0.
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
	
	public double sin(double hertz, double frame)
	{
		return Math.sin(hertz * frame *(2 * Math.PI / sampleRate));
	}
	
	public double square(double hertz, double frame)
	{
		if(sin(hertz, frame) > 0)
			return 1;
		return -1;
	}
	
	public double saw(double hertz, double frame)
	{
		double period = sampleRate/hertz;
		frame %= period;
		double amp = frame/period * 2;
		if(amp < 1)
			return amp;
		return amp - 2;
	}
	
	
	public double triangle(double hertz, double frame)
	{
		double period = sampleRate/hertz;
		frame %= period;
		double amp = 4 * frame/period;
		if(amp < 1)
			return amp;
		if(amp < 3)
			return 2-amp;
		return -4+amp;
	}
	
	public double rand() {
		return rand.nextDouble() * 2 - 1;
	}
	
}
