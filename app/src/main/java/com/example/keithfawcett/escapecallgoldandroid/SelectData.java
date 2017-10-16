package com.example.keithfawcett.escapecallgoldandroid;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectData extends AppCompatActivity {


    Context mContext = this;

    ListView settingsList;
    int settingsPicker;

    MediaPlayer mPlayer;

    String ringtone;

    String[] chosenSettings = {};
    final String[] ringtones = {"atria", "dione","luna", "sedna","titania"};

    final String[] setTimer = {"30 seconds", "1 Minute", "5 Minutes", "10 Minutes", "15 Minutes", "20 Minutes", "30 Minutes", "45 Minutes"};
    final int[] timeInSeconds = {30,60,300,600,900,1200,1800,2700,3600};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data);


        settingsList = (ListView) findViewById(R.id.chosenItemList);


        settingsPicker = getIntent().getIntExtra(CustomCaller.Extra_Settings_Picker,5);
        if (settingsPicker == 0 ){
            chosenSettings = ringtones;
        }else if(settingsPicker == 1){
            chosenSettings = setTimer;
        }else {
            Toast.makeText(mContext, "somethings wrong", Toast.LENGTH_SHORT).show();
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_list_item_activated_1,
                chosenSettings
        );

        settingsList.setAdapter(arrayAdapter);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                if (settingsPicker == 0) {
                    switch (i) {
                        case 0:
                            stopSound();
                            mPlayer = MediaPlayer.create(SelectData.this, R.raw.atria);
                            break;
                        case 1:
                            stopSound();
                            mPlayer = MediaPlayer.create(SelectData.this, R.raw.dione);
                            break;
                        case 2:
                            stopSound();
                            mPlayer = MediaPlayer.create(SelectData.this, R.raw.luna);
                            break;
                        case 3:
                            stopSound();
                            mPlayer = MediaPlayer.create(SelectData.this, R.raw.sedna);
                            break;
                        case 4:
                            stopSound();
                            mPlayer = MediaPlayer.create(SelectData.this, R.raw.titania);
                            break;
                    }

                    mPlayer.start();
                    ringtone = ringtones[i];
                } else if (settingsPicker == 1) {
                    chosenSettings = setTimer;
                    intent.putExtra(CustomCaller.Extra_Set_Timer, setTimer[i]);
                    intent.putExtra(CustomCaller.Extra_Set_Timer_Seconds, timeInSeconds[i]);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (settingsPicker == 0) {
            Intent intent = new Intent();
            intent.putExtra(CustomCaller.Extra_Ringtone, ringtone);
            setResult(RESULT_OK, intent);
            stopSound();
            finish();

            super.onBackPressed();
        }
    }

    public void stopSound(){
        if(mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
        }
    }
}
