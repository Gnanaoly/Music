package instrument;
import java.util.ArrayList;

import music.RandomUtil;
import music.Time;

@SuppressWarnings("serial")
public class Rhythm extends ArrayList<ArrayList<Rhythm.Drum>>{
	
	RandomUtil rand;
	
	public enum Drum {
		snare, hihat, kick, tom1, tom2, cymbal;
	}

	public Rhythm(Time time) {
		rand = new RandomUtil();
		generateRhythm(time);
	}
	
	private void generateRhythm(Time time) {
		clear();
		for(int i = 0; i < time.subdivide; i++) {
			add(new ArrayList<Rhythm.Drum>());
		}
		drumSteady(Drum.kick, rand.nextInt(3) + 2, rand.nextInt(2), time);
		drumSteady(Drum.snare, rand.nextInt(6) + 2, rand.nextInt(2), time);
		drumSteady(Drum.hihat, rand.nextInt(8) + 2, rand.nextInt(2), time);
		expand(time.numMeasures);
	}
	
	//end is not inclusive
	public void fill(int start, int end) {
		for(int i = start; i < end; i++) {
			get(i).clear();
			for(Drum drum : Drum.values()) {
				if(rand.nextBoolean()) get(i).add(drum);
			}
		}
	}
	
	public void fillEnd() {
		fill(size()-3, size());
	}
	
	private void expand(int times) {
		int range = size();
		for(int i = 0; i < times - 1; i++) {
			for(int j = 0; j < range; j++) {
				ArrayList<Drum> drumList = new ArrayList<Drum>();
				for(Drum drum : get(j))
					drumList.add(drum);
				add(drumList);
			}
		}
	}
	
	private void drumSteady(Drum drum, int freq, int start, Time time) {
		int spaceBetween = time.subdivide / freq;
		for(int i = start; i < size(); i += spaceBetween)
			get(i).add(drum);
	}
	
}
