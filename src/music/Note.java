package music;

public class Note {

	public int lengthSixteenths;
	public double hz; //If hz is 0, this is a rest
	public boolean bendFromPrev;
	public boolean accent;
	public double prevHz;
	
	public Note(int lengthSixteenths, double hz, boolean bendFromPrev, boolean accent, double prevHz) {
		this.lengthSixteenths = lengthSixteenths;
		this.hz = hz;
		this.bendFromPrev = bendFromPrev;
		this.accent = accent;
		this.prevHz = prevHz;
	}
	
}
