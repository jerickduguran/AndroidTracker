package com.groupm.sprinter;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by Jerick.Duguran on 4/12/2016.
 */
public class SprintCursor extends CursorWrapper {

    public SprintCursor(Cursor cursor)
    {
        super(cursor);
    }

    public String getStart(int position)
    {
        moveToPosition(position);
        String name = getString(getColumnIndex("start"));

        return name;
    }
    public String getEnd(int position)
    {
        moveToPosition(position);
        String name = getString(getColumnIndex("end"));

        return name;
    }

    public Integer getId(int position)
    {
        moveToPosition(position);
        Integer id = getInt(getColumnIndex("id"));

        return id;
    }

}
