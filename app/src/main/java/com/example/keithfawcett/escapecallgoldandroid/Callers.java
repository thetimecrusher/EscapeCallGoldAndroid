
// Keith Fawcett
// MDF3 - 1703
// Heroes

package com.example.keithfawcett.escapecallgoldandroid;


import android.content.ContentValues;

import com.example.keithfawcett.escapecallgoldandroid.database.CallerTable;

import java.io.Serializable;
import java.util.UUID;

public class Callers implements Serializable {



    private String callerId;
    private String callerName;
    private int callerTimeCounter;
    private String callerTimer;
    private String callerRingtone;
    private String callerVoice;
    private String callerImage;

    public Callers(){

    }



    public Callers(String id, String name, int timeCounter, String timer, String ringtone,String voice, String image){

        if (id == null){
            id = UUID.randomUUID().toString();
        }


        callerId = id;
        callerName = name;
        callerTimeCounter = timeCounter;
        callerTimer = timer;
        callerRingtone = ringtone;
        callerVoice = voice;
        callerImage = image;

    }


    @Override
    public String toString() {
        return callerName;
    }
    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public int getCallerTimeCounter() {
        return callerTimeCounter;
    }

    public void setCallerTimeCounter(int callerTimeCounter) {
        this.callerTimeCounter = callerTimeCounter;
    }

    public String getCallerTimer() {
        return callerTimer;
    }

    public void setCallerTimer(String callerTimer) {
        this.callerTimer = callerTimer;
    }

    public String getCallerRingtone() {
        return callerRingtone;
    }

    public void setCallerRingtone(String callerRingtone) {
        this.callerRingtone = callerRingtone;
    }

    public String getCallerVoice() {
        return callerVoice;
    }

    public void setCallerVoice(String callerVoice) {
        this.callerVoice = callerVoice;
    }


    public String getCallerImage() {
        return callerImage;
    }

    public void setCallerImage(String callerImage) {
        this.callerImage = callerImage;
    }


    public ContentValues toValues(){
        ContentValues values = new ContentValues(7);

        values.put(CallerTable.COLUMN_ID, callerId);
        values.put(CallerTable.COLUMN_NAME, callerName);
        values.put(CallerTable.COLUMN_TIMECOUNTER, callerTimeCounter);
        values.put(CallerTable.COLUMN_TIMER, callerTimer);
        values.put(CallerTable.COLUMN_RINGTONE, callerRingtone);
        values.put(CallerTable.COLUMN_VOICE, callerVoice);
        values.put(CallerTable.COLUMN_IMAGE, callerImage);
        return values;

    }



}
