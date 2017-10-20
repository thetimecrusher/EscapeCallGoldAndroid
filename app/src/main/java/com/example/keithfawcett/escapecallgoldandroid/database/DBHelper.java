package com.example.keithfawcett.escapecallgoldandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keithfawcett.escapecallgoldandroid.database.CallerTable;


class DBHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "callers.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CallerTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CallerTable.SQL_DELETE);
        onCreate(db);
    }
}
