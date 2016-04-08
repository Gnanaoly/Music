package instrument;

public class Note {

	public int lengthSubdivides = 0;
	public double hz = 0; //If hz is 0, this is a rest
	public boolean bendFromPrev = false;
	public boolean accent = false;
	public double prevHz = 0;
	
	public Note(){
	}
	
	public Note(int lengthSubdivides, double hz, boolean bendFromPrev, boolean accent, double prevHz) {
		this.lengthSubdivides = lengthSubdivides;
		this.hz = hz;
		this.bendFromPrev = bendFromPrev;
		this.accent = accent;
		this.prevHz = prevHz;
	}
	
}
