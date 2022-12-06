package com.example.fragmentnote;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import com.example.fragmentnote.util.MysqliteOpenHelper;

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
