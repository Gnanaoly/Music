package music;

import java.util.ArrayList;

import composition.Section;
import composition.Time;
import instrument.ChordProgression;
import instrument.Instrument;
import instrument.Note;
import instrument.Rhythm;

public class MusicGenerator {

	private Tones toneGenerator;
	private MusicUtil util;
	private int numChannels;

	public MusicGenerator(int sampleRate, int bitsPerSample, int numChannels) {
		toneGenerator = new Tones(sampleRate, numChannels);
		util = new MusicUtil(sampleRate, bitsPerSample, numChannels);
		this.numChannels = numChannels;
	}

	public byte[] getMusic(final Section sections[]) {
		@SuppressWarnings("unchecked")
		final ArrayList<double[]>[] secs = (ArrayList<double[]>[]) new ArrayList[sections.length];
		Thread[] threads = new Thread[sections.length];
		for (int i = 0; i < sections.length; i++) {
			final int num = i;
			threads[i] = new Thread() {
				public void run() {
					secs[num] = makeChorus(sections[num]);
				}
			};
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Done doing parallel stuff");

		ArrayList<double[]> music = new ArrayList<double[]>();
		double t = 0;
		for (int i = 0; i < sections.length; i++) {
			util.add(music, secs[i], t);
			t += sections[i].time.secondsPerBeat * sections[i].time.beatsPerMeasure * sections[i].time.numMeasures;
			//Avoid running out of heap space:
			secs[i] = null;
			sections[i] = null;
		}
		return util.format(music);
	}

	private ArrayList<double[]> makeChorus(Section section) {
		ArrayList<double[]> ret = new ArrayList<double[]>();
		for(Instrument instrument : section.getInstruments()) {
			switch(instrument.getType()) {
			case Melody:
			case Bass:
				addNotes(ret, instrument, 0, section.time, section);
				break;
			case Rhythm:
				addRhythm(ret, (Rhythm) instrument, 0, section.time, section);
				break;
			case ChordProgression:
				addProgression(ret, (ChordProgression) instrument, 0, section.time, section);
				break;
			default:
				break;
			}
		}
		return ret;
	}

	private void addProgression(ArrayList<double[]> music, ChordProgression progression, double offsetSecs, Time time,
			Section section) {
		int beforeSwitch = 0;
		ArrayList<double[]> add = new ArrayList<double[]>();
		for (int c = 1; c < progression.size(); c++) {
			double[] vol = progression.getBalance().clone();
			for(int i = 0; i < vol.length; i++) {
				vol[i] *= c * (section.endVolume - section.startVolume) / (time.beatsPerMeasure * time.numMeasures)
						+ section.startVolume;
			}
			if (c != progression.size() - 1 && progression.get(c).equals(progression.get(c - 1))) {
				beforeSwitch++;
			} else {
				for (double hz : progression.get(c - 1).getTriadHz()) {
					util.add(add, toneGenerator.toneFlat(hz, time.secondsPerBeat * (beforeSwitch + 1), .01, .01, vol,
							progression.getWaveType()), time.secondsPerBeat * (c - beforeSwitch - 1));
				}
				beforeSwitch = 0;
			}
		}
		util.add(music, add, offsetSecs);
	}

	private void addRhythm(ArrayList<double[]> music, Rhythm rhythm, double offsetSecs, Time time, Section section) {
		ArrayList<double[]> add = new ArrayList<double[]>();
		for (int r = 0; r < rhythm.size(); r++) {
			for (int drum = 0; drum < rhythm.get(r).size(); drum++) {
				double[] vol = rhythm.getBalance().clone();
				for(int i = 0; i < vol.length; i++) {
					vol[i] *= r * (section.endVolume - section.startVolume) / (time.subdivide * time.numMeasures)
							+ section.startVolume;
				}
				ArrayList<double[]> tone;
				switch (rhythm.get(r).get(drum)) {
				case snare:
					tone = toneGenerator.snare(vol);
					break;
				case hihat:
					tone = toneGenerator.hihat(vol);
					break;
				case kick:
					for (int i = 0; i < numChannels; i++) {
						vol[i] *= 3;
					}
					tone = toneGenerator.drum(70, vol);
					break;
				case tom1:
					for (int i = 0; i < numChannels; i++) {
						vol[i] *= 4;
					}
					tone = toneGenerator.drum(200, vol);
					break;
				case tom2:
					for (int i = 0; i < numChannels; i++) {
						vol[i] *= 4;
					}
					tone = toneGenerator.drum(150, vol);
					break;
				default:
					tone = new ArrayList<double[]>();
					break;
				}
				util.add(add, tone, r * time.secondsPerSubdivide());
			}
		}
		util.add(music, add, offsetSecs);
	}

	private void addNotes(ArrayList<double[]> music, Instrument instrument, double offsetSecs, Time time, Section section) {
		ArrayList<double[]> add = new ArrayList<double[]>();
		@SuppressWarnings("unchecked")
		ArrayList<Note> notes = (ArrayList<Note>) instrument;
		int t = 0;
		for (Note note : notes) {
			if (note.hz == 0) {

			} else {
				ArrayList<double[]> tone;
				double noteTime = note.lengthSubdivides * time.secondsPerSubdivide();
				double[] vol = instrument.getBalance().clone();
				for(int i = 0; i < vol.length; i++) {
					vol[i] *= t * (section.endVolume - section.startVolume) / (time.subdivide * time.numMeasures)
							+ section.startVolume;
					if(note.accent) {
						vol[i] *= 1.2;
					}
				}
				if (note.bendFromPrev) {
					tone = toneGenerator.toneBend(note.prevHz, note.hz, noteTime, .2, .01, .01, vol, instrument.getWaveType());
				} else {
					tone = toneGenerator.toneFlat(note.hz, noteTime, .01, .01, vol, instrument.getWaveType());
				}
				util.add(add, tone, t * time.secondsPerSubdivide());
			}
			t += note.lengthSubdivides;
		}
		util.add(music, add, offsetSecs);
	}

	public byte[] test() {
		double[] vol = new double[] { 100, 100 };
		return util.format(toneGenerator.toneFlat(200, 2, 0, 0, vol, Waves.WaveType.aa));
	}

}
