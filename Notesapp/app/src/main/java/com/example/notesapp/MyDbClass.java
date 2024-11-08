package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDbClass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="notedata";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="notetable";
    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_SUBTITLE="subtitle";
    private static final String KEY_CONTENT="content";
    public MyDbClass(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_TITLE+" TEXT,"+KEY_SUBTITLE+" TEXT,"+KEY_CONTENT+" TEXT"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    public void addValue(String title, String subtitle, String content) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Get the current maximum ID
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT MAX(id) FROM " + TABLE_NAME, null);
        int nextId = 1; // Default to 1 if no rows are present
        if (cursor.moveToFirst()) {
            int maxId = cursor.getInt(0);
            nextId = maxId + 1; // Set the new ID to one greater than the current max ID
        }
        cursor.close();

        // Insert the new row with the calculated ID
        contentValues.put(KEY_ID, nextId);
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_SUBTITLE, subtitle);
        contentValues.put(KEY_CONTENT, content);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        logAllIds();
    }

    public void updateData(int id,String title,String subtitle,String content){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_TITLE,title);
        contentValues.put(KEY_SUBTITLE,subtitle);
        contentValues.put(KEY_CONTENT,content);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{String.valueOf(id)});
        Log.d("updateData",String.valueOf(id));
    }
    public void deleteData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        Log.d("deleteData", "Deleted ID: " + id);
        database.execSQL("UPDATE " + TABLE_NAME + " SET id = id - 1 WHERE id > " + id);

        Log.d("deleteData", "Rearranged IDs after deletion of ID: " + id);
        logAllIds();
    }

    public individualNoteItem showData(int id){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE ID = "+id,null);
        Log.d("showData",String.valueOf(id));
        individualNoteItem obj=new individualNoteItem();
        if(cursor!=null){
            if(cursor.moveToFirst()){
                String title=cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
                String subtitle=cursor.getString(cursor.getColumnIndexOrThrow(KEY_SUBTITLE));
                String content=cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT));
                obj=new individualNoteItem(title,subtitle,content);
            }
            cursor.close();
        }
        database.close();
        logAllIds();
        return obj;

    }
    public void logAllIds() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT id FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                Log.d("logAllIds", "ID: " + id);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
    }

    public List<individualNoteItem> getAll(){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        List<individualNoteItem> arrayList=new ArrayList<>();
        if(cursor!=null){
            while(cursor.moveToNext()){
                String title=cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
                String subtitle=cursor.getString(cursor.getColumnIndexOrThrow(KEY_SUBTITLE));
                String content=cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT));
                arrayList.add(new individualNoteItem(title,subtitle,content));
            }
            cursor.close();
        }
        database.close();
        return arrayList;
    }
}

