package music;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Rhythm extends ArrayList<ArrayList<Rhythm.Drum>>{
	
	public final int slotsPerMeasure = 16;
	RandomUtil rand;
	
	public enum Drum {
		snare, hihat, kick, tom1, tom2, cymbal;
	}

	public Rhythm() {
		rand = new RandomUtil();
	}
	
	public void generateRhythm(int numMeasures) {
		clear();
		for(int i = 0; i < slotsPerMeasure; i++) {
			add(new ArrayList<Rhythm.Drum>());
		}
		drumSteady(Drum.kick, rand.nextInt(3) + 2, rand.nextInt(2));
		drumSteady(Drum.snare, rand.nextInt(6) + 2, rand.nextInt(2));
		drumSteady(Drum.hihat, rand.nextInt(8) + 2, rand.nextInt(2));
		expand(numMeasures);
	}
	
	//endIndex is not inclusive
	public void fill(int start, int end) {
		for(int i = start; i < end; i++) {
			get(i).clear();
			for(Drum drum : Drum.values()) {
				if(rand.nextBoolean()) get(i).add(drum);
			}
		}
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
	
	private void drumSteady(Drum drum, int freq, int start) {
		int spaceBetween = slotsPerMeasure / freq;
		for(int i = start; i < size(); i += spaceBetween)
			get(i).add(drum);
	}
	
}
