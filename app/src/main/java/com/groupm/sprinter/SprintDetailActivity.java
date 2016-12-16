package com.groupm.sprinter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Jerick on 6/2/2016.
 */
public class SprintDetailActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    protected DatabaseHelper dh;
    protected Long sprint_id;
    private PolylineOptions userPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sprint_id = extras.getLong("sprint_id");
            dh = new DatabaseHelper(SprintDetailActivity.this);


            LocationManager mLocationmanager = null;

            String gosProvider = LocationManager.GPS_PROVIDER;
            String networkProvider = LocationManager.NETWORK_PROVIDER;
            mLocationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap sprinter_map) {
        //draw points
        SprintTrackCursor sc = dh.getTracks(sprint_id);
        if(sc.getCount() >= 1) {
            userPaths = new PolylineOptions().width(5).color(Color.BLUE);
            while (sc.moveToNext()) {
                LatLng track = new LatLng(sc.getLatitude(), sc.getLongitude());
                if(sc.isFirst()) {
                    sprinter_map.addMarker(new MarkerOptions().position(track).title("Started " + sc.getReachTime().toString()));
                    sprinter_map.moveCamera(CameraUpdateFactory.newLatLngZoom(track, 16));
                }
                if(sc.isLast()) {
                    sprinter_map.addMarker(new MarkerOptions().position(track).title("Ended " + sc.getReachTime().toString()));
                }
                userPaths.add(new LatLng(sc.getLatitude(), sc.getLongitude()));
                sprinter_map.addPolyline(userPaths);
            }
        }
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
