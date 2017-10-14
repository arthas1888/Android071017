package com.cajalopez.apimapsapplication.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

public class DBHelper extends SQLiteOpenHelper{

    private final static int VERSION_DB = 2;
    private final static String DB_NAME = "test";

    public final static String TABLE_NAME = "chuck_norris";
    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_SERVER_ID = "id";
    public final static String COLUMN_NAME = "joke";
    public final static String COLUMN_CAT = "categories";
    //private final SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION_DB);
        //
    }

    public void deleteRows(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "(" + COLUMN_ID + " = %s)";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(TABLE_NAME, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " integer PRIMARY KEY not null, "
                + COLUMN_SERVER_ID + " INTEGER, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_CAT + " TEXT "
                + ")";
        db.execSQL(query);
        Logger.d("Base de datos creada, %s", TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }
}
