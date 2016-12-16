package com.groupm.sprinter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Jerick on 5/31/2016.
 */
public class SprintActivity extends AppCompatActivity {

    SprintCursor sc;
    ListView sprint_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint);

        sprint_list          = (ListView) findViewById(R.id.sprint_list);
        DatabaseHelper    dh = new DatabaseHelper(SprintActivity.this);
        sc = dh.getSprints();

        SprintAdapter sprintAdapter   = new SprintAdapter(this,sc,0);
        sprint_list.setAdapter(sprintAdapter);

        sprint_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), SprintDetailActivity.class);
                i.putExtra("sprint_id",id);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.activity_list:
                Intent sprint_list = new Intent(this, SprintActivity.class);
                startActivity(sprint_list);
                break;
            case R.id.start_activity:
                Intent intent = new Intent(this, Bootstrap.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
