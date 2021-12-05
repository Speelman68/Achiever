package com.example.achiever.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.achiever.R;
import com.example.achiever.User;


public class GoalCompletionCheck extends AppCompatActivity {

    User user;
    LongTerm longTerm;
    Intent intent = new Intent();
    int goalSlot;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_completion_check);

        user = ((User) this.getApplication());
        intent = getIntent();
        goalSlot = intent.getIntExtra("goalSlot", 0);
        longTerm = user.longTerms.get(goalSlot);

        TextView goalView = (TextView) findViewById(R.id.goalGoesHere1);
        goalView.setText(longTerm.description);

        TextView endDateView = (TextView) findViewById(R.id.endDateGoesHere);
        endDateView.setText(longTerm.endDate);
    }
    public void startGoalNotAchieved(View view) {
        Intent notAchievedIntent = new Intent(GoalCompletionCheck.this, GoalNotAchieved.class);
        notAchievedIntent.putExtra("goalSlot", goalSlot);
        startActivity(notAchievedIntent);
        finish();
    }
    public void startGoalAchieved(View view) {
        Intent AchievedIntent = new Intent(GoalCompletionCheck.this, GoalAchieved.class);
        AchievedIntent.putExtra("goalSlot", goalSlot);
        startActivity(AchievedIntent);
        finish();
    }
}