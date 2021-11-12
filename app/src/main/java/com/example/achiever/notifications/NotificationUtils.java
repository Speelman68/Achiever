package com.example.achiever.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.achiever.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationUtils extends ContextWrapper
{

    private NotificationManager _notificationManager;
    private Context _context;
    private String CHANNEL_ID = "notification channel";
    private String TIMELINE_CHANNEL_NAME = "Timeline notification";

    public NotificationUtils(Context base)
    {
        super(base);
        _context = base;
        createChannel();
    }

    public NotificationCompat.Builder setNotification(String title, String body)
    {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)//Choose a better Icon.
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }


    private void createChannel()
    {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, TIMELINE_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(channel);

    }

    public NotificationManager getManager()
    {
        if(_notificationManager == null)
        {
            _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return _notificationManager;
    }

    public void setReminder(long timeInMillis)
    {
        Intent _intent = new Intent(_context, ReminderBroadcast.class);
        PendingIntent _pendingIntent = PendingIntent.getBroadcast(_context, 0, _intent, 0);

        AlarmManager _alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        _alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, _pendingIntent);
    }

}