package com.sumitdev.voicedo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="voicedo.db";
    public static final String TABLE_NAME="voicenotes";


    public SqliteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table voicenotes (id integer primary key autoincrement,todotitle text,voicepath text,bool text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists voicenotes");
        onCreate(db);

    }
    public long insertdata(String title,String path,String booleann)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("todotitle",title);
        values.put("voicepath",path);
        values.put("bool",booleann);
       long rowid=database.insert("voicenotes",null,values);
       return rowid;





    }
    public Cursor retrivedata()
    {
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("select*from voicenotes",null);
        return cursor;

    }
    public void updatedata(int id,String bool)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("bool",bool);

       db.update("voicenotes",cv,"ID = ?",new String[] {String.valueOf(id)});


    }
    public void deletedata(int  id)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        sqLiteDatabase.delete("voicenotes","ID = ?",new String[] {String.valueOf(id)});



    }
}
