package com.groupm.sprinter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Jerick.Duguran on 5/31/2016.
 */

public class SprintAdapter extends CursorAdapter
{
    public SprintAdapter(Context context, Cursor c, int flags)
    {
        super(context, c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.activity_sprint_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        try {
            TextView startView       = (TextView) view.findViewById(R.id.start);
            String start  = cursor.getString(cursor.getColumnIndex("start"));
            startView.setText(start);

            TextView endView       = (TextView) view.findViewById(R.id.end);
            String end  = cursor.getString(cursor.getColumnIndex("end"));
            endView.setText(end);

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}