package ai.kitt.snowboy.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import org.rpanic1308.snowboyNotification.SnowboyNotificationHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ai.kitt.snowboy.Constants;
import ai.kitt.snowboy.SnowboyDetect;
import ai.kitt.snowboy.SnowboyListener;

public class RecordingThreadCopy {
    static { System.loadLibrary("snowboy-detect-android"); }

    private static final String TAG = RecordingThreadCopy.class.getSimpleName();

    private static final String ACTIVE_RES = Constants.ACTIVE_RES;
    private static final String ACTIVE_UMDL = Constants.ACTIVE_UMDL;

    private boolean shouldContinue;
    private AudioDataReceivedListener listener = null;
    SnowboyListener handler;
    SnowboyListener stopCallback;
    private Thread thread;

    private static String strEnvWorkSpace = Constants.DEFAULT_WORK_SPACE;
    private String activeModel = strEnvWorkSpace+ACTIVE_UMDL;
    private String commonRes = strEnvWorkSpace+ACTIVE_RES;

    private SnowboyDetect detector = new SnowboyDetect(commonRes, activeModel);
    private MediaPlayer player = new MediaPlayer();

    public RecordingThreadCopy(SnowboyListener handler, AudioDataReceivedListener listener) {
        this.handler = handler;
        this.listener = listener;

        detector.SetSensitivity(Constants.sensitivity);
        detector.SetAudioGain(1.2F);
        detector.ApplyFrontend(true);
        try {
            player.setDataSource(strEnvWorkSpace+"ding.wav");
            player.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Playing ding sound error", e);
        }
    }

    public boolean isRunning(){
        return shouldContinue;
    }

    public void startRecording() {
        if (thread != null)
            return;

        shouldContinue = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                record();
            }
        });
        thread.start();
        Log.d("Ceres", "Snowboy started");
    }

    public void stopRecording(SnowboyListener callback){
        stopCallback = callback;
        shouldContinue = false;
        stopRecording();
        callback.callback(true);
        Log.d("Ceres", "Snowboy stopped");
    }

    public void stopRecording() {
        shouldContinue = false;
        stopCallback = null;

        if(detector != null){
            detector.delete();
        }

        if (thread == null)
            return;

        thread = null;
    }

    private void record() {
        Log.v(TAG, "Start");
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        SnowboyNotificationHandler.getInstance(null).updateNotification();

        // Buffer size in bytes: for 0.1 second of audio
        int bufferSize = (int)(Constants.SAMPLE_RATE * 0.1 * 2);
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = Constants.SAMPLE_RATE * 2;
        }

        byte[] audioBuffer = new byte[bufferSize];
        AudioRecord record = new AudioRecord(
            MediaRecorder.AudioSource.DEFAULT,
            Constants.SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize);

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "Audio Record can't initialize!");
            return;
        }
        record.startRecording();
        if (null != listener) {
            listener.start();
        }
        Log.v(TAG, "Start recording");

        long shortsRead = 0;
        detector.Reset();
        while (shouldContinue) {
            record.read(audioBuffer, 0, audioBuffer.length);

            if (null != listener) {
                listener.onAudioDataReceived(audioBuffer, audioBuffer.length);
            }
            
            // Converts to short array.
            short[] audioData = new short[audioBuffer.length / 2];
            ByteBuffer.wrap(audioBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioData);

            shortsRead += audioData.length;

            if(!shouldContinue){
                break;
            }

            // Snowboy hotword detection.
            int result = detector.RunDetection(audioData, audioData.length);

            if (result == -2) {
                // post a higher CPU usage:
                // sendMessage(MsgEnum.MSG_VAD_NOSPEECH, null);
            } else if (result == -1) {
                //ERROR
                handler.callback(result);
            } else if (result == 0) {
                // post a higher CPU usage:
                // sendMessage(MsgEnum.MSG_VAD_SPEECH, null);
            } else if (result > 0) {
                Log.i("Snowboy: ", "Hotword " + Integer.toString(result) + " detected!");
                //DETECTED
                handler.callback(result);
                player.start();
            }
        }

        record.stop();
        record.release();


        if (null != listener) {
            listener.stop();
        }

        SnowboyNotificationHandler.getInstance(null).updateNotification(); //Context darf null sein, weil sonst updateNotification nicht gehen würde

        if(stopCallback != null){
            stopCallback.callback(null);
        }
        Log.v(TAG, String.format("Recording stopped. Samples read: %d", shortsRead));
    }
}
