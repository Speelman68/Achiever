package com.example.achiever.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.achiever.R;
import com.example.achiever.User;

import java.util.ArrayList;

public class DisplayHabit extends AppCompatActivity {
    User user;
    ArrayList list;
    ListView listView;
    AdaptHabits adaptHabits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_habit);

        user = ((User) this.getApplication());
        setList();

        Button addNewButton= (Button) findViewById(R.id.addNewHabit);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set slot to -1 so the design will make a new habit
                user.slot = -1;
                Intent habitIntent = new Intent(DisplayHabit.this, DesignHabit.class);
                startActivity(habitIntent);
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
        list = user.habits;
        listView = (ListView) findViewById(R.id.listview);
        adaptHabits = new AdaptHabits(DisplayHabit.this, list, user);
        adaptHabits.adaptHabits = adaptHabits;
        listView.setAdapter(adaptHabits);
    }
}