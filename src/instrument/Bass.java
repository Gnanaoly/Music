package instrument;

import java.util.ArrayList;

import composition.Time;
import music.RandomUtil;
import music.Waves.WaveType;

@SuppressWarnings("serial")
public class Bass extends ArrayList<Note> implements Instrument {

	RandomUtil rand;
	private int[] riff = null;
	private WaveType waveType;

	public Bass(Time time, ChordProgression progression, Rhythm rhythm, WaveType waveType) {
		rand = new RandomUtil();
		this.waveType = waveType;
		generateBass(time, rhythm, progression);
	}

	public Bass(Bass bass) {
		riff = bass.riff;
		rand = new RandomUtil();
		addAll(bass);
		waveType = bass.getWaveType();
	}

	private void generateBass(Time time, Rhythm rhythm, ChordProgression progression) {
		riff = getRiff(time, rhythm);
		complexify();
		
		Note note = new Note();
		for (int i = 0; i < time.subdivide * time.numMeasures; i++) {
			note.lengthSubdivides++;
			if (riff[i % riff.length] != Integer.MAX_VALUE && (note.hz.length == 0
					|| note.hz[0] != progression.getChordAtSubdivide(i).getScaleNoteHz()[riff[i%riff.length]])) {
				add(note);
				note = new Note();
				note.hz = putInRange(new double[]{progression.getChordAtSubdivide(i).getScaleNoteHz()[riff[i%riff.length]]});
			} else if (riff[i % riff.length] == Integer.MAX_VALUE && note.hz.length != 0) {
				add(note);
				note = new Note();
			}
		}
	}

	private int[] getRiff(Time time, Rhythm rhythm) {
		int[] riff = new int[time.subdivide];
		riff[0] = 0;
		int options[] = new int[] { 0, 2, 4, 6 };
		for (int i = 1; i < riff.length; i++) {
			if (rhythm.get(i).contains(Rhythm.Drum.kick)) {
				riff[i] = options[rand.nextSkewed(options.length, .5)];
			} else {
				riff[i] = Integer.MAX_VALUE;
			}
		}
		return riff;
	}

	public void complexify() {
		int prev = riff[0];
		double probToDoSomething = .5;
		for (int i = 1; i < riff.length; i++) {
			if (riff[i] == Integer.MAX_VALUE && i % 2 == 0 && rand.nextDouble() < probToDoSomething) {
				int next = -1;
				for (int j = i; j < riff.length; j++) {
					if (riff[j] != Integer.MAX_VALUE) {
						next = j;
						break;
					}
				}
				if (next == -1) {
					next = 0;
				}
				int[] options = new int[Math.abs(next - prev)];
				if (options.length > 0) {
					int direction = (int) Math.signum(next - prev);
					if (direction == 1)
						options[0] = prev;
					else if (direction == -1)
						options[0] = next;
					for (int j = 1; j < options.length; j++) {
						options[j] = options[j - 1] + direction;
					}
				} else {
					options = new int[] { rand.nextInt(7) };
				}
				riff[i] = options[rand.nextSkewed(options.length, -1)];
			}
		}
		for(int i = 0; i < riff.length; i++) {
			riff[i] %= 7;
		}
	}

	private final int baseHz = 50;

	private double[] putInRange(double[] in) {
		for (int i = 0; i < in.length; i++) {
			while (in[i] > baseHz * 2) {
				in[i] /= 2;
			}
			while (in[i] < baseHz && in[i] > 0) {
				in[i] *= 2;
			}
		}
		return in;
	}

	@Override
	public InstrumentType getType() {
		return InstrumentType.Bass;
	}

	@Override
	public Instrument duplicate() {
		Bass bass = new Bass(this);
		bass.setBalance(getBalance());
		return bass;
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
