package com.example.achiever.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achiever.R;
import com.example.achiever.User;
import com.example.achiever.goals.Habit;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimerScreen extends AppCompatActivity {
    //   variables.
    int seconds = 0;
    User user;

    private boolean running;
    private boolean wasRunning;

    int totalTime = 0;
    private boolean goalCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_screen);
        user = ((User) this.getApplication());
        if (savedInstanceState != null){
            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds = savedInstanceState.getInt("seconds");

            running = savedInstanceState.getBoolean("running");

            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
    }

    // Save the state of the stopwatch
    // if it's about to be destroyed.

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("seconds", seconds);

        savedInstanceState.putBoolean("running", running);

        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }


    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause(){

        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.

    @Override
    protected void onResume(){
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    // Start the stopwatch running
    // when the Start button is clicked.
    // Below method gets called
    // when the Start button is clicked.
    public void onClickStart(View view){
        running = true;
    }
    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.

    public void onClickStop(View view){
        running = false;
    }
    // Reset the stopwatch when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.

    public void onClickReset(View view){
        totalTime += seconds;
        System.out.println(totalTime);
        running = false;
        seconds = 0;
    }

    // Sets the Number of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer(){
        // Get the text view.
        final TextView timeView = (TextView) findViewById(R.id.time_view);

        //creates a new handler
        final Handler handler = new Handler();
        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run(){
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                long millisec = 1000;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                // Set the text view text.
                timeView.setText(time);


                // If running is true, increment the
                // seconds variable.
                if (running == true){
                    seconds++;
                }


                if(seconds > 30){
                    user.habits.get(user.slot).updateStreak();
                    Habit habit = user.habits.get(user.slot);

                    goalCompleted = true;
                    Toast.makeText(TimerScreen.this, habit.getDescription() + " has been completed", Toast.LENGTH_LONG).show();
                }
                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this,millisec);

            }
        });

    }

}