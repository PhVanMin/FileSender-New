package com.example.xender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.xender.model.ConnectedBluetoothDevice;
import com.example.xender.model.FileCloud;
import com.example.xender.model.FileSend;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceDatabaseHandler extends LocalDatabaseHandler<ConnectedBluetoothDevice> {
    private static final String DATABASE_NAME = "xenderManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bluetooth_device";

    public static String TAG = "Database Handler";

    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DATE = "date_create";
    public BluetoothDeviceDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void add(ConnectedBluetoothDevice connectedBluetoothDevice) {
        if(getByAddress(connectedBluetoothDevice.getAddress()) != null)
        {
            Log.d(TAG, "addBluetooth Device Exist: ");
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,connectedBluetoothDevice.getName());
        values.put(KEY_ADDRESS,connectedBluetoothDevice.getAddress());
        values.put(KEY_DATE, connectedBluetoothDevice.getTime().getTime());
        db.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addBluetooth Device: ");
        db.close();
    }

    public  ConnectedBluetoothDevice getByAddress(String address){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ADDRESS + " = ?", new String[] {address},null, null, null);
        if(cursor != null) {
            if (cursor.moveToFirst() == false){
                return null;
            }
        }


        ConnectedBluetoothDevice device = new ConnectedBluetoothDevice(
                cursor.getString(0),
                cursor.getString(1),
                new Timestamp(cursor.getLong(2))
                );
        Log.d(TAG, "getBluetooth: ");
        return device;


    }

    @Override
    public void delete(int id) {

    }

    @Override
    public ConnectedBluetoothDevice getById(int id) {
        return null;
    }

    @Override
    public List<ConnectedBluetoothDevice> getAll() {
        List<ConnectedBluetoothDevice> deviceList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ConnectedBluetoothDevice device = new ConnectedBluetoothDevice(
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        new Timestamp(cursor.getLong(cursor.getColumnIndex(KEY_DATE)))
                    );
                deviceList.add(device);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d(TAG, "getAllbluetooth: " + deviceList.size());
        return deviceList;

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




}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tableString  = "DROP TABLE IF EXISTS bluetooth_device";
        db.execSQL(tableString);
        onCreate(db);
    }
}
