package org.rpanic1308.audiorecording;

import android.media.MediaRecorder;

/**
 * Created by Team_ on 06.05.2017.
 */

public class MicrophoneRecorder {

    MediaRecorder recorder;

    public void startRecording(){

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

    }

}
