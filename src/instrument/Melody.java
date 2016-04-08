package instrument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import music.RandomUtil;
import music.Time;

@SuppressWarnings("serial")
public class Melody extends ArrayList<Note> {
	
	RandomUtil rand;
	
	private final double chanceAccent = .25;
	
	//Complexity ranges 0 to 1
	public Melody(Time time, ChordProgression progression, double complexity) {
		rand = new RandomUtil();
		generateMelody(time, progression, complexity);
	}
	
	//complexity between 0 and 1
	private void generateMelody(Time time, ChordProgression progression, double complexity) {
		clear();
		double[] scaleNotes = progression.get(0).getScaleNoteHz();
		double prevHz = scaleNotes[0];
		for(int i = 0; i < progression.size(); i++) {
			double[] triad = progression.get(i).getTriadHz();
			int t = 0;
			while(t < time.subdividesPerBeat()) {
				scaleNotes = sortScaleNotes(scaleNotes, triad, prevHz);
				int noteTime = rand.nextInt(time.subdividesPerBeat() - t) + 1;
				t += noteTime;
				if(rand.nextDouble() < complexity) {
					Note note = new Note(noteTime, scaleNotes[rand.nextSkewed(scaleNotes.length, -.1)],
							rand.nextBoolean(), rand.nextDouble() < chanceAccent, prevHz);
					prevHz = note.hz;
					add(note);
				} else {
					add(new Note(noteTime, 0, false, false, prevHz));
				}
			}
		}
	}
	
	private double[] sortScaleNotes(double[] scaleNotes, double[] triad, final double prevHz) {
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
}
