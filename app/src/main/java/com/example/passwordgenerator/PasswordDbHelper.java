package com.example.passwordgenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PasswordDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "passwords.db"; //it'll store passwords
    private static final int DATABASE_VERSION = 1;

    //below table name and row column variable.
    public static final String TABLE_PASSWORDS = "passwords";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PASSWORD = "password";


    //creating table here.
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PASSWORDS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL);";

    public PasswordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        onCreate(db);
    }
}
