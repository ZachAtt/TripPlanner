package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

public class NoteEditor extends AppCompatActivity {

    private Button buttonAdd, buttonCancel;
    private EditText editTextNote;

    private int selectedNoteId = 0;
    private int selectedDestinationId = 0;
    private String type = "";
    private String note;

    private TripDatabaseHelper tripDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        tripDatabaseHelper = new TripDatabaseHelper(this);

        buttonAdd = findViewById(R.id.btn_add);
        buttonCancel = findViewById(R.id.btn_cancel);
        editTextNote = findViewById(R.id.editTextNote);

        selectedNoteId = getIntent().getIntExtra("note_id", 0);
        selectedDestinationId = getIntent().getIntExtra("destinationId", 0);
        type = getIntent().getStringExtra("type");
        note = getIntent().getStringExtra("note");

        if (type.equals("edit")) {
            editTextNote.setText(note);
        }

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextNote.getText().toString();
                if (!text.isEmpty()) {
                    if (type.equals("add")) {
                        long id = tripDatabaseHelper.insertNote(selectedDestinationId, text);
                        if(id != -1) {
                            Toast.makeText(NoteEditor.this, "Note added successfully.", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        tripDatabaseHelper.updateNote(selectedNoteId, text);
                        Toast.makeText(NoteEditor.this, "Note updated successfully.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });

    }

}