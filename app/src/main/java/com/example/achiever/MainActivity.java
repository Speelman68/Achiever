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


import com.example.achiever.calendar.WeekViewActivity;
import com.example.achiever.goals.DisplayHabit;
import com.example.achiever.notifications.NotificationReceiver;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity{
    User user;
    Context context;
    Intent habitIntent;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    //Leave these for now, I'm using them for testing
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void weeklyAction(View view)
    {
        habitIntent = new Intent(this, WeekViewActivity.class);
        startActivity(habitIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
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