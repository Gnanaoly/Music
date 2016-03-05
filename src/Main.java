
public class Main {

	static final int sampleRate = 22050;
	static final int bitsPerSample = 16;
	static final int numChannels = 2;
	static final String path = "/home/nathav63/Downloads/test.wav";
	
	public static void main(String[] args)
	{
		Writer writer = new Writer();
		MusicGenerator generator = new MusicGenerator(sampleRate, bitsPerSample, numChannels);
		writer.dumpToFile(path, generator.getMusic(), sampleRate, bitsPerSample, numChannels);
		System.out.println("All done!");
	}
	
}
