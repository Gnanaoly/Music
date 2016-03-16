import java.util.ArrayList;

@SuppressWarnings("serial")
public class ChordProgression extends ArrayList<Chord> {

	private Chord tonic;
	private RandomUtil rand;

	public ChordProgression(double tonic, Chord.ChordType type) {
		super();
		this.tonic = new Chord(tonic, type);
		this.rand = new RandomUtil();
		generateProgression(0, 0);
	}
	
	public void generateProgression(int beatsPerMeasure, int numMeasures) {
		clear();
		for(int i = 1; i <= 8; i++) {
			add(getNth(tonic, i));
		}
	}
	
	private Chord getNth(Chord root, int n) {
		double[] noteHz = root.getScaleNoteHz();
		
		double oldn = n;
		n %= 8;
		if(n != oldn) n++;
		int mult = (int) Math.ceil(oldn / 7);
		
		double nthRootHz = noteHz[n-1] * mult;
		
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
		
		return new Chord(nthRootHz, scaleNotes, notesInChord, true);
	}
}
