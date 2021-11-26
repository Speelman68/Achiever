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
import com.example.achiever.goals.DisplayLongTerm;
import com.example.achiever.notifications.NotificationReceiver;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private void userCheck() throws IOException {
        if (user == null){
            if (loadUser()) {
                System.out.println("Loaded");
                return;
            }
            else{
                System.out.println("New user");
                user = new User();
                saveUser();
            }
        }
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

    private void saveUser() throws IOException {
        File path = context.getFilesDir();
        File file = new File(path, "user.txt");
        String userString = gson.toJson(user);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(userString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
            System.out.println("Saved");
        }
    }

    private boolean loadUser() {
        if (context == null) {
            return false;
        }

        File path = context.getFilesDir();
        File file = new File(path, "user.txt");

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String contents = new String(bytes);
        System.out.println("User" + contents);
        user = gson.fromJson(contents, User.class);
        return true;
    }
}