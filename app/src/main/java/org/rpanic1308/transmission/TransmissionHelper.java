package org.rpanic1308.transmission;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.feed.FeedItem;
import org.rpanic1308.main.CeresController;

public class TransmissionHelper {

	private static Map<ServerToAppInfo.TransType, ServerListener> recievers = new HashMap<>();
	
	public static void sendSentenceToServer(String sentence){
		
			AppToServerInfo info = new AppToServerInfo(AppToServerInfo.TransType.TEXT, sentence, CeresController.CLIENTID);
			sendToServer(info);
		
	}

	private static boolean connectionfailedshown = false;
	
	public static void sendToServer(final AppToServerInfo info){
		System.out.println("Writing: " + info.toString());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Socket s = new Socket(InetAddress.getByName(CeresController.SERVER), CeresController.SERVER_PORT);
					
					SocketHolder holder = new SocketHolder(s);
					
					holder.getOut().writeObject(info);
					runInfoListener(holder);
					
					
					
				} catch (IOException e) {
					if(e instanceof ConnectException || e instanceof NoRouteToHostException || e instanceof UnknownHostException){
						if(!connectionfailedshown){

							MainFeedActivity.mainActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainFeedActivity.mainActivity, "Connection to " + CeresController.SERVER + ":" + CeresController.SERVER_PORT + " failed", Toast.LENGTH_LONG).show();
								}
							});
							//TODO Schönerer Fehler - z.B. Feed überlagern durch Bild
							connectionfailedshown = true;
						}else {
							//e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}else
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	private static void runInfoListener(final SocketHolder holder){
		
		ObjectInputStream in = holder.getIn(); 
		ServerToAppInfo obj;
		try {

			Object temp = in.readObject();


			if(temp instanceof String && temp.equals("End")) {
				holder.close();
				return;
			}

			obj = (ServerToAppInfo)temp;

			while(obj != null){
				
				System.out.println("TransmissionHelper: Packet recieved " + obj.toString());
				
				ServerListener listener = recievers.get(obj.getType());
				if(listener != null){
					listener.onRecieve(obj, holder);
				}else{
					System.out.println("No recievers found");
				}
//				executeAudio(obj, in);
//				executeObject(obj);
				Object o = in.readObject();
				if(o instanceof String){
					if(o.equals("End")){
						break;
					}
				}else if(o instanceof ServerToAppInfo){
				
					obj = (ServerToAppInfo)o;
				}
			}
			
			System.out.println("ClientTransHelper Socket close 1");
			in.close();
			
		} catch (EOFException e){System.out.println("Server closed down connection"); 
			
		} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}

		catch (Exception e){e.printStackTrace();}
		
		finally{
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("TransmissionHelper: Serious Problem");
				e.printStackTrace();
			}
		}
		
	}
	
//	private static void executeAudio(ServerToAppInfo info, ObjectInputStream in){
//		
//	}

	private static Thread checkingThread = null;
	private static Thread currentThread;

	public static void runCheckingLoop(){
		currentThread = Thread.currentThread();
		checkingThread = new Thread(new Runnable() {
			public void run() {
					while (true) {

						//Data is Timestamp from last feedItem
						FeedItem item = MainFeedActivity.mainActivity.getLastFeedItem();
						String data;
						if(item != null){
							data = Long.toString(item.getTimeStamp());
						}else{
							data = Long.toString(System.currentTimeMillis());
						}

						sendToServer(new AppToServerInfo(AppToServerInfo.TransType.CHECKFORUPDATES, data, CeresController.CLIENTID));

						try {
							synchronized (currentThread){
								currentThread.wait(10000L);
								Thread.sleep(300L);
							}

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			}
		});
		checkingThread.start();
		
	}

	/**
	 * Notifies the Checking-Loop Thread that it should check for Updates in The Feed or Alarm
	 */
	public static void checkForUpdates(){
		System.out.println("checkForUpdates");
		if(currentThread == null) {
			System.out.println("Return");
			return;
		}
		synchronized (currentThread){
			currentThread.notifyAll();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(300L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (currentThread){
					currentThread.notifyAll();
				}
			}
		}).start();
	}
	
	public static void registerListener(ServerToAppInfo.TransType type, ServerListener listener){
		recievers.put(type, listener);
	}
	
	public static boolean unregisterListener(ServerToAppInfo.TransType type){
		return recievers.remove(type) != null;
	}
	
//	private static void executeObject(ServerToAppInfo info){
//		
//		
//	}
	
}
