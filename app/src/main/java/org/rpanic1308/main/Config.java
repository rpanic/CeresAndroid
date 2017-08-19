package org.rpanic1308.main;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Config {

	private static Properties properties = new Config().getProperties();
	private static String fileName = CeresController.baseDirectory + "resources/config.properties";
	
	public static String getString(String key){
		return properties.getProperty(key);
	}
	
	public static int getInt(String key){
		return Integer.parseInt(getString(key));
	}
	
	private Properties getProperties(){
		Properties prop = new Properties();
		try {
			if(fileName == null){
				fileName = CeresController.baseDirectory + "resources/config.properties";
			}
			File f = new File(fileName);

			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			if(!f.exists()){
				f.createNewFile();
			}

			prop.load(new FileInputStream(new File(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	public static void write(String key, String value){
		properties.setProperty(key, value);
		try {
			OutputStream s = new Config().getWriteableStream();
			
			properties.store(s, null);
			s.flush();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private OutputStream getWriteableStream(){
		OutputStream output = null;
		try {
			output = new FileOutputStream(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return output;
	}
}
