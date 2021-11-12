package com.example.achiever;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.achiever.calendar.WeekViewActivity;
import com.example.achiever.notifications.NotificationUtils;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }






    //Leave these for now, I'm using them for testing
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void weeklyAction(View view)
    {

        startActivity(new Intent(this, WeekViewActivity.class));

        NotificationUtils _notificationUtils = new NotificationUtils(this);
        long _currentTime = System.currentTimeMillis();
        long tenSeconds = 1000 * 20;
        long _triggerReminder = _currentTime + tenSeconds; //triggers a reminder after 10 seconds.
        _notificationUtils.setReminder(_triggerReminder);
    }
}
