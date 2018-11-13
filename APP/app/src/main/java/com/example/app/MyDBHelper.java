package com.example.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import okhttp3.Callback;

/**
 * Created by 笙笙 on 2017/7/24.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE localDB (" +
                "_id UNSIGNED INT(10) PRIMARY KEY, " +
                "time TEXT," +
                "address TEXT," +
                "posture1 BOOLEAN," +
                "posture2 BOOLEAN," +
                "posture3 BOOLEAN," +
                "posture4 BOOLEAN," +
                "distance TEXT," +
                "focus_ratio DOUBLE(3,1)" + ")");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        final String SQL = "DROP TABLE localDB";
        db.execSQL(SQL);
        onCreate(db);
    }


}
