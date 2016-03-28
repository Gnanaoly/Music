import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")
public class Melody extends ArrayList<Melody.Note> {
	
	private ChordProgression progression;
	//TODO: take complexity and beatsPerMeasure into account
	private double complexity;
	private int beatsPerMeasure;
	RandomUtil rand;
	
	private final double chanceRest = .25;
	
	//Complexity ranges 0 to 1
	public Melody(ChordProgression progression, double complexity, int beatsPerMeasure) {
		this.progression = progression;
		this.complexity = complexity;
		this.beatsPerMeasure = beatsPerMeasure;
		rand = new RandomUtil();
	}
	
	public void generateMelody() {
		//Each chord in ChordProgression is a quarter note
		double[] scaleNotes = progression.get(0).getScaleNoteHz();
		double prevHz = scaleNotes[0];
		for(int i = 0; i < progression.size(); i++) {
			double[] triad = progression.get(i).getTriadHz();
			int time = 0;
			while(time < 4) {
				scaleNotes = sortScaleNotes(scaleNotes, triad, prevHz);
				int noteTime = rand.nextInt(4 - time) + 1;
				time += noteTime;
				if(rand.nextDouble() < chanceRest) {
					add(new Note(noteTime, 0, false, false, prevHz));
				} else {
					Note note = new Note(noteTime, scaleNotes[rand.nextSkewed(scaleNotes.length, .1)], rand.nextBoolean(), rand.nextBoolean(), prevHz);
					prevHz = note.hz;
					add(note);
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
	
	public class Note {
		
		public int lengthSixteenths;
		public double hz; //If hz is 0, this is a rest
		public boolean bendFromPrev;
		public boolean accent;
		public double prevHz;
		
		public Note(int lengthSixteenths, double hz, boolean bendFromPrev, boolean accent, double prevHz) {
			this.lengthSixteenths = lengthSixteenths;
			this.hz = hz;
			this.bendFromPrev = bendFromPrev;
			this.accent = accent;
			this.prevHz = prevHz;
		}
		
	}
}
