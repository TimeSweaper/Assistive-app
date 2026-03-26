package com.example.assitive;

import android.content.Context;
import android.database.sqlite.*;
import android.content.ContentValues;
import android.database.Cursor;


public class ADatabase extends  SQLiteOpenHelper{
    public static final String DB_NAME = "AssistiveDB";
    public static final int DB_VERSION = 2;

    public ADatabase(Context context){
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "phone TEXT," +
                "disability TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String password, String phone, String disability) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{email});

        if (cursor.getCount() > 0) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("phone", phone);
        values.put("disability", disability);

        long result = db.insert("users", null, values);

        return result != -1;
    }

    public boolean checkUser(String email,String password){
        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email=? AND password=?",
                new String[] {email,password}
        );
        return cursor.getCount() > 0;
    }

    public Cursor getUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM users WHERE email=?",
                new String[]{email}
        );
    }

    public boolean updateProfile(String email, String name, String phone, String disability) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("disability", disability);

        int rows = db.update("users", values, "email=?", new String[]{email});

        return rows > 0;
    }
}
