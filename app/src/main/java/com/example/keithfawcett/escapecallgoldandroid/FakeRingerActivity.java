package com.example.keithfawcett.escapecallgoldandroid;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class FakeRingerActivity extends AppCompatActivity {


    Context mContext = this;

    Chronometer callTime;

    private ImageButton callActionButton;
    private ImageButton answer;
    private ImageButton decline;
    private ImageButton endCall;

    private ImageView contactPhoto;

    private ImageView ring;

    private TextView callStatus;

    private RelativeLayout main;

    private RelativeLayout callActionButtons;

    MediaPlayer mPlayer;

    private Vibrator vibrator;

    private PowerManager.WakeLock wakeLock;

    private ContentResolver contentResolver;

    private String contactImageString;

    final Handler handler = new Handler();

    private Runnable hangUP = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fake_ringer);

        Window window = getWindow();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);

        TextView callerName = (TextView) findViewById(R.id.callerName);

        TextView callerNumber = (TextView) findViewById(R.id.phoneNumber);

        final Animation ringExpandAnimation = AnimationUtils.loadAnimation(this, R.anim.ring_expand);

        final Animation ringShrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.ring_shrink);

        final Drawable bg2 = getDrawable(R.drawable.answered_bg);

        contactPhoto = (ImageView)findViewById(R.id.contactPhoto);

        contentResolver = getContentResolver();

        Resources resources = getResources();

        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "Tag");

        callActionButtons = (RelativeLayout)findViewById(R.id.callActionButtons);

        callActionButton = (ImageButton) findViewById(R.id.callActionButton);

        answer = (ImageButton) findViewById(R.id.callActionAnswer);

        decline = (ImageButton) findViewById(R.id.callActionDecline);

        endCall = (ImageButton) findViewById(R.id.endCall);

        callStatus = (TextView) findViewById(R.id.callStatus);

        main = (RelativeLayout) findViewById(R.id.main);

        ring = (ImageView) findViewById(R.id.ring);

        callTime = (Chronometer) findViewById(R.id.callTime);

        String name = Timer.finalCallerName;

        String number = Timer.finalCallerNumber;

        contactImageString = Timer.finalCallerImage;

        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        wakeLock.setReferenceCounted(false);

        nBuilder.setSmallIcon(R.drawable.ic_call);

        nBuilder.setOngoing(true);

        nBuilder.setContentTitle(name);

        nBuilder.setColor(Color.rgb(4, 137, 209));

        nBuilder.setContentText(resources.getString(R.string.incoming_call));


        setContactImage(true);

        callActionButton.setOnTouchListener(new View.OnTouchListener() {

            float x1 = 0, x2 = 0, y1 = 0, y2 = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int a = event.getAction();

                if (a == MotionEvent.ACTION_DOWN) {

                    x1 = event.getX();

                    y1 = event.getY();

                    ring.startAnimation(ringExpandAnimation);

                    answer.setVisibility(View.VISIBLE);

                    decline.setVisibility(View.VISIBLE);

                    callActionButton.setVisibility(View.INVISIBLE);

                } else if (a == MotionEvent.ACTION_MOVE) {

                    x2 = event.getX();

                    y2 = event.getY();

                    if ((x2 - 200) > x1) {

                        callActionButtons.removeView(callActionButton);

                        callActionButtons.removeView(ring);

                        callActionButtons.removeView(answer);

                        callActionButtons.removeView(decline);

                        handler.removeCallbacks(hangUP);

                        callStatus.setText("");

                        setContactImage(false);

                        stopRinging();

                        callTime.setVisibility(View.VISIBLE);
                        callTime.setBase(SystemClock.elapsedRealtime());
                        callTime.start();

                        main.setBackground(bg2);

                        endCall.setVisibility(View.VISIBLE);

                        wakeLock.acquire();

                        if(!Timer.finalCustomVoice.equals("")) {
                            switch (Timer.finalCustomVoice) {
                                case "Male":
                                    mPlayer = new MediaPlayer();
                                    try {
                                        mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                        mPlayer.setDataSource(mContext, Uri.parse("android.resource://" + getPackageName() + "/raw/male"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }try {
                                    mPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                    break;
                                case "Female":
                                    mPlayer = new MediaPlayer();
                                    try {
                                        mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                        mPlayer.setDataSource(mContext, Uri.parse("android.resource://" + getPackageName() + "/raw/female"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }try {
                                    mPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                    break;
                                default:
                                    mPlayer = new MediaPlayer();
                                    try {
                                        mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                        mPlayer.setDataSource(Timer.finalCustomVoice);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mPlayer.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }



                            mPlayer.start();
                        }


                    }

                    if ((x2 + 200) < x1) {

                        stopRinging();

                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);


                    }



                } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL) {

                    answer.setVisibility(View.INVISIBLE);

                    decline.setVisibility(View.INVISIBLE);

                    ring.startAnimation(ringShrinkAnimation);

                    callActionButton.setVisibility(View.VISIBLE);

                }

                return false;

            }
        });

        Animation animCallStatusPulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.call_status_pulse);

        callStatus.startAnimation(animCallStatusPulse);

        callerName.setText(name);
        callerNumber.setText(number);

        switch (Timer.finalCallerRingtone) {
            case "atria":
                mPlayer = MediaPlayer.create(FakeRingerActivity.this, R.raw.atria);
                break;
            case "dione":
                mPlayer = MediaPlayer.create(FakeRingerActivity.this, R.raw.dione);
                break;
            case "luna":
                mPlayer = MediaPlayer.create(FakeRingerActivity.this, R.raw.luna);
                break;
            case "sedna":
                mPlayer = MediaPlayer.create(FakeRingerActivity.this, R.raw.sedna);
                break;
            case "titania":
                mPlayer = MediaPlayer.create(FakeRingerActivity.this, R.raw.titania);
                break;
            default:
                mPlayer = MediaPlayer.create(FakeRingerActivity.this, R.raw.atria);
        }



        mPlayer.setLooping(true);
        mPlayer.start();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        vibrator.vibrate(pattern, 0);

    }

    private void setContactImage(boolean tint) {

        if (!(contactImageString == null)) {

            Uri contactImageUri = Uri.parse(contactImageString);



            try {

                InputStream contactImageStream = contentResolver.openInputStream(contactImageUri);

                Drawable contactImage = Drawable.createFromStream(contactImageStream, contactImageUri.toString());

                if(tint) {
                    contactImage.setTint(getResources().getColor(R.color.contact_photo_tint));
                    contactImage.setTintMode(PorterDuff.Mode.DARKEN);
                }

                contactPhoto.setImageDrawable(contactImage);

            } catch (Exception e) {
                e.printStackTrace();
                contactPhoto.setImageBitmap(BitmapFactory.decodeFile(Timer.finalCallerImage));
            }


        }
    }


    public void onClickEndCall(View view) {

        mPlayer.stop();
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);

    }


    private void stopRinging() {

        vibrator.cancel();

        mPlayer.stop();

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        wakeLock.release();

        stopRinging();

    }

    @Override
    public void onBackPressed() {

    }

}
