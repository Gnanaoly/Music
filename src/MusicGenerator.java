
public class MusicGenerator {

	private Tones toneGenerator;
	private MusicUtil util;

	public MusicGenerator(int sampleRate, int bitsPerSample, int numChannels) {
		toneGenerator = new Tones(sampleRate, numChannels);
		util = new MusicUtil(sampleRate, bitsPerSample, numChannels);
	}

	public byte[] getMusic() {
		// return format(add(toneGenerator.toneFlat(440, 2, .1, 1, 100,
		// Waves.WaveType.sin),
		// toneGenerator.toneFlat(261.25, 60, 1, 10, 100, Waves.WaveType.sin)));
		Waves.WaveType type = Waves.WaveType.sin;
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
		for(int c = 0; c < progression.size(); c++) {
			for(double hz : progression.get(c).getTriadHz()) {
				music = util.add(music, toneGenerator.toneFlat(hz, 2, .01, .01, 100, type), 2 * c);
			}
		}
		return util.format(music);
	}

}
