package phoneme;

public class PhonWaves {

	private int sampleRate;
	
	public PhonWaves(int sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	private double getAmp(double hertz, double frame, double[][] data, double n) {
		frame *= hertz * n / sampleRate;
		frame = frame % n;
		
		double ret = 0;
		for(int k = 0; k < data[0].length; k++) {
			ret += 1/n * (data[0][k]*Math.cos(data[2][k]*frame*2*Math.PI/n) - data[1][k]*Math.sin(data[2][k]*frame*2*Math.PI/n));
		}
		return ret;
	}
	
	public double iy(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.IY, PhonWaveData.IYrecordFrames);
	}
	
	public double ih(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.IH, PhonWaveData.IHrecordFrames);
	}
	
	public double ey(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.EY, PhonWaveData.EYrecordFrames);
	}
	
	public double eh(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.EH, PhonWaveData.EHrecordFrames);
	}
	
	public double ae(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AE, PhonWaveData.AErecordFrames);
	}
	
	public double aa(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AA, PhonWaveData.AArecordFrames);
	}
	
	public double ao(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AO, PhonWaveData.AOrecordFrames);
	}
	
	public double ow(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.OW, PhonWaveData.OWrecordFrames);
	}
	
	public double uh(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.UH, PhonWaveData.UHrecordFrames);
	}
	
	public double uw(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.UW, PhonWaveData.UWrecordFrames);
	}
	
	public double er(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.ER, PhonWaveData.ERrecordFrames);
	}
	
	public double ax(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AX, PhonWaveData.AXrecordFrames);
	}
	
	public double ah(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AH, PhonWaveData.AHrecordFrames);
	}
	
	public double ay(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AY, PhonWaveData.AYrecordFrames);
	}
	
	public double aw1(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AW1, PhonWaveData.AW1recordFrames);
	}
	
	public double aw2(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.AW2, PhonWaveData.AW2recordFrames);
	}
	
	public double oy1(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.OY1, PhonWaveData.OY1recordFrames);
	}
	
	public double oy2(double hertz, double frame) {
		return getAmp(hertz, frame, PhonWaveData.OY2, PhonWaveData.OY2recordFrames);
	}
}
