package composition;

import instrument.Bass;
import instrument.Chord;
import instrument.ChordProgression;
import instrument.Instrument;
import instrument.Melody;
import instrument.Rhythm;
import instrument.Instrument.InstrumentType;
import music.MusicGenerator;
import music.RandomUtil;
import music.Waves;
import music.Waves.WaveType;

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
		for (int i = 0; i < sections.length; i++) {
			int id = rand.nextInt(sections.length / 2 + 1);
			for (int j = 0; j < i; j++) {
				if (sections[j].id == id) {
					sections[i] = sections[j].duplicate();
					sections[i].startVolume = rand.nextInt(100) + 50;
					sections[i].endVolume = rand.nextInt(100) + 50;
					sections[i].complexify();
				}
			}
			if (sections[i] == null) {
				int measuresInSection = rand.nextEven(8, 16);
				int beatsPerMeasure = rand.nextInt(7) + 3;
				int subdivide = beatsPerMeasure * (rand.nextInt(3) + 3);
				double spb = secondsPerBeat;
				if (rand.nextDouble() < chanceToChangeTime) {
					spb = rand.nextDouble() + .2;
				}
				Time time = new Time(spb, beatsPerMeasure, subdivide, measuresInSection);

				sections[i] = new Section(id);
				sections[i].time = time;
				Rhythm rhythm = new Rhythm(sections[i].time);
				rhythm.fillEnd();
				sections[i].addInstrument(rhythm);
				Chord tonic = new Chord(mainTonic.getScaleNoteHz()[rand.nextInt(mainTonic.getScaleNoteHz().length)],
						Chord.ChordType.values()[rand.nextInt(Chord.ChordType.values().length)]);
				sections[i].addInstrument(new ChordProgression(sections[i].time, tonic,
						rand.nextDouble() * sections[i].time.beatsPerMeasure, rand.nextInt(8)));
				sections[i].addInstrument(new Melody(sections[i].time,
						(ChordProgression) sections[i].getInstrument(InstrumentType.ChordProgression),
						1 - (rand.nextDouble() / 4)));
				sections[i].addInstrument(
						new Bass(sections[i].time, (Rhythm) sections[i].getInstrument(InstrumentType.Rhythm),
								(ChordProgression) sections[i].getInstrument(InstrumentType.ChordProgression),
								(Melody) sections[i].getInstrument(InstrumentType.Melody)));

				WaveType instWaves[] = new Waves.WaveType[] { WaveType.sin, WaveType.saw, WaveType.square,
						WaveType.triangle, WaveType.aa };
				for (Instrument instrument : sections[i].getInstruments()) {
					instrument.setWaveType(instWaves[rand.nextInt(instWaves.length)]);
				}
				sections[i].startVolume = rand.nextInt(100) + 50;
				sections[i].endVolume = rand.nextInt(100) + 50;
				sections[i].randomizeBalances();
			}
		}

		return generator.getMusic(sections);
	}

}
