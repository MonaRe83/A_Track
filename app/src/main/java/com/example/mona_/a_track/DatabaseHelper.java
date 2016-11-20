package com.example.mona_.a_track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    // SQL local data.
// base

public class DatabaseHelper extends SQLiteOpenHelper {

    // Declare the name for the database and columns
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "ass_tracker.db";
    public static final String TABLE_NAME = "userinfo";
    public static final String COLUMN_id= "id";
    public static final String COLUMN_password= "password";
    public static final String COLUMN_email= "email";
    public static final String COLUMN_name= "name";
    public static final String COLUMN_username= "username";

    SQLiteDatabase db ;
    // Execute query and create table
    private static final String TABLE_CREATE = "create table  userinfo (id integer primary key not null," +
            "name text not null ,email text not null,username text not null,password text not null);";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db= db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    // Getting data using a query to pull userinfo
    public void insertUserInfo(UserInfo ui) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String query = "SELECT * FROM "+TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);
        int count  = cursor.getCount();

        values.put(COLUMN_id,count);
        values.put(COLUMN_name, ui.getName());
        values.put(COLUMN_email, ui.getEmail());
        values.put(COLUMN_username, ui.getUsername());
        values.put(COLUMN_password, ui.getPassword());


        db.insert(TABLE_NAME,null,values);
        db.close();
    }
        //  Query to find user name and password for login
    public String searchPass(String username) {
        db = this.getReadableDatabase();
        String query  = "SELECT username, password FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String a ,b ;
        b = "not found";
        if (cursor.moveToFirst()){

            do{
                a = cursor.getString(0);

                if  (a.equals(username)){

                    b = cursor.getString(1);
                    break;
                }
            }while (cursor.moveToNext());
        }
        return b;
    }

    //Call function to create database
    public DatabaseHelper(Context context ,String name , SQLiteDatabase.CursorFactory factory,int version) {
        super(context,DATABASE_NAME, factory,DATABASE_VERSION);
    }

}

