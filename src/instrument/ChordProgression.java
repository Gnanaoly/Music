package instrument;
import java.util.ArrayList;

import composition.Time;
import music.RandomUtil;
import music.Waves;
import music.Waves.WaveType;

@SuppressWarnings("serial")
public class ChordProgression extends ArrayList<Chord> implements Instrument {

	private Chord tonic;
	private RandomUtil rand;
	public Waves.WaveType waveType = Waves.WaveType.sin;

	public ChordProgression(Time time, Chord tonic, double relChordRate, int leadTo) {
		super();
		this.tonic = tonic;
		this.rand = new RandomUtil();
		generateProgression(time, relChordRate, leadTo);
	}
	
	//Shoot for relChordRate chords per measure
	private void generateProgression(Time time, double relChordRate, int leadTo) {
		clear();
		/*for(int i = 1; i <= 8; i++) {
			add(getNth(tonic, i));
		}*/
		add(tonic);
		while(size() < time.beatsPerMeasure * (time.numMeasures - 1)) { //Save last measure to resolve
			Chord prev = get(size()-1);
			//Probability to move on is relChordRate / beatsPerMeasure.
			double probToSwitch = relChordRate / time.beatsPerMeasure;
			if(size() % time.beatsPerMeasure == 0 || size() % time.beatsPerMeasure / 2 == 0)
				probToSwitch *= 1.5;
			else 
				probToSwitch *= .5;
			if(rand.nextDouble() < probToSwitch) {
				Chord[] options = new Chord[] {getNth(prev, 4), getNth(prev, 3),
						getNth(prev, 6), getNth(prev, 5),  getNth(prev, 2)};
				add(options[rand.nextSkewed(options.length, -.5)]);
			} else {
				add(prev);
			}
		}
		//We have a measure to set up the leadTo chord
		if(time.beatsPerMeasure < 4 || relChordRate < 1) {
			Chord[] options = new Chord[] {getNth(tonic, leadTo+4), getNth(tonic, leadTo+6)};
			Chord chord = options[rand.nextSkewed(options.length, 0)];
			for(int i = 0; i < time.beatsPerMeasure; i++) {
				add(chord);
			}
		} else {
			for(int i = 0; i < time.beatsPerMeasure-2; i++) {
				add(getNth(tonic, leadTo));
			}
			//Put in 2,5
			add(getNth(tonic, leadTo+1));
			add(getNth(tonic, leadTo+4));
		}
	}
	
	private Chord getTriToneSub() {
		return null;
	}
	
	private Chord getNth(Chord root, int n) {
		double[] noteHz = root.getScaleNoteHz();
		
		//double oldn = n;
		n %= 8;
		if(n == 0) n = 7;
		//if(n != oldn) n++;
		//int mult = (int) Math.ceil(oldn / 7);
		
		double nthRootHz = noteHz[n-1];// * mult;
		while(nthRootHz > tonic.getRootHz() * 2) {
			nthRootHz /= 2;
		}
		while(nthRootHz < tonic.getRootHz()) {
			nthRootHz *= 2;
		}
		
		int[] rootScaleNotes = root.getScaleNotes();
		int[] scaleNotes = new int[rootScaleNotes.length];
		scaleNotes[0] = 0;
		for(int i = 1, j = n; i < scaleNotes.length; i++, j++) {
			int rootStep;
			if(j == rootScaleNotes.length) {
				rootStep = rootScaleNotes[0] - (rootScaleNotes[rootScaleNotes.length-1] - 12);
				j = 0;
			} else {
				rootStep = rootScaleNotes[j] - rootScaleNotes[j-1];
			}
			scaleNotes[i] = scaleNotes[i-1] + rootStep;
		}
		
		/*System.out.println("" + n + " chord");
		for(int i = 0; i < scaleNotes.length; i++) {
			System.out.println(scaleNotes[i] - rootScaleNotes[i]);
		}*/
		
		int[] notesInChord = new int[] {1, 3, 5};
		
		Chord ret = new Chord(nthRootHz, tonic.getRootHz(), scaleNotes, notesInChord, true);
		ret.imposeChromatic(root.getChromatic());
		
		return ret;
	}

	@Override
	public InstrumentType getType() {
		return InstrumentType.ChordProgression;
	}

	@Override
	public Instrument duplicate() {
		return this;
	}

	@Override
	public void complexify() {
		// TODO Auto-generated method stub
	}

	@Override
	public WaveType getWaveType() {
		return waveType;
	}

	@Override
	public void setWaveType(WaveType type) {
		waveType = type;
	}
	
	private double[] balance;
	
	@Override
	public void setBalance(double[] balance) {
		this.balance = balance;
	}

	@Override
	public double[] getBalance() {
		return balance;
	}
}
