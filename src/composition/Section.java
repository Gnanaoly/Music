package composition;

import java.util.ArrayList;

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

	public Section(int id) {
		this.id = id;
		instruments = new ArrayList<Instrument>();
		rand = new RandomUtil();
	}

	public void addInstrument(Instrument instrument) {
		instruments.add(instrument);
	}

	public void complexify() {
		for (Instrument instrument : instruments) {
			instrument.complexify();
		}
	}

	public Section duplicate() {
		Section section = new Section(id);
		for (Instrument instrument : instruments) {
			section.addInstrument(instrument.duplicate());
		}
		section.time = time;
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
				if(instrument.getType() == InstrumentType.Melody) {
					if(instrument.getWaveType() != WaveType.square || instrument.getWaveType() != WaveType.saw) {
						balance[i] *= 7;
					}
				}
			}
			instrument.setBalance(balance);
		}
	}

}
