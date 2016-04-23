package composition;

import java.util.ArrayList;
import java.util.HashMap;

import instrument.Instrument;
import instrument.Instrument.InstrumentType;
import music.Main;
import music.RandomUtil;
import music.Waves.WaveType;

public class Section {

	public int id;
	public Time time;
	public double startVolume;
	public double endVolume;
	private ArrayList<Instrument> instruments;
	private RandomUtil rand;
	private HashMap<Instrument, Integer> offset;

	public Section(int id, Time time) {
		this.id = id;
		this.time = time;
		instruments = new ArrayList<Instrument>();
		rand = new RandomUtil();
		offset = new HashMap<Instrument, Integer>();
	}

	public void addInstrument(Instrument instrument, int offsetMeasures) {
		instruments.add(instrument);
		offset.put(instrument, offsetMeasures);
	}
	
	public int getOffsetMeasures(Instrument instrument) {
		return offset.get(instrument);
	}

	public void complexify() {
		for (Instrument instrument : instruments) {
			instrument.complexify();
		}
	}

	public Section duplicate() {
		Section section = new Section(id, time);
		for (Instrument instrument : instruments) {
			section.addInstrument(instrument.duplicate(), getOffsetMeasures(instrument));
		}
		section.startVolume = startVolume;
		section.endVolume = endVolume;
		return section;
	}

	public Instrument getInstrument(InstrumentType type) {
		for (Instrument instrument : instruments) {
			if (instrument.getType() == type) {
				return instrument;
			}
		}
		return null;
	}

	public ArrayList<Instrument> getInstruments() {
		return instruments;
	}

	public void randomizeBalances() {
		int numChannels = Main.numChannels;
		
		for(Instrument instrument : instruments) {
			double[] balance = new double[numChannels];
			for(int i = 0; i < numChannels; i++) {
				balance[i] = rand.nextDouble() * .25 + .75;
				if(instrument.getWaveType() == WaveType.sin || instrument.getWaveType() == WaveType.aa) {
						balance[i] *= 5;
				}
			}
			instrument.setBalance(balance);
		}
	}

}
