package org.rpanic1308.transmission;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.rpanic1308.main.CeresController;
import org.rpanic1308.transmission.listeners.Invokation;
import org.rpanic1308.music.AudioInfo;

public class Logic {
	
	public static FileInputStream copy(InputStream in, AudioInfo info, Invokation action){
		
		System.out.println(info.getTitle() + " | Begonnen zum downloaden");
		
		File f = new File(CeresController.baseDirectory + info.getSinger() + " " + info.getTitle() + ".mp3");
		
		try {
			if(!f.exists()){
				f.createNewFile();
			}
	        OutputStream out = new FileOutputStream(f);
	        
        	byte[] buf = new byte[1024];
	        int len;
	        int count=0;
	        while(++count < 10 && (len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }

	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
			out.close();
			action.action(f);
	        System.out.println(info.getTitle() + " | Fertig mit downloaden");
	        
	        
	        Thread.sleep(100L);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return null;
	}

	static int count = 0;

	public static int getNewCount(){
		return ++count;
	}

	public static FileInputStream copy(InputStream in){
		return copy(in, false, ++count);
	}

	public static FileInputStream copy(InputStream in, boolean append, int countt){

		System.out.println(" Begonnen zum kopieren");

		File f = new File(CeresController.baseDirectory + countt + ".mp3");

		try {
			if(!f.exists()){
				f.createNewFile();
			}
			OutputStream out = new FileOutputStream(f, append);

			byte[] buf = new byte[1024];
			int len;
			while((len=in.read(buf))>0){
				out.write(buf,0,len);
			}
			out.close();
			System.out.println("Fertig mit kopieren");

			return new FileInputStream(f);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
