package com.example.achiever.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.achiever.MainActivity;
import com.example.achiever.R;
import com.example.achiever.User;

public class GoalAchieved extends AppCompatActivity {

    User user;
    LongTerm longTerm;
    Intent intent = new Intent();
    int goalSlot;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_achieved);

        user = ((User) this.getApplication());
        intent = getIntent();
        goalSlot = intent.getIntExtra("goalSlot", 0);

        Button backToMain = (Button) this.findViewById(R.id.backToMain);

        backToMain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                user.longTerms.remove(goalSlot);

                Intent goBackToMain = new Intent(GoalAchieved.this, MainActivity.class);
                goBackToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goBackToMain);
                finish();
            }
        });
    }
}