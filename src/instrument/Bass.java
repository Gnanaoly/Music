package instrument;
import java.util.ArrayList;

import music.RandomUtil;
import music.Time;

@SuppressWarnings("serial")
public class Bass extends ArrayList<Note> {
	
	RandomUtil rand;
	public int[] riff = null;
	
	public Bass(Time time, Rhythm rhythm, ChordProgression progression, Melody melody) {
		rand = new RandomUtil();
		generateBass(time, rhythm, progression, melody);
	}
	
	public Bass(Bass bass) {
		riff = bass.riff;
		rand = new RandomUtil();
		addAll(bass);
	}
	
	private void generateBass(Time time, Rhythm rhythm, ChordProgression progression, Melody melody) {
		riff = getRiff(time, rhythm);
		Note note = new Note();
		for(int i = 0; i < time.numMeasures; i++) {
			for(int j = 0; j < riff.length; j++) {
				if(riff[j] != Integer.MAX_VALUE) {
					note.hz = putInRange(progression
							.get((j*time.beatsPerMeasure/time.subdivide)+i*time.beatsPerMeasure)
							.getScaleNoteHz())[riff[j]];
					add(note);
					note = new Note();
					note.lengthSubdivides = 1;
				}
				else {
					note.lengthSubdivides++;
				}
			}
		}
		note.hz = putInRange(progression.get(progression.size()-1).getScaleNoteHz())[0];
		add(note);
	}
	
	private int[] getRiff(Time time, Rhythm rhythm) {
		int[] riff = new int[time.subdivide];
		riff[0] = 0;
		int options[] = new int[]{0, 2, 4, 6};
		for(int i = 1; i < riff.length; i++) {
			if(rhythm.get(i).contains(Rhythm.Drum.kick)) {
				riff[i] = options[rand.nextSkewed(options.length, .5)];
			} else {
				riff[i] = Integer.MAX_VALUE;
			}
		}
		return riff;
	}
	
	public void complexifyRiff() {
		int prev = riff[0];
		double probToDoSomething = .2;
		for(int i = 1; i < riff.length; i++) {
			if(riff[i] == Integer.MAX_VALUE && i % 2 == 0 && rand.nextDouble() < probToDoSomething) {
				int next = -1;
				for(int j = i; j < riff.length; j++) {
					if(riff[j] != Integer.MAX_VALUE) {
						next = j;
						break;
					}
				}
				if(next == -1) {
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
					options = new int[]{rand.nextInt(7)};
				}
				riff[i] = options[rand.nextSkewed(options.length, -1)];
			}
		}
	}
	
	private final int baseHz = 50;
	
	private double[] putInRange(double[] in) {
		for(int i = 0; i < in.length; i++) {
			while(in[i] > baseHz * 2) {
				in[i] /= 2;
			}
			while(in[i] < baseHz && in[i] > 0) {
				in[i] *= 2;
			}
		}
		return in;
	}

}
