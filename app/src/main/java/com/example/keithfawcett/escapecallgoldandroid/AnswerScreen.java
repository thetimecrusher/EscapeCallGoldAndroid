package com.example.keithfawcett.escapecallgoldandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class AnswerScreen extends AppCompatActivity {

    Context mContext = this;

    Chronometer callTime;
    long time = 0;

    ImageView callerImage;


    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_screen);

        callerImage = (ImageView) findViewById(R.id.contactImageAnswer);
        callerImage.setImageURI(Timer.finalCallerImage);
        TextView callerName = (TextView) findViewById(R.id.callerNameAnswerScreen);
        callerName.setText(Timer.finalCallerName);
        Button endcallButton = (Button) findViewById(R.id.endCallButton);


        endcallButton.setBackgroundColor(Color.TRANSPARENT);

        callTime = (Chronometer) findViewById(R.id.callTime);

        callTime.setBase(SystemClock.elapsedRealtime()+time);
        callTime.start();

        if(!CallSettings.voice.equals("Voice Off")){

            if(CallSettings.voice.equals("Male")){
                mPlayer = MediaPlayer.create(AnswerScreen.this, R.raw.male);
            }else {
                mPlayer = MediaPlayer.create(AnswerScreen.this, R.raw.male);
            }

            mPlayer.start();
        }

        if(!Timer.finalCustomVoice.equals("")){
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(Timer.finalCustomVoice);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
        }




        endcallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer != null){
                    mPlayer.stop();
                }
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        });

    }



}
