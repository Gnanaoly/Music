package music;

public class Time {
	
	public double secondsPerBeat;
	public int beatsPerMeasure;
	public int subdivide;
	public int numMeasures;

	public Time(double secondsPerBeat, int beatsPerMeasure, int subdivide, int numMeasures) {
		this.secondsPerBeat = secondsPerBeat;
		this.beatsPerMeasure = beatsPerMeasure;
		this.subdivide = subdivide;
		this.numMeasures = numMeasures;
	}
	
	public double secondsPerSubdivide() {
		return (secondsPerBeat * beatsPerMeasure) / subdivide;
	}
	
	public int subdividesPerBeat() {
		return subdivide / beatsPerMeasure;
	}
	
}
