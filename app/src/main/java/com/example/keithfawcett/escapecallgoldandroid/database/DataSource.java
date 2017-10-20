package com.example.keithfawcett.escapecallgoldandroid.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keithfawcett.escapecallgoldandroid.Callers;

import java.util.ArrayList;
import java.util.List;

public class DataSource {


    private SQLiteDatabase mDatabase;
    private final SQLiteOpenHelper mDbHelper;

    public DataSource(Context context) {
        mDbHelper = new DBHelper(context);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();

    }

    public void close() {
        mDbHelper.close();

    }

    public void addCaller(Callers callers){
        ContentValues values = callers.toValues();
        mDatabase.insert(CallerTable.TABLE_CALLERS, null, values);
    }

    public void deleteCaller(Callers callers){
        mDatabase.delete(CallerTable.TABLE_CALLERS, CallerTable.COLUMN_NAME + "=?", new String[]{callers.getCallerName()});
    }


    public List<Callers> getAllItems(){
        List<Callers> articleItems = new ArrayList<>();
        Cursor cursor = mDatabase.query(CallerTable.TABLE_CALLERS, CallerTable.ALL_COLUMNS, null, null, null, null, null);

        while (cursor.moveToNext()){
            Callers callers = new Callers();
            callers.setCallerId(cursor.getString(cursor.getColumnIndex(CallerTable.COLUMN_ID)));
            callers.setCallerName(cursor.getString(cursor.getColumnIndex(CallerTable.COLUMN_NAME)));
            callers.setCallerTimeCounter(cursor.getInt(cursor.getColumnIndex(CallerTable.COLUMN_TIMECOUNTER)));
            callers.setCallerTimer(cursor.getString(cursor.getColumnIndex(CallerTable.COLUMN_TIMER)));
            callers.setCallerRingtone(cursor.getString(cursor.getColumnIndex(CallerTable.COLUMN_RINGTONE)));
            callers.setCallerVoice(cursor.getString(cursor.getColumnIndex(CallerTable.COLUMN_VOICE)));
            callers.setCallerImage(cursor.getString(cursor.getColumnIndex(CallerTable.COLUMN_IMAGE)));
            articleItems.add(callers);
        }
        cursor.close();
        return articleItems;
    }
}
