package org.rpanic1308.audiorecording;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

public class Recorder{

    int bytePerElement = 2;
    int bufferElements = 1024;
    int arrayLength = 1024*10;

    AudioRecord recorder;

    AudioTransmitter transmitter = new AudioTransmitter();

    public void startRecording(){


        int bufferSize = AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);


        int audioSource = MediaRecorder.AudioSource.MIC;
        int sampleRateInHz = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        final AudioRecord record2 = new AudioRecord(audioSource,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSize);

        /*final AudioRecord record2 = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.DEFAULT)
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(16000)
                        .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                        .build())
                .setBufferSizeInBytes(bufferSize)
                .build();*/

        new Thread(new Runnable() {
            @Override
            public void run() {

                record2.startRecording();

                while(true){

                    short[] arr = new short[arrayLength];
                    int read;
                    read = record2.read(arr, 0, arr.length);
                    while(read > 0){

                        transmitter.send(arr);

                        read = record2.read(arr, 0, arr.length);
                    }


                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }
        }).start();

        recorder = record2;

    }

    public void stopRecording(){
        recorder.stop();
    }
}
