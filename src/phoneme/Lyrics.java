package phoneme;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Lyrics {
	
	public static void doStuff() {
		try {
			Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Special:Random").get();
			System.out.println(doc.text());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
