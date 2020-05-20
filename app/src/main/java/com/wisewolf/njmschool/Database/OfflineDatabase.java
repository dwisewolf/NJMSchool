package com.wisewolf.njmschool.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.OfflineVideos;

import java.util.ArrayList;

public class OfflineDatabase extends SQLiteOpenHelper {
    SQLiteDatabase dh;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "OfflineDB";

    public OfflineDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE_SITENAME = "CREATE TABLE tbl_item(id varchar PRIMARY KEY ,name varchar  ,salt_name varchar  ,dir_name varchar  ,inv_name varchar  ,location varchar ,userid varchar ,extra1 varcha,extra2 varchar   )";
        sqLiteDatabase.execSQL(CREATE_TABLE_SITENAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String insertinto(String name, String salt_name, String dir_name, String inv_name, String location, String user_id, String extra1, String extra2)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("[name]", name);
            values.put("[salt_name]", salt_name);
            values.put("[dir_name]", dir_name);
            values.put("[inv_name]", inv_name);
            values.put("[location]", location);
            values.put("[userid]", user_id);
            values.put("[extra1]", extra1);
            values.put("[extra2]", extra2);


            dh.insert("tbl_item", null, values);
            dh.close();
            return "y";

        } catch (Exception e) {
            return e.toString();
        }


    }

    public void OfflineVideoss() {
        dh = this.getReadableDatabase();
        GlobalData.OfflineVideos=null;
        String select = "select * from tbl_item  WHERE userid ='"+GlobalData.regno+"'";
        Cursor cursor = dh.rawQuery(select, null);
        ArrayList offlinelist=new ArrayList<OfflineVideos>();

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String name; String salt_name; String dir_name; String inv_name; String location; String user_id; String extra1; String extra2 ;
                name=cursor.getString(cursor.getColumnIndex("name"));
                salt_name=cursor.getString(cursor.getColumnIndex("salt_name"));
                dir_name=cursor.getString(cursor.getColumnIndex("dir_name"));
                inv_name=cursor.getString(cursor.getColumnIndex("inv_name"));
                location=cursor.getString(cursor.getColumnIndex("location"));
                user_id=cursor.getString(cursor.getColumnIndex("userid"));
                extra1=cursor.getString(cursor.getColumnIndex("extra1"));
                extra2=cursor.getString(cursor.getColumnIndex("extra2"));
                OfflineVideos offlineVideos =new OfflineVideos(
                    name,salt_name,dir_name,inv_name,location,user_id,extra1,extra2
                );
                offlinelist.add(offlineVideos);



            }
            while (cursor.moveToNext());
            if (GlobalData.OfflineVideos==null){
                GlobalData.OfflineVideos=offlinelist;
            }
            else {
                GlobalData.OfflineVideos.addAll(offlinelist);
            }


        }

        dh.close();

    }

   public void deletetable(String name) {
        dh = this.getWritableDatabase();

        String query;


        query = "delete from tbl_item WHERE name ='"+name+"'";
        Log.e("Query", query);
        dh.execSQL(query);



    }

}
