package instrument;

import music.Waves.WaveType;

public interface Instrument {
	
	public enum InstrumentType {
		Melody,
		Bass,
		Rhythm,
		ChordProgression
	}
	
	public InstrumentType getType();
	
	public WaveType getWaveType();
	
	public void setWaveType(WaveType type);
	
	public Instrument duplicate();
	
	public void complexify();
	
	public void setBalance(double[] balance);
	
	public double[] getBalance();
}
