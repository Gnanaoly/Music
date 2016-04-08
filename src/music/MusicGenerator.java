package music;
import java.util.ArrayList;

import instrument.Bass;
import instrument.ChordProgression;
import instrument.Melody;
import instrument.Note;
import instrument.Rhythm;

public class MusicGenerator {

	private Tones toneGenerator;
	private MusicUtil util;
	private Waves.WaveType type = Waves.WaveType.sin;
	private int numChannels = 2;

	public MusicGenerator(int sampleRate, int bitsPerSample, int numChannels) {
		toneGenerator = new Tones(sampleRate, numChannels);
		util = new MusicUtil(sampleRate, bitsPerSample, numChannels);
	}

	public byte[] getMusic(Composer.Section sections[]) {
		double[][] music = null;
		double t = 0;
		for(int i = 0; i < sections.length; i++) {
			music = util.add(music,
					makeChorus(sections[i].progression, sections[i].rhythm, sections[i].melody, sections[i].bass, sections[i].time), 
					t, false);
			t += sections[i].time.secondsPerBeat * sections[i].time.beatsPerMeasure * sections[i].time.numMeasures;
		}
		return util.format(music);
	}
	
	private double[][] makeChorus(ChordProgression progression, Rhythm rhythm,
			Melody melody, Bass bass, Time time) {
		double[][] ret = new double[numChannels][0];
		ret = addProgression(ret, progression, 0, time);
		ret = addRhythm(ret, rhythm, 0, time);
		ret = addNotes(ret, melody, 0, time, Waves.WaveType.saw, 100);
		ret = addNotes(ret, bass, 0, time, Waves.WaveType.triangle, 200);
		return ret;
	}
	
	private double[][] addProgression(double[][] music, ChordProgression progression, double offsetSecs, Time time) {
		int beforeSwitch = 0;
		double[][] add = new double[numChannels][0];
		for(int c = 1; c < progression.size(); c++) {
			if(progression.get(c).equals(progression.get(c-1))) {
				beforeSwitch++;
			} else {
				for(double hz : progression.get(c-1).getTriadHz()) {
					add = util.add(add,
							toneGenerator.toneFlat(hz, time.secondsPerBeat * (beforeSwitch + 1), .01, .01, 100, type),
							time.secondsPerBeat * (c - beforeSwitch - 1),
							false);
				}
				beforeSwitch = 0;
			}
		}
		return util.add(music, add, offsetSecs, false);
	}
	
	private double[][] addRhythm(double[][] music, Rhythm rhythm, double offsetSecs, Time time) {
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
				add = util.add(add, tone, r * time.secondsPerSubdivide(), false);
			}
		}
		return util.add(music, add, offsetSecs, false);
	}
	
	private double[][] addNotes(double[][] music, ArrayList<Note> notes,
			double offsetSecs, Time time, Waves.WaveType type, int volume) {
		double[][] add = new double[numChannels][0];
		int t = 0;
		for(Note note : notes) {
			if(note.hz == 0) {
				
			} else {
				double[][] tone;
				double noteTime = note.lengthSubdivides * time.secondsPerSubdivide();
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
				add = util.add(add, tone, t * time.secondsPerSubdivide(), false);
			}
			t += note.lengthSubdivides;
		}
		return util.add(music, add, offsetSecs, false);
	}

}
