package com.example.fragmentnote.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MysqliteOpenHelper extends SQLiteOpenHelper {
    private final String CREATE_ARTICLE = "create table article(" +
            "id integer primary key autoincrement," +
            "title text," +
            "avatar text," +
            "contentMd text,"+
            "isSecret Integer," +
            "isStick Integer," +
            "isOriginal Integer," +
            "quantity Integer," +
            "createTime text," +
            "isPublish Integer," +
            "categoryName text," +
            "tagNames text)";

    public MysqliteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
