
public class Chord {
	
	private double[] notes;
	private double[] chromatic;
	
	//Just Temperament factors
	private final double[] jt =
		{1.0, 25.0/24, 9.0/8, 6.0/5, 5.0/4, 4.0/3, 45.0/32, 3.0/2, 8.0/5, 5.0/3, 9.0/5, 15.0/8};
	
	public enum ChordType {
		Major,
		Dom7,
		Minor,
		Dim
	}

	public Chord(double rootHz, ChordType type)
	{
		notes = new double[7];
		notes[0] = rootHz;
		chromatic = new double[12];
		for(int i = 0; i < 12; i++) {
			chromatic[i] = rootHz * jt[i];
		}
		switch(type) {
		case Major: setupMajor();
		break;
		case Dom7: setupDom7();
		break;
		case Minor: setupMinor();
		break;
		case Dim: setupDim();
		break;
		default: break;
		}
	}
	
	double[] getAllNotes() {
		return notes;
	}
	
	double[] getTriad(int[] mod){
		boolean[] include = new boolean[] {true, false, true, false, true, false, false};
		if(mod != null) {
			for (int i : mod) {
				if (i > 0) {
					include[i - 1] = true;
				} else if (i < 0) {
					include[i * -1 - 1] = false;
				}
			}
		}
		int num = 0;
		for(boolean in : include)
			if(in) num++;
		double[] ret = new double[num];
		int index = 0;
		for(int i = 0; i < 7; i++) {
			if(include[i])
				ret[index++] = notes[i];
		}
		return ret;
	}
	
	private void setupMajor(){
		notes[1] = chromatic[2];
		notes[2] = chromatic[4];
		notes[3] = chromatic[5];
		notes[4] = chromatic[7];
		notes[5] = chromatic[9];
		notes[6] = chromatic[11];
	}
	
	private void setupDom7(){
		notes[1] = chromatic[2];
		notes[2] = chromatic[4];
		notes[3] = chromatic[5];
		notes[4] = chromatic[7];
		notes[5] = chromatic[9];
		notes[6] = chromatic[10];
	}
	
	private void setupMinor(){
		notes[1] = chromatic[2];
		notes[2] = chromatic[3];
		notes[3] = chromatic[5];
		notes[4] = chromatic[7];
		notes[5] = chromatic[8];
		notes[6] = chromatic[10];
	}
	
	private void setupDim(){
		notes[1] = chromatic[2];
		notes[2] = chromatic[3];
		notes[3] = chromatic[5];
		notes[4] = chromatic[6];
		notes[5] = chromatic[8];
		notes[6] = chromatic[10];
	}
	
}
