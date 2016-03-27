import java.util.ArrayList;

@SuppressWarnings("serial")
public class Rhythm extends ArrayList<ArrayList<Rhythm.Drum>>{
	
	public int slotsPerMeasure;
	RandomUtil rand;
	
	public enum Drum {
		snare, hihat, kick, tom1, tom2, cymbol;
	}

	public Rhythm(int slotsPerMeasure) {
		this.slotsPerMeasure = slotsPerMeasure;
		rand = new RandomUtil();
		for(int i = 0; i < slotsPerMeasure; i++) {
			add(new ArrayList<Rhythm.Drum>());
		}
	}
	
	public void generateRhythm() {
		drumSteady(Drum.kick, 4, true);
		drumSteady(Drum.snare, 2, true);
		drumSteady(Drum.hihat, 8, false);
	}
	
	//If early, hits first beat, otherwise doesn't.
	private void drumSteady(Drum drum, int freq, boolean early) {
		int spaceBetween = size() / freq;
		int start;
		if(early) start = 0;
		else start = spaceBetween / 2;
		for(int i = start; i < size(); i += spaceBetween)
			get(i).add(drum);
	}
	
}
