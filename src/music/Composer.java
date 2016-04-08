package music;

import instrument.Bass;
import instrument.ChordProgression;
import instrument.Melody;
import instrument.Rhythm;

public class Composer {
	
	MusicGenerator generator;
	RandomUtil rand;
	
	public Composer(MusicGenerator generator) {
		this.generator = generator;
		this.rand = new RandomUtil();
	}
	
	public byte[] compose() {
		
		double secondsPerBeat = rand.nextDouble() + .2;
		double chanceToChangeTime = .25;
		
		Chord mainTonic = new Chord(rand.nextInt(100) + 100,
				Chord.ChordType.values()[rand.nextInt(Chord.ChordType.values().length)]);
		
		Section sections[] = new Section[rand.nextInt(5) + 3];
		for(int i = 0; i < sections.length; i++) {
			int id = rand.nextInt(sections.length / 2 + 1);
			for(int j = 0; j < i; j++) {
				if(sections[j].id == id) {
					sections[i] = sections[j].duplicate();
					sections[i].complexify();
				}
			}
			if(sections[i] == null) {
				int measuresPerSection = rand.nextEven(8, 24);
				int beatsPerMeasure = rand.nextInt(7) + 3;
				int subdivide = beatsPerMeasure * (rand.nextInt(3) + 3);
				double spb = secondsPerBeat;
				if(rand.nextDouble() < chanceToChangeTime) {
					spb = rand.nextDouble() + .2;
				}
				Time time = new Time(spb, beatsPerMeasure, subdivide, measuresPerSection);
				
				sections[i] = new Section(id);
				sections[i].time = time;
				sections[i].rhythm = new Rhythm(sections[i].time);
				sections[i].rhythm.fillEnd();
				sections[i].tonic = new Chord(mainTonic.getScaleNoteHz()[rand.nextInt(mainTonic.getScaleNoteHz().length)],
						Chord.ChordType.values()[rand.nextInt(Chord.ChordType.values().length)]);
				sections[i].progression = new ChordProgression(sections[i].time, sections[i].tonic, rand.nextDouble() * sections[i].time.beatsPerMeasure, rand.nextInt(8));
				sections[i].melody = new Melody(sections[i].time, sections[i].progression, 1-(rand.nextDouble()/4));
				sections[i].bass = new Bass(sections[i].time, sections[i].rhythm, sections[i].progression, sections[i].melody);
			}
		}
		
		return generator.getMusic(sections);
	}
	
	public class Section {
		
		public int id;
		public Rhythm rhythm;
		public Melody melody;
		public Chord tonic;
		public ChordProgression progression;
		public Bass bass;
		public Time time;
		
		public Section(int id) {
			this.id = id;
		}
		
		public void complexify() {
			bass.complexifyRiff();
		}
		
		public Section duplicate() {
			Section section = new Section(id);
			section.rhythm = rhythm;
			section.melody = melody;
			section.tonic = tonic;
			section.progression = progression;
			section.bass = new Bass(bass);
			section.time = time;
			return section;
		}
		
	}

}
