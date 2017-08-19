package org.rpanic1308.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextToSpeech {

	public static InputStream getVoiceAudio(String sentence){
		InputStream ret = null;
		URL url = null;
		HttpURLConnection con = null;
		
		sentence = sentence.replaceAll(" ", "%20");
		
		try {
			url = new URL("https://translate.google.com/translate_tts?ie=UTF-8&q=" + sentence + "&tl=de-DE&client=tw-ob");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			ret =  con.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return ret;
		}
		
		return ret;
	}
	
}
