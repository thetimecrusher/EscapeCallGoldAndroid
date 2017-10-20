package com.example.keithfawcett.escapecallgoldandroid;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class Timer extends AppCompatActivity {

    Context mContext = this;

    static String finalCallerName = "Dad";
    String finalCallerImageString;
    static String finalCallerImage;
    static String finalCallerRingtone = "atria";
    static String finalCustomVoice = "";

    static CountDownTimer countDownTimer;

    TextView timerTextView;

    private Long startTime;
    private Long stopTime = 0L;
    private Long countDownTimeLeft = 0L;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        int timer = getIntent().getIntExtra(CallSettings.Extra_Set_Timer,0);
        countDownTimeLeft = (long) timer;
        finalCallerName = getIntent().getStringExtra(CallSettings.Extra_Final_Callers_Name);
        finalCallerImageString = getIntent().getStringExtra(CallSettings.Extra_Image);
        finalCallerRingtone = getIntent().getStringExtra(CallSettings.Extra_Ringtone);
        finalCustomVoice = getIntent().getStringExtra(CallSettings.Extra_Custom_Voice);


        finalCallerImage = finalCallerImageString;
        timerTextView = (TextView) findViewById(R.id.timerTextView);


        countDownTimer = new CountDownTimer(countDownTimeLeft * 1000, 1000) {
            @Override
            public void onTick(long l) {
                countDownTimeLeft = l / 1000;
                timerTextView.setText(String.format("%02d:%02d" ,countDownTimeLeft/60, countDownTimeLeft % 60));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(mContext, FakeRingerActivity.class);
                PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                screenLock.acquire();

                screenLock.release();
                startActivity(intent);
            }
        };

        countDownTimer.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = (System.currentTimeMillis() - startTime + stopTime);
            long seconds = millis / 1000;
            timerTextView.setText(String.format("%02d:%02d" ,seconds/60, seconds % 60));
            mHandler.postDelayed(mRunnable, 10L);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
        finish();
    }

}
