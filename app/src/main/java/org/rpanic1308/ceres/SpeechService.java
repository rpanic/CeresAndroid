package org.rpanic1308.ceres;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Team_ on 27.01.2017.
 */

public class SpeechService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        recognize();
    }

    public void recognize(){

        final SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent in = new Intent();
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        in.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30 * 1000);

        sr.setRecognitionListener(new RecognitionListener() {
            public void onReadyForSpeech(Bundle params)
            {
                System.out.println("onReadyForSpeech");
            }
            public void onBeginningOfSpeech()
            {
                System.out.println("onBeginningOfSpeech");
            }
            public void onRmsChanged(float rmsdB)
            {
                System.out.println("onRmsChanged");
            }
            public void onBufferReceived(byte[] buffer)
            {
                System.out.println("onBufferReceived");
            }
            public void onEndOfSpeech()
            {
                System.out.println("onEndofSpeech");
            }
            public void onError(int error)
            {
                System.out.println("error " +  error);

                if(error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT){
                    sr.stopListening();
                    sr.startListening(in);
                }

            }
            public void onResults(Bundle results)
            {
                String str = new String();
                System.out.println("onResults " + results);
                ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++)
                {
                    System.out.println("result " + data.get(i));
                    str += data.get(i);
                }

                results(data.get(0).toString());
                sr.stopListening();
                sr.startListening(in);

            }
            public void onPartialResults(Bundle partialResults)
            {
                System.out.println("onPartialResults");
            }
            public void onEvent(int eventType, Bundle params)
            {
                System.out.println("onEvent " + eventType);
            }
        });

        sr.startListening(in);

    }

    private void results(String s) {

        if(s.toLowerCase().contains("hallo")) {
            //Intent t = new Intent(this, MainActivity.class);
            //t.putExtra("result", s);
            //startActivity(t);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
