package music;

import phoneme.Phoneme;

public class Main {

	static final int sampleRate = 44100;
	static final int bitsPerSample = 16;
	static final int numChannels = 2;
	static final String path = "/home/nathav63/Downloads/out.wav";

	public static void main(String[] args) {
		System.out.println(Phoneme.toPhoneme("Hello world!"));
		//Lyrics.doStuff();
		Writer writer = new Writer();
		MusicGenerator generator = new MusicGenerator(sampleRate, bitsPerSample, numChannels);
		Composer composer = new Composer(generator);
		writer.dumpToFile(path, composer.compose(), sampleRate, bitsPerSample, numChannels);
		
		System.out.println("All done!");
	}

}
