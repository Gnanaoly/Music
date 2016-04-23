package instrument;

import composition.Time;
import instrument.Instrument.InstrumentType;
import music.Waves.WaveType;

public class InstrumentFactory {

	private Time time;
	private ChordProgression progression;
	private Rhythm rhythm;

	public InstrumentFactory(Time time, ChordProgression progression) {
		this.time = time;
		this.progression = progression;
		this.rhythm = new Rhythm(time);
	}
	
	public ChordProgression getChordProgression() {
		return progression;
	}

	public Instrument getInstrument(InstrumentType type, double complexity, WaveType waveType) {
		return getInstrument(type, time, progression, complexity, waveType);
	}

	public Instrument getInstrument(InstrumentType type, Time time, ChordProgression progression, double complexity,
			WaveType waveType) {
		switch (type) {
		case Melody:
			return new Melody(time, progression, complexity, waveType);
		case Bass:
			return new Bass(time, progression, rhythm, waveType);
		case Rhythm:
			return rhythm;
		case Keys:
			return new Keys(time, progression, complexity, waveType);
		default:
			System.err.println("Instrument not implemented in InstrumentFactory.");
			return null;
		}
	}

}
