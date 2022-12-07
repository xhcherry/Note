package com.example.note;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.note.util.MysqliteOpenHelper;

public class App extends Application {
    public SQLiteDatabase db;
    private MysqliteOpenHelper helper;

    @Override
    public void onCreate() {
        super.onCreate();
        helper = new MysqliteOpenHelper(this, "notes", null, 1);
        db = helper.getReadableDatabase();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
