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
import java.util.List;

public class OfflineDatabase extends SQLiteOpenHelper {
    SQLiteDatabase dh;
    // Database Version
    private static final int DATABASE_VERSION = 6;
    // Database Name
    private static final String DATABASE_NAME = "OfflineDB6";

    public OfflineDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE_SITENAME = "CREATE TABLE tbl_item(id varchar PRIMARY KEY ,name varchar  ,salt_name varchar  ,dir_name varchar  ,inv_name varchar  ,location varchar ,userid varchar ,extra1 varcha,extra2 varchar   )";
        sqLiteDatabase.execSQL(CREATE_TABLE_SITENAME);

        String CREATE_TABLE_DocName = "CREATE TABLE tbl_document(id varchar PRIMARY KEY ,name varchar  ,dir_name varchar ,location varchar ,userid varchar ,extra1 varchar  )";
        sqLiteDatabase.execSQL(CREATE_TABLE_DocName);

        String CREATE_TABLE_taskid = "CREATE TABLE tbl_taskid(id varchar PRIMARY KEY ,userid varchar ,taskid varchar  )";
        sqLiteDatabase.execSQL(CREATE_TABLE_taskid);

        String CREATE_TABLE_optionTest = "CREATE TABLE optionTest(id varchar PRIMARY KEY ,userid varchar ,optionId varchar  )";
        sqLiteDatabase.execSQL(CREATE_TABLE_optionTest);

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

    public String insertDocument(String name, String dir_name, String location, String userid, String extra1)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("[name]", name);

            values.put("[dir_name]", dir_name);
            values.put("[location]", location);
            values.put("[userid]", userid);
            values.put("[extra1]", extra1);


            dh.insert("tbl_document", null, values);
            dh.close();
            return "y";

        } catch (Exception e) {
            return e.toString();
        }


    }

    public String inserttaskID( String userid, String taskid)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("[userid]", userid);
            values.put("[taskid]", taskid);


            dh.insert("tbl_taskid", null, values);
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

    public String DocumentList(String Dname) {
        dh = this.getReadableDatabase();
        GlobalData.OfflineVideos=null;
        String select = "select * from tbl_document  WHERE name ='"+Dname+"'";
        Cursor cursor = dh.rawQuery(select, null);
        ArrayList offlinelist=new ArrayList<OfflineVideos>();

        if (cursor.moveToFirst()) {
            int i = 0;
            String name,   dir_name,   location,   userid,   extra1;
            do {


                name=cursor.getString(cursor.getColumnIndex("name"));
                dir_name=cursor.getString(cursor.getColumnIndex("dir_name"));
                location=cursor.getString(cursor.getColumnIndex("location"));
                userid=cursor.getString(cursor.getColumnIndex("userid"));
                extra1=cursor.getString(cursor.getColumnIndex("extra1"));




            }
            while (cursor.moveToNext());
            dh.close();
           return location;

        }
        else {
            dh.close();
            return "false";

        }



    }

    public String taskId(String userid1) {
        dh = this.getReadableDatabase();

        String select = "select * from tbl_taskid  WHERE userid ='"+userid1+"'";
        Cursor cursor = dh.rawQuery(select, null);
       String  taskid1="";
        String userid,   taskid="";
        if (cursor.moveToFirst()) {
            int i = 0;

            do {


                userid=cursor.getString(cursor.getColumnIndex("userid"));
                taskid1=cursor.getString(cursor.getColumnIndex("taskid"));

                taskid=taskid+taskid1+",";

            }
            while (cursor.moveToNext());
            dh.close();
            return taskid;

        }
        else {
            dh.close();
            return taskid;

        }



    }


    public void OfflineVideossFull() {
        dh = this.getReadableDatabase();
        GlobalData.OfflineVideos=null;
        String select = "select * from tbl_item ";
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

    public void deletetask(String userid,String taskid) {
        dh = this.getWritableDatabase();

        String query;


        query = "delete from tbl_taskid WHERE userid ='"+userid+"' and taskid='"+taskid+"'";
        Log.e("Query", query);
        dh.execSQL(query);



    }

    public String insertoptionTest( String userid, String test)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("[userid]", userid);
            values.put("[optionId]", test);


            dh.insert("optionTest", null, values);
            dh.close();
            return "y";

        } catch (Exception e) {
            return e.toString();
        }


    }

    public String getOptiontest(String userid) {
        dh = this.getReadableDatabase();

        String select = "select * from optionTest  WHERE userid ='"+userid+"'";
        Cursor cursor = dh.rawQuery(select, null);
        String test="";
        if (cursor.moveToFirst()) {
            int i = 0;

            do {

                test=cursor.getString(cursor.getColumnIndex("optionId"));


            }
            while (cursor.moveToNext());
            dh.close();
            return test;

        }
        else {
            dh.close();
            return test;

        }



    }

}
