package com.example.achiever;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.achiever.calendar.WeekViewActivity;
import com.example.achiever.notifications.NotificationReceiver;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAlarm();
    }






    //Leave these for now, I'm using them for testing
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void weeklyAction(View view)
    {

        startActivity(new Intent(this, WeekViewActivity.class));
    }

    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 47);
        calendar.set(Calendar.SECOND, 0);

        Date currentTime = Calendar.getInstance().getTime();

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10000, pendingIntent);
            //AlarmManager.INTERVAL_DAY
        }

    }
}