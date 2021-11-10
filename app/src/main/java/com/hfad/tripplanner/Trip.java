package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Trip extends AppCompatActivity {

    TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        receiver_msg = (TextView)findViewById(R.id.received_value_id);

                Intent intent = getIntent();

                String str = intent.getStringExtra("message_key");

        receiver_msg.setText("Ready to plan for your " + str + " trip?");
    }
}
