
public class Main {

	static final int sampleRate = 22050;
	static final int bitsPerSample = 16;
	static final int numChannels = 2;
	static final String path = "/home/nathav63/Downloads/test.wav";

	public static void main(String[] args) {
		Writer writer = new Writer();
		MusicGenerator generator = new MusicGenerator(sampleRate, bitsPerSample, numChannels);
		writer.dumpToFile(path, generator.getMusic(), sampleRate, bitsPerSample, numChannels);
		System.out.println("All done!");
		//testStuff();
	}
	
	public static void testStuff() {
		RandomUtil rand = new RandomUtil();
		int[] values = new int[20];
		for(int i = 0; i < 100000; i++) {
			values[rand.randomSkewed(values.length, 1)]++;
		}
		for(int i = 0; i < values.length; i++) {
			System.out.println("values[" + i + "] = " + values[i]);
		}
	}

}
