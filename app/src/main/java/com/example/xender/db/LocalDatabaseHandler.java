package com.example.xender.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.database.sqlite.SQLiteDatabaseKt;

import java.util.List;

public abstract  class LocalDatabaseHandler<T> extends SQLiteOpenHelper {
    public LocalDatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public abstract void add(T t);
    public abstract void delete(int id);
    public abstract T getById(int id);
    public abstract List<T> getAll();


}
