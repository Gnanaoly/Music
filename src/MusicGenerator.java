
public class MusicGenerator {

	private Tones toneGenerator;
	private MusicUtil util;
	private Waves.WaveType type = Waves.WaveType.sin;

	public MusicGenerator(int sampleRate, int bitsPerSample, int numChannels) {
		toneGenerator = new Tones(sampleRate, numChannels);
		util = new MusicUtil(sampleRate, bitsPerSample, numChannels);
	}

	public byte[] getMusic() {
		// return format(add(toneGenerator.toneFlat(440, 2, .1, 1, 100,
		// Waves.WaveType.sin),
		// toneGenerator.toneFlat(261.25, 60, 1, 10, 100, Waves.WaveType.sin)));
		/*
		 * double[][] AtoC = toneGenerator.toneBend(261.25, 440, 2, 0, 0, 100,
		 * type); double[][] A = toneGenerator.toneFlat(261.25, 1, .1, 0, 100,
		 * type); double[][] C = toneGenerator.toneFlat(440, 1, 0, 0, 100,
		 * type); double[][] CtoN = toneGenerator.toneBend(440, 400, 2, 0, 0,
		 * 100, type); double[][] N = toneGenerator.toneFlat(400, 1, 0, .1, 100,
		 * type);
		 */
		// return format(add(add(add(add(A, AtoC, 1), C, 3), CtoN, 4), N, 6));
		double[][] music = new double[0][0];
		double secsPerBeat = .5;
		ChordProgression progression = new ChordProgression(200, Chord.ChordType.Major);
		progression.generateProgression(4, 12, 1, 1);
		music = addProgression(music, progression, 0, secsPerBeat);
		Rhythm rhythm = new Rhythm(8);
		rhythm.generateRhythm();
		for(int i = 0; i < 12; i++) {
			music = addRhythm(music, rhythm, i*2, secsPerBeat / 2);
		}
		return util.format(music);
	}
	
	private double[][] addProgression(double[][] music, ChordProgression progression, double offsetSecs, double secsPerSlot) {
		int beforeSwitch = 0;
		double[][] add = new double[music.length][0];
		for(int c = 1; c < progression.size(); c++) {
			if(progression.get(c).equals(progression.get(c-1))) {
				beforeSwitch++;
			} else {
				for(double hz : progression.get(c-1).getTriadHz()) {
					add = util.add(add,
							toneGenerator.toneFlat(hz, secsPerSlot * (beforeSwitch + 1), .01, .01, 100, type),
							secsPerSlot * (c - beforeSwitch - 1),
							false);
				}
				beforeSwitch = 0;
			}
		}
		return util.add(music, add, offsetSecs, false);
	}
	
	private double[][] addRhythm(double[][] music, Rhythm rhythm, double offsetSecs, double secsPerSlot) {
		double[][] add = new double[music.length][0];
		for(int r = 0; r < rhythm.size(); r++) {
			for(int drum = 0; drum < rhythm.get(r).size(); drum++) {
				double[][] tone = new double[music.length][];
				switch(rhythm.get(r).get(drum)) {
				case snare:
					tone = toneGenerator.snare(100);
					break;
				case hihat:
					tone = toneGenerator.hihat(100);
					break;
				case kick:
					tone = toneGenerator.drum(70, 300);
					break;
				default:
					break;
				}
				add = util.add(add, tone, r * secsPerSlot, false);
			}
		}
		return util.add(music, add, offsetSecs, false);
	}

}
