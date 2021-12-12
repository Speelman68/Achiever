package com.example.achiever.journal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.achiever.DateHandler;
import com.example.achiever.R;

import java.util.HashSet;

public class EntryEditorActivity extends AppCompatActivity {

    int entryId;
    DateHandler dateHandler = new DateHandler();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        // If this is a new entry, the intent extra from JournalActivity will be null
        // and the EditText will be blank (except for the date), otherwise it will be
        // filled with the to-be-edited entry.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_editor);

        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        entryId = intent.getIntExtra("entryId", -1);

        if (entryId != -1) {
            editText.setText(JournalActivity.entries.get(entryId));
        } else {
            String currentDate = dateHandler.getTodaysDate();
            JournalActivity.entries.add("");
            editText.setText(currentDate + ":");
            entryId = JournalActivity.entries.size() - 1;
            JournalActivity.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                // Does nothing
            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                // Automatically saves the entry using the arrayAdapter.
                JournalActivity.entries.set(entryId, String.valueOf(s));
                JournalActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.achiever.journal", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(JournalActivity.entries);
                sharedPreferences.edit().putStringSet("entries", set).apply();
            }

            @Override
            public void afterTextChanged (Editable s) {
                // Does nothing
            }
        });
    }
}