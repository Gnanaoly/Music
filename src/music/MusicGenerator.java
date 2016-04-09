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

	public MusicGenerator(int sampleRate, int bitsPerSample, int numChannels) {
		toneGenerator = new Tones(sampleRate, numChannels);
		util = new MusicUtil(sampleRate, bitsPerSample, numChannels);
	}

	public byte[] getMusic(final Composer.Section sections[]) {
		@SuppressWarnings("unchecked")
		final ArrayList<double[]>[] secs = (ArrayList<double[]>[])new ArrayList[sections.length];
		Thread[] threads = new Thread[sections.length];
		for(int i = 0; i < sections.length; i++) {
			final int num = i;
			threads[i] = new Thread(){
				public void run() {
					secs[num] = makeChorus(sections[num].progression, sections[num].rhythm, sections[num].melody, sections[num].bass, sections[num].time);
				}
			};
			threads[i].start();
		}
		for(int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Done doing parallel stuff");
		
		ArrayList<double[]> music = new ArrayList<double[]>();
		double t = 0;
		for(int i = 0; i < sections.length; i++) {
			util.add(music, secs[i], t, false);
			t += sections[i].time.secondsPerBeat * sections[i].time.beatsPerMeasure * sections[i].time.numMeasures;
		}
		return util.format(music);
	}
	
	private ArrayList<double[]> makeChorus(ChordProgression progression, Rhythm rhythm,
			Melody melody, Bass bass, Time time) {
		ArrayList<double[]> ret = new ArrayList<double[]>();
		addProgression(ret, progression, 0, time);
		addRhythm(ret, rhythm, 0, time);
		addNotes(ret, melody, 0, time, Waves.WaveType.saw, 100);
		addNotes(ret, bass, 0, time, Waves.WaveType.triangle, 200);
		return ret;
	}
	
	private void addProgression(ArrayList<double[]> music, ChordProgression progression, double offsetSecs, Time time) {
		int beforeSwitch = 0;
		ArrayList<double[]> add = new ArrayList<double[]>();
		for(int c = 1; c < progression.size(); c++) {
			if(c != progression.size()-1 && progression.get(c).equals(progression.get(c-1))) {
				beforeSwitch++;
			} else {
				for(double hz : progression.get(c-1).getTriadHz()) {
					util.add(add,
							toneGenerator.toneFlat(hz, time.secondsPerBeat * (beforeSwitch + 1), .01, .01, 100, type),
							time.secondsPerBeat * (c - beforeSwitch - 1),
							false);
				}
				beforeSwitch = 0;
			}
		}
		util.add(music, add, offsetSecs, false);
	}
	
	private void addRhythm(ArrayList<double[]> music, Rhythm rhythm, double offsetSecs, Time time) {
		ArrayList<double[]> add = new ArrayList<double[]>();
		for(int r = 0; r < rhythm.size(); r++) {
			for(int drum = 0; drum < rhythm.get(r).size(); drum++) {
				ArrayList<double[]> tone;
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
					tone = new ArrayList<double[]>();
					break;
				}
				util.add(add, tone, r * time.secondsPerSubdivide(), false);
			}
		}
		util.add(music, add, offsetSecs, false);
	}
	
	private void addNotes(ArrayList<double[]> music, ArrayList<Note> notes,
			double offsetSecs, Time time, Waves.WaveType type, int volume) {
		ArrayList<double[]> add = new ArrayList<double[]>();
		int t = 0;
		for(Note note : notes) {
			if(note.hz == 0) {
				
			} else {
				ArrayList<double[]> tone;
				double noteTime = note.lengthSubdivides * time.secondsPerSubdivide();
				int vol = volume;
				if(note.accent) vol *= 1.2;
				if(note.bendFromPrev) {
					tone = toneGenerator.toneBend(note.prevHz, note.hz, noteTime * .2,
							.01, 0, vol, type);
					util.add(tone, toneGenerator.toneFlat(note.hz, noteTime * .8,
							0, .01, vol, type), noteTime * .2, true);
				} else {
					tone = toneGenerator.toneFlat(note.hz, noteTime, .01, .01, vol, type);
				}
				util.add(add, tone, t * time.secondsPerSubdivide(), false);
			}
			t += note.lengthSubdivides;
		}
		util.add(music, add, offsetSecs, false);
	}

}
