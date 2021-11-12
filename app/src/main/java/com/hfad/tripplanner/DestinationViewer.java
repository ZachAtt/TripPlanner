package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DestinationViewer extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_viewer);

        button = (Button) findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToTripViewer();
            }
        });

        button = (Button) findViewById(R.id.btn_direction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDirectionViewer();
            }
        });
    }

    public void backToTripViewer() {
        Intent intent = new Intent(this, TripViewer.class);
        startActivity(intent);
    }

    public void openDirectionViewer() {
        Intent intent = new Intent(this, Direction.class);
        startActivity(intent);
    }
}
