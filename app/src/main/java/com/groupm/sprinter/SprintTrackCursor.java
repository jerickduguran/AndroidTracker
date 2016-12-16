package com.groupm.sprinter;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by Jerick.Duguran on 5/31/2016.
 */
public class SprintTrackCursor extends CursorWrapper {

    public SprintTrackCursor(Cursor cursor)
    {
        super(cursor);
    }

    public Double getLatitude()
    {
        Double latitude = 0.0;
        try {
            latitude = Double.parseDouble(getString(getColumnIndex("latitude")));
            return latitude;
        }catch(Exception e){
            e.printStackTrace();
        }

        return latitude;
    }
    public Double getLongitude()
    {
        Double longitude = 0.0;
        try {
          longitude = Double.parseDouble(getString(getColumnIndex("longitude")));
        }catch(Exception e){
            e.printStackTrace();
        }
        return longitude;
    }

    public String getReachTime()
    {
        String reach_time = getString(getColumnIndex("reach_time"));

        return reach_time;
    }

    public Long getId()
    {
        Long id = getLong(getColumnIndex("id"));

        return id;
    }

}
