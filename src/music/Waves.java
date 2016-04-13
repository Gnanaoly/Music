package music;
import java.util.Random;

import phoneme.PhonWaves;

public class Waves {
	
	private int sampleRate;
	private Random rand;
	private PhonWaves phonWaves;
	
	public enum WaveType {
		sin,
		square,
		saw,
		triangle,
		iy,
		ih,
		ey,
		eh,
		ae,
		aa,
		ao,
		ow,
		uh,
		uw,
		er,
		ax,
		ah,
		ay,
		aw1,
		aw2,
		oy1,
		oy2
	}

	public Waves(int sampleRate)
	{
		this.sampleRate = sampleRate;
		rand = new Random();
		this.phonWaves = new PhonWaves(sampleRate);
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
		case iy: return phonWaves.iy(hertz, frame);
		case ih: return phonWaves.ih(hertz, frame);
		case ey: return phonWaves.ey(hertz, frame);
		case eh: return phonWaves.eh(hertz, frame);
		case ae: return phonWaves.ae(hertz, frame);
		case aa: return phonWaves.aa(hertz, frame);
		case ao: return phonWaves.ao(hertz, frame);
		case ow: return phonWaves.ow(hertz, frame);
		case uh: return phonWaves.uh(hertz, frame);
		case uw: return phonWaves.uw(hertz, frame);
		case er: return phonWaves.er(hertz, frame);
		case ax: return phonWaves.ax(hertz, frame);
		case ah: return phonWaves.ah(hertz, frame);
		case ay: return phonWaves.ay(hertz, frame);
		case aw1: return phonWaves.aw1(hertz, frame);
		case aw2: return phonWaves.aw2(hertz, frame);
		case oy1: return phonWaves.oy1(hertz, frame);
		case oy2: return phonWaves.oy2(hertz, frame);

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
