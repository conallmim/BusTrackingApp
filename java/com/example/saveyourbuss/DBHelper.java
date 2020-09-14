package com.example.saveyourbuss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    Context context;
    public final static String D_NAME = "Busslocation.db";
    public final static String TABLE = "Table2";
    public final static int VERSION = 2;
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String FEED = "feed";
    public final static String LAT = "lat";
    public final static String LAN = "lan";

    public DBHelper(Context context) {
        super(context, D_NAME, null, VERSION);
        sqLiteDatabase = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String TABLE_QUERY = " CREATE TABLE " + TABLE + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, lat TEXT,lan  TEXT, name TEXT,feed TEXT)";
        sqLiteDatabase.execSQL(TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);

    }

    public void addData(double lat, double lan, String name, String email) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LAT, lat);
        cv.put(LAN, lan);
        cv.put(NAME, name);
        cv.put(FEED, email);
        //long value;
        //value =
        sqLiteDatabase.insert(TABLE, null, cv);
//        if(value==-1){
//            return 0;
//        }
//        else
//            return 1;

    }

    public Cursor getData() {
        sqLiteDatabase = this.getReadableDatabase();
        String S_QUERY = " SELECT * FROM " + TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(S_QUERY, null);
        return cursor;
    }



}
