package com.example.keithfawcett.escapecallgoldandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CallScreen extends AppCompatActivity {

    Context mContext = this;

    boolean calling = true;

    MediaPlayer mPlayer;
    ImageView callerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);

        callerImage = (ImageView) findViewById(R.id.contactImage);
        callerImage.setImageURI(Timer.finalCallerImage);
        TextView callerName = (TextView) findViewById(R.id.callerNameCallScreen);
        callerName.setText(Timer.finalCallerName);
        Button answerButton = (Button) findViewById(R.id.answerButton);

        answerButton.setBackgroundColor(Color.TRANSPARENT);

        final Runnable helloRunnable = new Runnable() {
            public void run() {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(calling){
                    v.vibrate(500);
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);

        switch (Timer.finalCallerRingtone) {
            case "atria":
                mPlayer = MediaPlayer.create(CallScreen.this, R.raw.atria);
                break;
            case "dione":
                mPlayer = MediaPlayer.create(CallScreen.this, R.raw.dione);
                break;
            case "luna":
                mPlayer = MediaPlayer.create(CallScreen.this, R.raw.luna);
                break;
            case "sedna":
                mPlayer = MediaPlayer.create(CallScreen.this, R.raw.sedna);
                break;
            case "titania":
                mPlayer = MediaPlayer.create(CallScreen.this, R.raw.titania);
                break;
            default:
                mPlayer = MediaPlayer.create(CallScreen.this, R.raw.atria);
        }



        mPlayer.setLooping(true);
        mPlayer.start();


        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();
                Intent intent = new Intent(mContext, AnswerScreen.class);
                calling = false;
                startActivity(intent);
            }
        });
    }
}