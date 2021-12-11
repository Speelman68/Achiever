package com.example.achiever;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.achiever.Firebase.FireBaseCloud;
import com.example.achiever.Firebase.FireBaseLoginActivity;
import com.example.achiever.calendar.WeekViewActivity;
import com.example.achiever.goals.DisplayHabit;
import com.example.achiever.goals.DisplayLongTerm;
import com.example.achiever.notifications.GoalCompletionNotification;
import com.example.achiever.notifications.NotificationReceiver;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity{
    User user;
    Intent habitIntent;
    DateHandler dateHandler = new DateHandler();
    String email = "";
    FireBaseCloud mCloud;

    //private SharedPreferences mPrefs = getSharedPreferences("cloud", 0);
    //private SharedPreferences.Editor mEditor = mPrefs.edit();

    private static String email;

    @RequiresApi(api = Build.VERSION_CODES.O) // Antonio: I used this for long term goal check.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = ((User) this.getApplication()); // Antonio: I used this for long term goal check.
        checkForGoals(); // Antonio: I used this for long term goal check.
    }

    public void settingButton(View view)
    {
        startActivity(new Intent(this, FireBaseLoginActivity.class));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startHabit(View view)
    {
        Intent habitIntent = new Intent(this, DisplayHabit.class);
        startActivity(habitIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startLongTerm(View view)
    {
        Intent longTermIntent = new Intent(this, DisplayLongTerm.class);
        startActivity(longTermIntent);
    }
    //-------------------------------------------------------------------//
    //---------Antonio: the next 2 methods are for goal checking---------//
    //-------------------------------------------------------------------//

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkForGoals() {

        String today = dateHandler.getTodaysDate();
        int i = 0;
        if (user.longTerms != null && !user.longTerms.isEmpty())
            for (i=0; i<user.longTerms.size(); i++) {
                if (user.longTerms.get(i).getEndDate().equals(today)){
                    GoalCompletionNotification goalCompletionNotification = new GoalCompletionNotification(this, i);
                    goalCompletionNotification.createNotification();
                }
            }
    }
    //-------------------------------------------------------------------//
    //-------------------------------------------------------------------//

    //Leave these for now, I'm using them for testing
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void weeklyAction(View view)
    {
        habitIntent = new Intent(this, WeekViewActivity.class);
        startActivity(habitIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O) // Antonio: I used this for long term goal check.

    @Override
    protected void onResume() {
        super.onResume();
        checkForGoals(); // Antonio: I used this for long term goal check.
    }

    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY, 20000, pendingIntent);
        }
    }

}