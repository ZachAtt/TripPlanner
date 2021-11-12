package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TripViewer extends AppCompatActivity {
    public static final String TRIP_ID = "tripId";
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_viewer);

        button = (Button) findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainActivity();
            }
        });

        button = (Button) findViewById(R.id.btn_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationViewer();
            }
        });


    }
        public void backToMainActivity(){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    public void openDestinationViewer(){
        Intent intent = new Intent(this, DestinationViewer.class);
        startActivity(intent);
    }


    }
