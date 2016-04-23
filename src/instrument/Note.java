package instrument;

public class Note {

	public int lengthSubdivides = 0;
	public double[] hz = new double[0]; //If hz.length is 0, this is a rest
	public boolean bendFromPrev = false;
	public boolean accent = false;
	public double[] prevHz = new double[0];
	
	public Note() {
		
	}
	
	public Note(int lengthSubdivides, double[] hz, boolean bendFromPrev, boolean accent, double[] prevHz) {
		this.lengthSubdivides = lengthSubdivides;
		this.hz = hz;
		this.bendFromPrev = bendFromPrev;
		this.accent = accent;
		this.prevHz = prevHz;
	}
	
}
