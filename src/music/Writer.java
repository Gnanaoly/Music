package music;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Writer {
	
	public void dumpToFile(String filename, byte[] data, int sampleRate, int bitsPerSample, int numChannels)
	{
		Path file = Paths.get(filename);
		try {
			Files.write(file, getHead(sampleRate, bitsPerSample, numChannels, data.length));
			Files.write(file, data, StandardOpenOption.APPEND);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] getHead(int sampleRate, int bitsPerSample, int numChannels, int dataLength)
	{
		byte[] head = new byte[44];
		//Offset  Size  Name             Description
		//0         4   ChunkID          Contains the letters "RIFF" in ASCII form
		writeString("RIFF", head, 0);
		//4         4   ChunkSize        36 + SubChunk2Size
		writeNum(36 + dataLength, head, 4, 4);
		//8         4   Format           Contains the letters "WAVE"
		writeString("WAVE", head, 8);
		//12        4   Subchunk1ID      Contains the letters "fmt "
		writeString("fmt ", head, 12);
		//16        4   Subchunk1Size    16 for PCM.
		writeNum(16, head, 16, 4);
		//20        2   AudioFormat      PCM = 1 (i.e. Linear quantization)
		writeNum(1, head, 20, 2);
		//22        2   NumChannels      Mono = 1, Stereo = 2, etc.
		writeNum(numChannels, head, 22, 2);
		//24        4   SampleRate       8000, 44100, etc.
		writeNum(sampleRate, head, 24, 4);
		//28        4   ByteRate         == SampleRate * NumChannels * BitsPerSample/8
		writeNum(sampleRate * numChannels * bitsPerSample / 8, head, 28, 4);
		//32        2   BlockAlign       == NumChannels * BitsPerSample/8
		writeNum(numChannels * bitsPerSample / 8, head, 32, 2);
		//34        2   BitsPerSample    8 bits = 8, 16 bits = 16, etc.
		writeNum(bitsPerSample, head, 34, 2);
		//36        4   Subchunk2ID      Contains the letters "data"
		writeString("data", head, 36);
		//40        4   Subchunk2Size    == NumSamples * NumChannels * BitsPerSample/8
		writeNum(dataLength, head, 40, 4);
		//44        *   Data             The actual sound data.
		return head;
	}
	
	private void writeString(String st, byte[] by, int start)
	{
		for(byte b : st.getBytes()) {
			by[start++] = b;
		}
	}
	
	private void writeNum(int num, byte[] by, int start, int length)
	{
		for(int i = start; i < start + length; i++) {
			by[i] = (byte) (num % 0x100);
			num >>= 8;
		}
	}

}
