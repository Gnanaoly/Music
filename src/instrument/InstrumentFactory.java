package instrument;

import java.util.HashMap;

import composition.Time;
import instrument.Instrument.InstrumentType;
import music.Waves.WaveType;

public class InstrumentFactory {

	private Time time;
	private ChordProgression progression;
	private Rhythm rhythm;
	private HashMap<InstrumentType, WaveType> instrumentWaves;

	public InstrumentFactory(Time time, ChordProgression progression, HashMap<InstrumentType, WaveType> instrumentWaves) {
		this.time = time;
		this.progression = progression;
		this.rhythm = new Rhythm(time);
		this.instrumentWaves = instrumentWaves;
	}
	
	public ChordProgression getChordProgression() {
		return progression;
	}

	public Instrument getInstrument(InstrumentType type, double complexity) {
		return getInstrument(type, time, progression, complexity);
	}

	public Instrument getInstrument(InstrumentType type, Time time, ChordProgression progression, double complexity) {
		switch (type) {
		case Melody:
			return new Melody(time, progression, complexity, instrumentWaves.get(type));
		case Bass:
			return new Bass(time, progression, rhythm, instrumentWaves.get(type));
		case Rhythm:
			return rhythm;
		case Keys:
			return new Keys(time, progression, complexity, instrumentWaves.get(type));
		default:
			System.err.println("Instrument not implemented in InstrumentFactory.");
			return null;
		}
	}

}
