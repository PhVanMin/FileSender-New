package com.example.xender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.xender.model.FileCloud;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FileCloudDatabaseHandler extends LocalDatabaseHandler<FileCloud> {


    private static final String DATABASE_NAME = "xenderManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "file_clouds";

    public static String TAG = "Database Handler";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_URI = "uri";
    private static final String KEY_DATE = "date_create";
    public FileCloudDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void add(FileCloud _fileCloud) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,_fileCloud.getName());
        values.put(KEY_URI,_fileCloud.getUri());
        values.put(KEY_DATE, _fileCloud.getTime().getTime());
        db.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addFileCloud: ");
        db.close();
    }

    @Override
    public void delete(int fileCloudId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(fileCloudId) });
        Log.d(TAG, "deleteFileCloud: ");
        db.close();
    }

    @Override
    public FileCloud getById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        FileCloud fileCloud = new FileCloud(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                new Timestamp(cursor.getLong(3)));
        Log.d(TAG, "getFileCloud: ");
        return fileCloud;
    }

    @Override
    public List<FileCloud> getAll() {
        List<FileCloud>  fileCloudList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            FileCloud fileCloud = new FileCloud(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    new Timestamp(cursor.getLong(3)));
            fileCloudList.add(fileCloud);
            cursor.moveToNext();
        }
        Log.d(TAG, "getAllFileClouds: ");
        return fileCloudList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create table: ");
        String tableString = String.format(
                "CREATE TABLE file_clouds (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "uri TEXT," +
                        "date_create DATETIME" +
                        ");");
        String tableString1 = String.format(
                "CREATE TABLE file_sends (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "file_name TEXT," +
                        "file_path TEXT," +
                        "receive_address TEXT," +
                        "date_create DATETIME," +
                        "is_send INTEGER" +
                        ");"
        );
        db.execSQL(tableString);
        db.execSQL(tableString1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tableString  = "DROP TABLE IF EXISTS file_upload";
        db.execSQL(tableString);
        onCreate(db);
    }
}
