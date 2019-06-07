package com.example.practiseitmogooglemapapi.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseMainModule extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database";
    public static final String TABLE_NAME = "MainTable";

    public static final String KEY_ID = "_id";
    public static final String KEY_X = "XVal";
    public static final String KEY_Y = "YVal";
    public static final String KEY_VALUE = "StrValue";

    public DatabaseMainModule(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + KEY_ID + " integer primary key, " + KEY_X + " real," + KEY_Y + " real," + KEY_VALUE + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
