package music;

public class Chord {

	private int[] scaleNotes;
	private int[] chordNotes;
	private double[] chromatic;

	// Just Temperament factors
	private final double[] jt = { 1.0, 25.0 / 24, 9.0 / 8, 6.0 / 5, 5.0 / 4, 4.0 / 3, 45.0 / 32, 3.0 / 2, 8.0 / 5,
			5.0 / 3, 9.0 / 5, 15.0 / 8 };

	public enum ChordType {
		Major, Dom7, Minor, Dim, Alt;
	}

	public Chord(double rootHz, ChordType type) {
		switch (type) {
		case Major:
			setup(rootHz, rootHz, new int[]{1,2,3,4,5,6,7}, new int[] { 1, 3, 5 });
			break;
		case Dom7:
			setup(rootHz, rootHz, new int[]{1,2,3,4,5,6,-7}, new int[] { 1, 3, 5, 7 });
			break;
		case Minor:
			setup(rootHz, rootHz, new int[]{1,2,-3,4,5,-6,7}, new int[] { 1, 3, 5 });
			break;
		case Dim:
			setup(rootHz, rootHz, new int[]{1,2,-3,4,-5,6,-7}, new int[] { 1, 3, 5 });
			break;
		case Alt:
			setup(rootHz, rootHz, new int[]{1,-2,-3,3,-5,-6,-7}, new int[] { 1, 3, 5 });
			break;
		default:
			break;
		}
	}
	
	/*
	 * For example,
	 * Dorian:
	 * 	noteNumbers = 1, 2, -3, 4, 5, 6, -7
	 * 	notesInChord = 1, 3, 5 (or some other combo)
	 *  numbersAreScaleNotes = false
	 */
	public Chord(double rootHz, double baseHz, int[] noteNumbers, int[] notesInChord, boolean numbersAreScaleNotes) {
		if(numbersAreScaleNotes) {
			setup(rootHz, baseHz, null, notesInChord);
			setScaleNotes(noteNumbers);
		} else {
			setup(rootHz, baseHz, noteNumbers, notesInChord);
		}
	}

	private void setup(double rootHz, double baseHz, int[] noteNumbers, int[] notesInChord) {
		chordNotes = notesInChord;
		chromatic = new double[12];
		for (int i = 0; i < 12; i++) {
			chromatic[i] = rootHz * jt[i];
			while(chromatic[i] > baseHz * 2) {
				chromatic[i] /= 2;
			}
			while(chromatic[i] < baseHz) {
				chromatic[i] *= 2;
			}
		}
		if(noteNumbers != null) {
			scaleNotes = new int[noteNumbers.length];
			for(int i = 0; i < scaleNotes.length; i++) {
				switch(Math.abs(noteNumbers[i])) {
				case 1: scaleNotes[i] = (noteNumbers[i] > 0)? 0 : 11;
				break;
				case 2: scaleNotes[i] = (noteNumbers[i] > 0)? 2 : 1;
				break;
				case 3: scaleNotes[i] = (noteNumbers[i] > 0)? 4 : 3;
				break;
				case 4: scaleNotes[i] = (noteNumbers[i] > 0)? 5 : 4;
				break;
				case 5: scaleNotes[i] = (noteNumbers[i] > 0)? 7 : 6;
				break;
				case 6: scaleNotes[i] = (noteNumbers[i] > 0)? 9 : 8;
				break;
				case 7: scaleNotes[i] = (noteNumbers[i] > 0)? 11 : 10;
				default: break;
				}
			}
		}
		
	}
	
	public int[] getScaleNotes() {
		return scaleNotes;
	}
	
	public void setScaleNotes(int[] scaleNotes) {
		this.scaleNotes = scaleNotes;
	}

	public double[] getScaleNoteHz() {
		double[] noteHz = new double[scaleNotes.length];
		for(int i = 0; i < noteHz.length; i++) {
			noteHz[i] = chromatic[scaleNotes[i]];
		}
		return noteHz;
	}
	
	public double getRootHz() {
		return chromatic[scaleNotes[0]];
	}
	
	public double[] getChromatic() {
		return chromatic;
	}
	
	public int[] getTriadNumbers() {
		return chordNotes;
	}
	
	public void setTriadNumbers(int[] chordNotes) {
		this.chordNotes = chordNotes;
	}

	public double[] getTriadHz() {
		double[] ret = new double[chordNotes.length];
		double[] notes = getScaleNoteHz();
		for(int i = 0; i < chordNotes.length; i++) {
			ret[i] = notes[chordNotes[i]-1];
		}
		return ret;
	}
	
	public boolean inTriadHz(double hz) {
		double[] triadHz = getTriadHz();
		for(int i = 0; i < triadHz.length; i++) {
			if(sameNote(triadHz[i], hz)) return true;
		}
		return false;
	}
	
	//Given the chromatic for the tonic, we should set the root as necessary and the rest accordingly
	public void imposeChromatic(double[] chromatic) {
		double root = this.chromatic[0];
		int i;
		for(i = 0; i < chromatic.length; i++) {
			if(sameNote(chromatic[i], root)) break;
		}
		int j = 0;
		while(i < chromatic.length)
			this.chromatic[j++] = chromatic[i++];
		i = 0;
		while(j < this.chromatic.length) {
			this.chromatic[j++] = chromatic[i++];
		}
	}
	
	@Override
    public boolean equals(Object obj) {
		if(!(obj instanceof Chord)) return false;
		Chord other = (Chord) obj;
		return arraysEqual(getScaleNoteHz(), other.getScaleNoteHz()) &&
				arraysEqual(getTriadHz(), other.getTriadHz());
	}
	
	private boolean arraysEqual(double[] a1, double[] a2) {
		if(a1.length != a2.length) return false;
		for(int i = 0; i < a1.length; i++) {
			if(a1[i] != a2[i]) return false;
		}
		return true;
	}
	
	private boolean sameNote(double a, double b) {
		 if(a < b) {
			 double tmp = a;
			 a = b;
			 b = tmp;
		 }
		 while(a >= b) {
			 if (Math.abs(a - b) < .00001) return true;
			 a /= 2;
		 }
		 return false;
	}

}
