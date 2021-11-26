package com.example.achiever.goals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.achiever.R;
import com.example.achiever.User;

import java.util.ArrayList;


public class DisplayLongTerm extends AppCompatActivity {

    User user;
    ArrayList list;
    ListView listView;
    AdaptLongTerms adaptLongTerms;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_long_term);
        user = ((User) this.getApplication());
        setList();

        user.longTerms.add(new LongTerm("Get this working", "Today")); //demo

        Button addNewButton = (Button) findViewById(R.id.addSummit);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.slot = -1;
                Intent summitIntent = new Intent(DisplayLongTerm.this, DesignLongTerm.class);
                startActivity(summitIntent);
            }
        });
    }

    @Override
    public void onResume() {
        user = ((User) this.getApplication());
        super.onResume();
        setList();
    }

    private void setList() {
        list = user.longTerms;
        listView = (ListView) findViewById(R.id.listview);
        adaptLongTerms = new AdaptLongTerms(DisplayLongTerm.this, list, user);
        adaptLongTerms.adaptLongTerms = adaptLongTerms;
        listView.setAdapter(adaptLongTerms);
    }
}
