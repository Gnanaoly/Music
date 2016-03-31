package music;
import java.util.ArrayList;

public class MusicGenerator {

	private Tones toneGenerator;
	private MusicUtil util;
	private Waves.WaveType type = Waves.WaveType.sin;
	private int numChannels = 2;

	public MusicGenerator(int sampleRate, int bitsPerSample, int numChannels) {
		toneGenerator = new Tones(sampleRate, numChannels);
		util = new MusicUtil(sampleRate, bitsPerSample, numChannels);
	}

	public byte[] getMusic() {
		double secsPerBeat = .5;
		int measuresPerSection = 8;
		ChordProgression progression = new ChordProgression(200, Chord.ChordType.Major);
		progression.generateProgression(4, measuresPerSection, 1, 1);
		
		Rhythm rhythm = new Rhythm();
		rhythm.generateRhythm(measuresPerSection);
		rhythm.fill(rhythm.size() - 8, rhythm.size());
		
		Melody melody = new Melody(progression, 1, 4);
		melody.generateMelody();
		
		Bass bass = new Bass(progression, melody, rhythm);
		bass.generateBass();
		double[][] music = makeChorus(progression, rhythm, melody, bass, secsPerBeat);
		
		bass.generateBass();
		double[][] music2 = makeChorus(progression, rhythm, melody, bass, secsPerBeat);
		music = util.add(music, music2, secsPerBeat * 4 * measuresPerSection, false);
		
		bass.generateBass();
		music2 = makeChorus(progression, rhythm, melody, bass, secsPerBeat);
		
		progression = new ChordProgression(200, Chord.ChordType.Major);
		progression.generateProgression(4, measuresPerSection, 1, 1);
		melody = new Melody(progression, 1, 4);
		melody.generateMelody();
		rhythm.generateRhythm(measuresPerSection);
		rhythm.fill(rhythm.size() - 8, rhythm.size());
		bass = new Bass(progression, melody, rhythm);
		bass.generateBass();
		double[][] music3 = makeChorus(progression, rhythm, melody, bass, secsPerBeat);
		music = util.add(music, music3, secsPerBeat * 4 * measuresPerSection * 2, false);
		
		music = util.add(music, music2, secsPerBeat * 4 * measuresPerSection * 3, false);
		
		return util.format(music);
	}
	
	private double[][] makeChorus(ChordProgression progression, Rhythm rhythm,
			Melody melody, Bass bass, double secsPerBeat) {
		double[][] ret = new double[numChannels][0];
		ret = addProgression(ret, progression, 0, secsPerBeat);
		ret = addRhythm(ret, rhythm, 0, secsPerBeat / 4);
		ret = addNotes(ret, melody, 0, secsPerBeat, Waves.WaveType.saw, 100);
		ret = addNotes(ret, bass, 0, secsPerBeat, Waves.WaveType.triangle, 200);
		return ret;
	}
	
	private double[][] addProgression(double[][] music, ChordProgression progression, double offsetSecs, double secsPerSlot) {
		int beforeSwitch = 0;
		double[][] add = new double[numChannels][0];
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
		double[][] add = new double[numChannels][0];
		for(int r = 0; r < rhythm.size(); r++) {
			for(int drum = 0; drum < rhythm.get(r).size(); drum++) {
				double[][] tone = new double[numChannels][];
				switch(rhythm.get(r).get(drum)) {
				case snare:
					tone = toneGenerator.snare(100);
					break;
				case hihat:
					tone = toneGenerator.hihat(100);
					break;
				case kick:
					tone = toneGenerator.drum(70, 400);
					break;
				case tom1:
					tone = toneGenerator.drum(200, 400);
					break;
				case tom2:
					tone = toneGenerator.drum(150, 400);
					break;
				default:
					tone = new double[numChannels][0];
					break;
				}
				add = util.add(add, tone, r * secsPerSlot, false);
			}
		}
		return util.add(music, add, offsetSecs, false);
	}
	
	private double[][] addNotes(double[][] music, ArrayList<Note> notes,
			double offsetSecs, double secsPerBeat, Waves.WaveType type, int volume) {
		double[][] add = new double[numChannels][0];
		int time = 0;
		double secsPerTime = secsPerBeat / 4;
		for(Note note : notes) {
			if(note.hz == 0) {
				
			} else {
				double[][] tone;
				double noteTime = note.lengthSixteenths * secsPerTime;
				int vol = volume;
				if(note.accent) vol *= 1.2;
				if(note.bendFromPrev) {
					tone = toneGenerator.toneBend(note.prevHz, note.hz, noteTime * .2,
							.01, 0, vol, type);
					double[][] tone2 = toneGenerator.toneFlat(note.hz, noteTime * .8,
							0, .01, vol, type);
					tone = util.add(tone, tone2, noteTime * .2, true);
				} else {
					tone = toneGenerator.toneFlat(note.hz, noteTime, .01, .01, vol, type);
				}
				add = util.add(add, tone, time * secsPerTime, false);
			}
			time += note.lengthSixteenths;
		}
		return util.add(music, add, offsetSecs, false);
	}

}
