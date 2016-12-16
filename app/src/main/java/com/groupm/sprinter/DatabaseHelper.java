package com.groupm.sprinter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jerick.Duguran on 4/12/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DBNAME = "sprint_activity.db";
    private static final int VERSION   = 1;

    public DatabaseHelper(Context context)
    {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void createTables(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE sprint (id integer primary key autoincrement, start DATETIME, end DATETIME)");
        db.execSQL("CREATE TABLE sprint_track (id integer primary key autoincrement,sprint_id integer, reach_time DATETIME, latitude varchar(255), longitude varchar(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SprintCursor getSprints()
    {
        Cursor wrapper = getReadableDatabase().rawQuery("select id _id,* from sprint  ORDER BY start ASC", null);

        return new SprintCursor(wrapper);
    }

    public long newSprint(String lat, String lng)
    {
        ContentValues sprint = new ContentValues();
        sprint.put("start", getDateTime());
        long sprint_id =  getWritableDatabase().insert("sprint", null, sprint);

        ContentValues  sprint_track= new ContentValues();
        sprint_track.put("latitude", lat);
        sprint_track.put("longitude", lng);
        sprint_track.put("reach_time", getDateTime());
        sprint_track.put("sprint_id", sprint_id);

        getWritableDatabase().insert("sprint_track", null, sprint_track);

        return sprint_id;
    }

    public void endSprint(Long sprint_id,String lat, String lng)
    {
        ContentValues row = new ContentValues();
        row.put("end", getDateTime());
        getWritableDatabase().update("sprint", row, "id = ?", new String[]{Long.toString(sprint_id)});

        ContentValues  sprint_track= new ContentValues();
        sprint_track.put("latitude", lat);
        sprint_track.put("longitude", lng);
        sprint_track.put("reach_time", getDateTime());
        sprint_track.put("sprint_id", sprint_id);
        getWritableDatabase().insert("sprint_track", null, sprint_track);
        getWritableDatabase().close();

    }


    public void addPoint(Long sprint_id,String lat, String lng)
    {
        ContentValues  sprint_track= new ContentValues();
        sprint_track.put("latitude", lat);
        sprint_track.put("longitude", lng);
        sprint_track.put("reach_time", getDateTime());
        sprint_track.put("sprint_id", sprint_id);
        Long id = getWritableDatabase().insert("sprint_track", null, sprint_track);
        Log.d("NEW TRACK",Long.toString(id));
        getWritableDatabase().close();

    }



    public SprintTrackCursor getTracks(Long sprint_id)
    {
        Cursor wrapper = getReadableDatabase().rawQuery("select id _id,* from sprint_track WHERE sprint_id= " + sprint_id+ " ORDER BY reach_time ASC", null);

        return new SprintTrackCursor(wrapper);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
