package com.example.BudgetDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Budget.db";
    public static final String TABLE_NAME = "Budget_Table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Date";
    public static final String COL_3 = "Item";
    public static final String COL_4 = "Rate";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 13);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,Date TEXT,Item TEXT,Rate INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String Date,String Item,String Rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,Date);
        contentValues.put(COL_3,Item);
        contentValues.put(COL_4,Rate);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getbyDate(String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=null;
        res = db.rawQuery("select * from "+TABLE_NAME+ " WHERE "+COL_2+"= ?"
                ,new String[] {Date});
        if (res==null)
            return null;
        else
        return res;
    }
    public Cursor getSelectedData(String Date,String Item,String Rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=null;
             res = db.rawQuery("select * from "+TABLE_NAME+ " WHERE "+COL_2+"= ?" +" AND "+COL_3+"= ?"
                             +" AND "+COL_4+"= ?"
                    ,new String[] {Date,Item,Rate});
        return res;
    }

    public boolean updateData(String ID,String Date,String Item,String Rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,ID);
        contentValues.put(COL_2,Date);
        contentValues.put(COL_3,Item);
        contentValues.put(COL_4,Rate);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { ID });
        return true;
    }

    public void deleteData(String id, MonthBudget monthBudget) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}