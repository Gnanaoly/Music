package instrument;

import java.util.ArrayList;
import java.util.Arrays;

import composition.Time;
import music.RandomUtil;
import music.Waves.WaveType;

@SuppressWarnings("serial")
public class Keys extends ArrayList<Note> implements Instrument {

	Time time;
	RandomUtil rand;
	private boolean[] pattern;
	private WaveType waveType;

	public Keys(Time time, ChordProgression progression, double complexity, WaveType waveType) {
		this.time = time;
		this.waveType = waveType;
		rand = new RandomUtil();
		genPattern(complexity);
		genKeys(progression);
	}

	private void genKeys(ChordProgression progression) {
		Note note = new Note();
		for (int i = 0; i < time.subdivide * time.numMeasures; i++) {
			note.lengthSubdivides++;
			if (pattern[i % pattern.length]
					&& !Arrays.equals(note.hz, progression.getChordAtSubdivide(i).getTriadHz())) {
				add(note);
				note = new Note();
				note.hz = progression.getChordAtSubdivide(i).getTriadHz();
			} else if (!pattern[i % pattern.length] && note.hz.length != 0) {
				add(note);
				note = new Note();
			}
		}
	}

	private void genPattern(double complexity) {
		pattern = new boolean[time.subdivide];
		int mostRecent = 1;
		for (int i = 0; i < time.subdivide; i++) {
			double prob = complexity;
			if (mostRecent == 1)
				prob += (1 - prob) / 2; // more likely if played previous note
			else if (mostRecent == 2)
				prob /= 2; // less likely if played two notes ago
			pattern[i] = rand.nextDouble() < prob;
			if (pattern[i])
				mostRecent = 0;
			mostRecent++;
		}
	}

	@Override
	public InstrumentType getType() {
		return InstrumentType.Keys;
	}

	@Override
	public Instrument duplicate() {
		return this;
	}

	@Override
	public void complexify() {
		// TODO Auto-generated method stub
	}

	@Override
	public WaveType getWaveType() {
		return waveType;
	}

	@Override
	public void setWaveType(WaveType type) {
		waveType = type;
	}

	private double[] balance;

	@Override
	public void setBalance(double[] balance) {
		this.balance = balance;
	}

	@Override
	public double[] getBalance() {
		return balance;
	}

}
