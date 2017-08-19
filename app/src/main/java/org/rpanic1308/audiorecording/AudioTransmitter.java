package org.rpanic1308.audiorecording;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by rpanic on 12.05.2017.
 */

public class AudioTransmitter {

    boolean detected = false;
    ObjectOutputStream out;
    ObjectInputStream in;

    static int arraySize  = 1024*10;

    /**
     *
     * @param arr = Byte audio to write
     * @return boolean indicating if a detection was made
     */
    public boolean send(short[] arr){

        byte[] bytes = new byte[arr.length*2];
        //Convert to byte[]
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(arr);

        sendAsync(bytes);

        return detected;
    }

    private void sendAsync(byte[] arr) {
        try {
            //boolean ready = !in.readBoolean();
            //if(!ready){
            //    System.out.println("SAFOSAHFO");
            //}
            System.out.println("Writing");
            out.write(arr, 0, arraySize);
            out.flush();

            System.out.println("Waiting");
            boolean b = in.readBoolean();
            detected = b;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void init(){
        try {
            Socket s = new Socket("localhost", 1338);

            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] arr){
        AudioTransmitter t = new AudioTransmitter();
        System.out.println("Conneting");
        t.init();
        System.out.println("Connected");
        byte[] r = new byte[arraySize];
        for(int i = 0 ; i < r.length ; i++){
            r[i] = 1;
            r[i+1] = 2;
            i++;
        }
        for(int i = 0 ; i < 10 ; i++) {
            boolean b = t.send(null);
            System.out.println(i + " " +b);
        }
    }


}
