package com.hfad.tripplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easywaylocation.draw_path.DirectionUtil;
import com.example.easywaylocation.draw_path.PolyLineDataBean;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class Direction extends AppCompatActivity implements OnMapReadyCallback, DirectionUtil.DirectionCallBack {

    Button button;
    private GoogleMap mMap;

    private double currentLat, currentLng;
    private double destLat, destLng;
    private String destName;

    private ArrayList<LatLng> wayPoints = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        if (getIntent() != null) {
            currentLat = getIntent().getDoubleExtra("current_lat", 0.0);
            currentLng = getIntent().getDoubleExtra("current_lng", 0.0);
            destLat = getIntent().getDoubleExtra("dest_lat", 0.0);
            destLng = getIntent().getDoubleExtra("dest_lng", 0.0);
            destName = getIntent().getStringExtra("dest_name");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = (Button) findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToDestinationViewer();
            }
        });

    }

    public void backToDestinationViewer() {
        onBackPressed();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentLatLng = new LatLng(39.000000,-80.500000);
        LatLng destLatLng = new LatLng(destLat, destLng);
        mMap.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("Your Location"));
        mMap.addMarker(new MarkerOptions().position(destLatLng).title(destName));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentLatLng);
        builder.include(destLatLng);
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
        mMap.animateCamera(cu);
        wayPoints.add(currentLatLng);
        wayPoints.add(destLatLng);
        DirectionUtil directionUtil = new DirectionUtil.Builder()
                .setOrigin(currentLatLng)
                .setDestination(destLatLng)
                .setWayPoints(wayPoints)
                .setGoogleMap(mMap)
                .setPathAnimation(false)
                .setPolyLineWidth(8)
                .setDirectionKey("AIzaSyDFvpKvSR2lqy6dTg5gvg8vqMSLdxrbt2s")
                .setCallback(this).build();
        try {
            directionUtil.drawPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pathFindFinish(HashMap<String, PolyLineDataBean> hashMap) {

    }
}