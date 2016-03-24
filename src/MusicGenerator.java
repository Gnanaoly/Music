
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
		ChordProgression progression = new ChordProgression(200, Chord.ChordType.Major);
		progression.generateProgression(4, 12, 1, 1);
		progression.add(new Chord(200, Chord.ChordType.Major));
		//for(int c = 0; c < progression.size(); c++) {
		//	for(double hz : progression.get(c).getTriadHz()) {
		//		music = util.add(music, toneGenerator.toneFlat(hz, .5, .01, .01, 100, type), .5 * c);
		//	}
		//}
		music = addProgression(music, progression, 0);
		return util.format(music);
	}
	
	private double[][] addProgression(double[][] music, ChordProgression progression, double offsetSecs) {
		double secsPerBeat = .5;
		int beforeSwitch = 0;
		double[][] add = new double[music.length][0];
		for(int c = 1; c < progression.size(); c++) {
			if(progression.get(c).equals(progression.get(c-1))) {
				beforeSwitch++;
			} else {
				for(double hz : progression.get(c-1).getTriadHz()) {
					add = util.add(add,
							toneGenerator.toneFlat(hz, secsPerBeat * (beforeSwitch + 1), .01, .01, 100, type),
							secsPerBeat * (c - beforeSwitch - 1),
							false);
				}
				beforeSwitch = 0;
			}
			add = util.add(add,
					toneGenerator.drumHit(100), 
					secsPerBeat * c,
					false);
		}
		return util.add(music, add, offsetSecs, false);
	}

}
