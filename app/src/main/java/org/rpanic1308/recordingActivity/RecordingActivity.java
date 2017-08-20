package org.rpanic1308.recordingActivity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.main.CeresController;

import java.util.ArrayList;

import ai.kitt.snowboy.SnowboyMaster;
import rpanic1308.ceres.R;

public class RecordingActivity extends AppCompatActivity{

    SpeechRecognizer sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        getSupportActionBar().hide();

        Slide slide = new Slide(Gravity.TOP);
        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);

        initSpeekRecognizer();

    }

    public void onButtonClick(final View v){
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSpeechRecognizer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSpeechRecognizer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sr.destroy();
    }

    public void initSpeekRecognizer(){
        sr = SpeechRecognizer.createSpeechRecognizer(this, ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));

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
                SnowboyMaster.continueRecording(RecordingActivity.this);
            }
            public void onError(int error)
            {
                System.out.println("error " +  error);

                if(error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT){
                    sr.stopListening();
                }
                sr.destroy();
                SnowboyMaster.continueRecording(RecordingActivity.this);
                SnowboyMaster.increaseVolume(RecordingActivity.this);

                //speakButton.getBackground().setColorFilter(ContextCompat.getColor(MainFeedActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC);
            }
            public void onResults(Bundle results)
            {
                String str = "";
                System.out.println("onResults " + results);
                final ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++)
                {
                    System.out.println("result " + data.get(i));
                    str += data.get(i); //Rendundant
                }
                str = (String)data.get(0);
                System.out.println("STR: " +str);

                final String str2 = str;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(str2.equals("")){
                            System.out.println("INITSPeechRecognizer | Result string is null");
                            return;
                        }
                        result(str2);
                    }
                }).start();

                sr.stopListening();
                //speakButton.getBackground().setColorFilter(ContextCompat.getColor(MainFeedActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC);
                sr.destroy();
                SnowboyMaster.continueRecording(RecordingActivity.this);
                SnowboyMaster.increaseVolume(RecordingActivity.this);

            }
            public void onPartialResults(Bundle partialResults)
            {
                System.out.println("onPartialResults");
                String str = "";
                ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++)
                {
                    System.out.println("Partial result " + data.get(i));
                    str += data.get(i);
                }
                //TextView tv = (TextView)mainActivity.findViewById(R.id.textView);
                //tv.setText(str);
            }
            public void onEvent(int eventType, Bundle params)
            {
                System.out.println("onEvent " + eventType);
            }
        });
    }

    private void startSpeechRecognizer() {
        final Intent in = new Intent();
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        in.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30 * 1000);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //speakButton.setBackgroundResource(R.color.colorSpeechActive/*ContextCompat.getColor(mainActivity, R.color.colorSpeechActive)*/);
                //speakButton.getBackground().setColorFilter(0x993F51B5, PorterDuff.Mode.MULTIPLY);
                sr.startListening(in);
            }
        });
    }

    private void stopSpeechRecognizer(){
        sr.stopListening();
    }

    public void result(String result){

        CeresController.inputMessage(result);
        finish(); //Closes Activity

    }

    public void displayBitmaps(){
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(BitmapSlider.bmp1);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = 0;
        windowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.flags =
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        this.getWindowManager().addView(imageView, windowParams);
    }
}
