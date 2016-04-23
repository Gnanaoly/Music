package music;

import composition.Composer;

public class Main {

	static final int sampleRate = 44100;
	static final int bitsPerSample = 16;
	public static final int numChannels = 2;
	static final String path = "/home/nathav63/Downloads/out.wav";

	public static void main(String[] args) {
		//Lyrics.doStuff();
		Writer writer = new Writer();
		MusicGenerator generator = new MusicGenerator(sampleRate, bitsPerSample, numChannels);
		Composer composer = new Composer(generator);
		writer.dumpToFile(path, composer.compose(), sampleRate, bitsPerSample, numChannels);
		//writer.dumpToFile(path, generator.test(), sampleRate, bitsPerSample, numChannels);
		
		System.out.println("All done!");
	}

}
