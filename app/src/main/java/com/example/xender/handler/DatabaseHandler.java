package com.example.xender.handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "xenderManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "file_clouds";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableString = String.format(
                "CREATE TABLE file_upload (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "uri TEXT," +
                        "date_create DATETIME" +
                        ");"
        );
        db.execSQL(tableString);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tableString  = "DROP TABLE IF EXISTS file_upload";
        db.execSQL(tableString);

        onCreate(db);
    }

}
