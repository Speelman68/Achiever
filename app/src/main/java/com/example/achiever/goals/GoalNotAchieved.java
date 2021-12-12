package com.example.achiever.goals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achiever.MainActivity;
import com.example.achiever.R;
import com.example.achiever.User;

public class GoalNotAchieved extends AppCompatActivity {

    User user;
    LongTerm longTerm;
    Intent intent = new Intent();
    int goalSlot;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        // This activity takes the goal position from the position list
        // provided by the GoalCompletionCheck activity and locates the
        // long term goal to be removed/edited upon creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_not_achieved);

        user = ((User) this.getApplication());
        intent = getIntent();
        goalSlot = intent.getIntExtra("goalSlot", 0);
        longTerm = user.longTerms.get(goalSlot);

        TextView goalView = (TextView) findViewById(R.id.goalGoesHere1);
        goalView.setText(longTerm.description);

        TextView endDateView = (TextView) findViewById(R.id.endDateGoesHere1);
        endDateView.setText(longTerm.endDate);

        Button move = (Button) this.findViewById(R.id.move);

        move.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Opens the long term goal editor activity.
                Intent longTermIntent = new Intent(GoalNotAchieved.this, DesignLongTerm.class);
                longTermIntent.putExtra("Position", goalSlot);
                v.getContext().startActivity(longTermIntent);
                finish();
            }
        });

        Button delete = (Button) this.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Removes the goal and displays the goal list.
                user.longTerms.remove(goalSlot);

                Context context = getApplicationContext();
                CharSequence text = "Goal removed";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent goDisplayHabit = new Intent(GoalNotAchieved.this, DisplayLongTerm.class);
                goDisplayHabit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goDisplayHabit);
                finish();
            }
        });
    }
}