package com.example.keithfawcett.escapecallgoldandroid;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class Recorder extends AppCompatActivity {

    MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private Button start, stop, play;

    MediaPlayer m;




    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);


        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        start = (Button) findViewById(R.id.button1);
        stop = (Button) findViewById(R.id.button2);
        play = (Button) findViewById(R.id.button3);

        start.setEnabled(true);
        stop.setEnabled(false);
        play.setEnabled(false);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() +  "rec.3gp";


        Toast.makeText(this, outputFile, Toast.LENGTH_SHORT).show();



    }

    public void start(View v){

        try {
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        start.setEnabled(false);
        stop.setEnabled(true);

//        Toast.makeText(this,"Recording started", Toast.LENGTH_SHORT).show();
    }

    public void stop(View v){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;

        stop.setEnabled(false);
        start.setEnabled(true);
        play.setEnabled(true);

//        Toast.makeText(this,"Audio Recording successfully", Toast.LENGTH_SHORT).show();
    }

    public void play(View v) throws IOException {

        m = new MediaPlayer();
        m.setDataSource(outputFile);
        m.prepare();
        m.start();

//        Toast.makeText(this," playing audio", Toast.LENGTH_SHORT).show();
    }

    public void select(View v) {
        Intent intent = new Intent();
        intent.putExtra(CustomCaller.EXTRA_RECORDING, outputFile);
        setResult(RESULT_OK, intent);
        if(m != null) {
            if (m.isPlaying()) {
                m.stop();
            }
        }

        finish();

    }

    @Override
    public void onBackPressed() {
        if (m != null) {
            if (m.isPlaying()) {
                m.stop();
            }
            super.onBackPressed();
        }
    }
}
