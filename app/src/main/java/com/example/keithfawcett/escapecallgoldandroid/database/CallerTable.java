package com.example.keithfawcett.escapecallgoldandroid.database;



public class CallerTable {
    public static final String TABLE_CALLERS = "callers";
    public static final String COLUMN_ID = "callerId";
    public static final String COLUMN_NAME = "callerName";
    public static final String COLUMN_TIMECOUNTER = "callerTimeCounter";
    public static final String COLUMN_TIMER = "callerTimer";
    public static final String COLUMN_RINGTONE = "callerRingtone";
    public static final String COLUMN_VOICE = "callerVoice";
    public static final String COLUMN_IMAGE = "callerImage";



    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_NAME, COLUMN_TIMECOUNTER, COLUMN_TIMER, COLUMN_RINGTONE, COLUMN_VOICE, COLUMN_IMAGE };

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_CALLERS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_TIMECOUNTER + " INTEGER," +
                    COLUMN_TIMER + " TEXT," +
                    COLUMN_RINGTONE + " TEXT," +
                    COLUMN_VOICE + " TEXT," +
                    COLUMN_IMAGE + " TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_CALLERS;
}


