package com.example.myapplication.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.myapplication.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class    MyDbManager {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }
    public void openDb(){
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToBd(String title, String desc, String uri){
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESC, desc);
        cv.put(MyConstants.URI, uri);
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    public void delete(int id){
        String selection = MyConstants._ID + "=" + id;
        db.delete(MyConstants.TABLE_NAME, selection, null);
    }

    public void update(String title, String desc, String uri, int id ){
        String selection = MyConstants._ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESC, desc);
        cv.put(MyConstants.URI, uri);
        db.update(MyConstants.TABLE_NAME, cv, selection, null);
    }


    public void getFromDb(String searchText, OnDataReceived onDataReceived){
        List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstants.TITLE + " like ?";
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, selection,
                new String[]{"%" + searchText + "%"}, null, null, null);

        while(cursor.moveToNext()){
            ListItem item = new ListItem();
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(MyConstants.DESC));
            String uri = cursor.getString(cursor.getColumnIndex(MyConstants.URI));
            int id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            item.setId(id);
            item.setTitle(title);
            item.setDesc(description);
            item.setUri(uri);
            tempList.add(item);

            //tempList.add(description);
        }
        cursor.close();
        onDataReceived.onReceived(tempList);
    }
    public void closeDb() {
        myDbHelper.close();
    }
}
