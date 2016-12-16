package com.groupm.sprinter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

public class Bootstrap extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap sprinter_map;
    private ArrayList<Circle> sprinter_map_circles;
    private Circle current_location = null;
    private Circle current_location_radius = null;
    private Button btnAction;
    private TextView timeLapse;
    private PolylineOptions userPaths;
    private Boolean started = false;
    private LatLng lastKnownLocation = null;
    private Long sprint_id;
    private DatabaseHelper dh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bootstrap);

        btnAction         = (Button)findViewById(R.id.start);
        //timeLapse         = (TextView)findViewById(R.id.timeLapse);
        dh = new DatabaseHelper(Bootstrap.this);

        LocationManager mLocationmanager = null;
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(started == true){
                    started = false;
                    btnAction.setText("Start");
                    sprinter_map.addMarker(new MarkerOptions().title("END POINT").position(lastKnownLocation));
                    userPaths = null;
                    dh.endSprint(sprint_id,Double.toString(lastKnownLocation.latitude),Double.toString(lastKnownLocation.longitude));
                }else {
                    started = true;
                    btnAction.setText("Stop");
                    userPaths = new PolylineOptions().width(5).color(Color.BLUE);
                    sprinter_map.addMarker(new MarkerOptions().title("START POINT").position(new LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)));
                    userPaths.add(new LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude));
                    sprinter_map.addPolyline(userPaths);
                    sprint_id = dh.newSprint(Double.toString(lastKnownLocation.latitude),Double.toString(lastKnownLocation.longitude));
                }
            }
        });


        String gosProvider = LocationManager.GPS_PROVIDER;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        mLocationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationmanager.requestLocationUpdates(gosProvider, 1000,20, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        sprinter_map = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        sprinter_map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        sprinter_map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        sprinter_map_circles = new ArrayList<>();

        Toast.makeText(Bootstrap.this, "Called Init Map Ready", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location){
        if(null == sprinter_map || null == location){
            return;
        }

        sprinter_map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
        dh.addPoint(sprint_id,Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
        if(null != current_location){
            current_location.remove();
        }

        if(null != current_location_radius){
            current_location_radius.remove();
        }

        current_location_radius = sprinter_map.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(50)
                .strokeColor(Color.RED)
                .strokeWidth(3)
                .fillColor(Color.TRANSPARENT));

        current_location = sprinter_map.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(5)
                .strokeColor(Color.BLUE)
                .fillColor(Color.BLUE));

        if(started == true) {
            if (null == userPaths) {
                userPaths = new PolylineOptions().width(5).color(Color.BLUE);
                sprinter_map.addMarker(new MarkerOptions().title("START POINT").position(new LatLng(location.getLatitude(), location.getLongitude())));
            }

            userPaths.add(new LatLng(location.getLatitude(), location.getLongitude()));
            sprinter_map.addPolyline(userPaths);
        }

        lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
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
