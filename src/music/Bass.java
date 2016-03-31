package music;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")
public class Bass extends ArrayList<Note> {
	
	ChordProgression progression;
	Melody melody;
	RandomUtil rand;
	
	private final double chanceRest = .05;
	
	public Bass(ChordProgression progression, Melody melody, Rhythm rhythm) {
		this.progression = progression;
		this.melody = melody;
		rand = new RandomUtil();
	}
	
	public void generateBass() {
		// Each chord in ChordProgression is a quarter note
		clear();
		ArrayList<Entry> targets = new ArrayList<Entry>();
		int time = 4;
		Chord prevChord = progression.get(0);
		for(Chord chord : progression) {
			if(chord.equals(prevChord)) {
				time += 4;
			} else {
				targets.add(new Entry(chord, prevChord, time));
				time = 4;
				prevChord = chord;
			}
		}
		targets.add(new Entry(progression.get(progression.size()-1), progression.get(progression.size()-1), time));
		
		double prevHz = putInRange(progression.get(0).getScaleNoteHz())[0];
		
		for (Entry entry : targets) {
			double[] triad = putInRange(entry.prevChord.getTriadHz());
			double[] scaleNotes = putInRange(entry.prevChord.getScaleNoteHz());
			double[] targetTriad = putInRange(entry.chord.getTriadHz());
			time = entry.time;
			while(time > 0) {
				scaleNotes = sortScaleNotes(scaleNotes, triad, targetTriad, prevHz);
				if(time == 1) {
					add(new Note(1, 0, false, false, prevHz));
					time -= 1;
				} else if(rand.nextDouble() < chanceRest) {
					int maxTime = Math.min(time, 4);
					int noteTime = rand.nextInt(maxTime - 1) + 2;
					time -= noteTime;
					add(new Note(noteTime, 0, false, false, prevHz));
				} else {
					int maxTime = Math.min(time, 8);
					int noteTime = rand.nextInt(maxTime - 1) + 2;
					time -= noteTime;
					Note note = new Note(noteTime, scaleNotes[rand.nextSkewed(scaleNotes.length, -.5)], false, false, prevHz);
					prevHz = note.hz;
					add(note);
				}
			}
		}
	}
	
	private int[] getRiff(Rhythm rhythm) {
		int[] riff = new int[rhythm.slotsPerMeasure];
		riff[0] = 0;
		int options[] = new int[]{0, 3, 5, 7};
		for(int i = 1; i < riff.length; i++) {
			if(rhythm.get(i).contains(Rhythm.Drum.kick)) {
				riff[i] = options[rand.nextInt(options.length)];
			} else {
				riff[i] = Integer.MAX_VALUE;
			}
		}
		int prev = riff[0];
		double probToDoSomething = .4;
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
				options = new int[Math.abs(next - prev)];
				int direction = (int) Math.signum(next - prev);
				if(direction == 1) options[0] = prev;
				else if(direction == -1) options[0] = next;
				for(int j = 1; j < options.length; j++) {
					options[j] = options[j-1] + direction;
				}
			}
		}
		return riff;
	}
	
	private double[] sortScaleNotes(double[] scaleNotes, double[] triad, double[] targetTriad, final double prevHz) {
		double[] ret = new double[scaleNotes.length];
		int p = 0;
		ArrayList<Double> sn = new ArrayList<Double>();
		for(int i = 0; i < scaleNotes.length; i++) {
			sn.add(scaleNotes[i]);
		}
		ret[p++] = prevHz;
		sn.remove(prevHz);
		for(int i = 0; i < triad.length; i++) {
			if(sn.contains(triad[i])) {
				ret[p++] = triad[i];
				sn.remove(triad[i]);
			}
		}
		Collections.sort(sn, new Comparator<Double>() {
			@Override
	        public int compare(Double d2, Double d1)
	        {
				double dd2 = Math.abs(d2 - prevHz);
				double dd1 = Math.abs(d1 - prevHz);
				if(dd2 > dd1) return 1;
				if(dd2 < dd1) return -1;
				return 0;
	        }
		});
		for(Double d : sn) {
			ret[p++] = d;
		}
		return ret;
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
	
	private class Entry {
		public Chord chord;
		public Chord prevChord;
		public int time;
		
		public Entry(Chord chord, Chord prevChord, int time) {
			this.chord = chord;
			this.prevChord = prevChord;
			this.time = time;
		}
	}

}
