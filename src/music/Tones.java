package music;

import java.util.ArrayList;

public class Tones {

	private int sampleRate;
	private int numChannels;
	private Waves waveGenerator;

	public Tones(int sampleRate, int numChannels) {
		this.sampleRate = sampleRate;
		this.numChannels = numChannels;
		waveGenerator = new Waves(sampleRate);
	}

	private double getRate(double time, double change) {
		if (time <= 0)
			return change;
		return change / (time * sampleRate);
	}

	public ArrayList<double[]> toneFlat(double hz, double duration, double buildIn, double buildOut, double[] volume,
			Waves.WaveType type) {
		return toneGliss(hz, hz, duration, buildIn, buildOut, volume, type);
	}
	
	public ArrayList<double[]> toneBend(double hzStart, double hzEnd, double duration, double ratio, double buildIn, double buildOut,
			double[] volume, Waves.WaveType type) {
		ArrayList<double[]> tone = toneGliss(hzStart, hzEnd, duration * ratio, buildIn, 0, volume, type);
		tone.addAll(toneFlat(hzEnd, duration * (1-ratio), 0, buildOut, volume, type));
		return tone;
	}

	public ArrayList<double[]> toneGliss(double hzStart, double hzEnd, double duration, double buildIn, double buildOut,
			double[] volume, Waves.WaveType type) {
		int numFrames = (int) (sampleRate * duration);
		double masterVol = 1;

		double buildInRate = getRate(buildIn, masterVol);
		double buildOutRate = getRate(buildOut, masterVol);
		double vol = 0;
		int buildOutStart = (int) (numFrames - masterVol / buildOutRate);

		// We have to cut bendRate in half because the waves being compressed
		// doubles the effect.
		double bendRate = getRate(duration, hzEnd - hzStart) / 2;
		double hz = hzStart;

		ArrayList<double[]> music = new ArrayList<double[]>(numFrames);
		
		for (int frame = 0; frame < numFrames; frame++) {
			if (frame >= buildOutStart)
				vol -= buildOutRate;
			else if (vol < masterVol)
				vol += buildInRate;
			hz += bendRate;
			music.add(new double[numChannels]);
			for (int channel = 0; channel < numChannels; channel++) {
				music.get(frame)[channel] = waveGenerator.getWave(hz, frame, type) * volume[channel] * vol;
			}
		}

		// Remove final partial wave so that we don't click
		for (int frame = music.size() - 1; frame > 0; frame--) {
			double val = music.get(frame)[0];
			music.remove(frame);
			if (val > 0 && music.get(frame - 1)[0] <= 0)
				break;
		}
		return music;
	}
	
	public ArrayList<double[]> snare(double[] volume) {
		return snareHat(volume, .9997);
	}
	
	public ArrayList<double[]> hihat(double[] volume) {
		return snareHat(volume, .9990);
	}
	
	private ArrayList<double[]> snareHat(double[] volume, double decay) {
		double masterVol = 1;
		double finalVolume = masterVol / 100;
		//volume * decay^numFrames = finalVolume
		int numFrames = (int) (Math.log(finalVolume / masterVol) / Math.log(decay));
		ArrayList<double[]> hit = new ArrayList<double[]>(numFrames);
		for(int frame = 0; frame < numFrames; frame++) {
			hit.add(new double[numChannels]);
			for (int channel = 0; channel < numChannels; channel++) {
				hit.get(frame)[channel] = waveGenerator.rand() * masterVol * volume[channel];
				
			}
			masterVol *= decay;
		}
		return hit;
	}
	
	//hz = 30 or 40 is great for kick
	public ArrayList<double[]> drum(double hz, double[] volume) {
		double hzStart = hz;
		double hzEnd = hzStart / 2;
		double decay = .9995;
		double masterVol = 1;
		double finalVolume = masterVol / 100;
		//volume * decay^numFrames = finalVolume
		int numFrames = (int) (Math.log(finalVolume / masterVol) / Math.log(decay));
		ArrayList<double[]> hit = new ArrayList<double[]>(numFrames);
		for(int frame = 0; frame < numFrames; frame++) {
			hit.add(new double[numChannels]);
			for (int channel = 0; channel < numChannels; channel++) {
				hit.get(frame)[channel] = waveGenerator.sin(hzStart, frame) * masterVol * volume[channel] * (1-frame / numFrames);
				hit.get(frame)[channel] += waveGenerator.sin(hzEnd, frame) * masterVol * volume[channel] * (frame / numFrames);
			}
			masterVol *= decay;
		}
		return hit;
	}

}
