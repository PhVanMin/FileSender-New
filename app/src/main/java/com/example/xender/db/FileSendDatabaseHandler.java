package com.example.xender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.xender.model.FileCloud;
import com.example.xender.model.FileSend;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FileSendDatabaseHandler extends LocalDatabaseHandler<FileSend> {
    private static final String DATABASE_NAME = "xenderManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "file_sends";

    private static final String TAG = "FileSendDatabaseHandler";

    private static final String KEY_ID = "id";
    private static final String KEY_FILE_NAME = "file_name";
    private static final String KEY_FILE_PATH = "file_path";
    private static final String KEY_RECEIVE_ADDRESS = "receive_address";
    private static final String KEY_IS_SEND = "is_send";
    private static final String KEY_DATE = "date_create";

    public FileSendDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void add(FileSend fileSend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FILE_NAME, fileSend.getFileName());
        values.put(KEY_FILE_PATH, fileSend.getFilePath());
        values.put(KEY_RECEIVE_ADDRESS, fileSend.getReceiveAddress());
        values.put(KEY_DATE, fileSend.getTime().getTime());
        values.put(KEY_IS_SEND, fileSend.getIsSend() ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addFileSend: ");
        db.close();
    }

    @Override
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "deleteFileSend: ");
        db.close();
    }

    @Override
    public FileSend getById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        boolean isSend = cursor.getInt(cursor.getColumnIndex(KEY_IS_SEND)) == 1;
        FileSend fileSend = new FileSend(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_FILE_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH)),
                cursor.getString(cursor.getColumnIndex(KEY_RECEIVE_ADDRESS)),
                new Timestamp(cursor.getLong(cursor.getColumnIndex(KEY_DATE))),
                isSend);
        Log.d(TAG, "getFileSend: ");
        cursor.close();
        return fileSend;
    }

    @Override
    public List<FileSend> getAll() {
        List<FileSend> fileSendList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                boolean isSend = cursor.getInt(cursor.getColumnIndex(KEY_IS_SEND)) == 1;
                FileSend fileSend = new FileSend(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_FILE_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH)),
                        cursor.getString(cursor.getColumnIndex(KEY_RECEIVE_ADDRESS)),
                        new Timestamp(cursor.getLong(cursor.getColumnIndex(KEY_DATE))),
                        isSend);
                fileSendList.add(fileSend);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d(TAG, "getAllFileSends: " + fileSendList.size());
        return fileSendList;
    }
    public boolean isDatabaseExists(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return ((File) dbFile).exists();
    }

    public boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (cursor == null) {
            return false;
        }
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create tables: ");
        String fileSendsTableString = String.format(
                "CREATE TABLE file_sends (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "file_name TEXT," +
                        "file_path TEXT," +
                        "receive_address TEXT," +
                        "date_create DATETIME," +
                        "is_send INTEGER" +
                        ");"
        );

        String fileCloudsTableString = String.format(
                "CREATE TABLE file_clouds (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "uri TEXT," +
                        "date_create DATETIME" +
                        ");"
        );

        String tableString = String.format(
                "CREATE TABLE bluetooth_device (" +
                        "name TEXT," +
                        "address TEXT PRIMARY KEY," +
                        "date_create DATETIME" +
                        ");"
        );
        db.execSQL(tableString);

        // Thực thi câu lệnh tạo bảng cho bảng file_sends
        db.execSQL(fileSendsTableString);

        // Thực thi câu lệnh tạo bảng cho bảng file_clouds
        db.execSQL(fileCloudsTableString);

        if(isTableExists(db, "file_clouds"))
        {
            Log.d(TAG, "file clouds ok");
        }
        else {
            Log.d(TAG, "file clouds not ok");
        }

        if(isTableExists(db, "file_sends"))
        {
            Log.d(TAG, "file sends ok");
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tableString  = "DROP TABLE IF EXISTS file_sends";
        db.execSQL(tableString);
        onCreate(db);
    }
}
