package com.example.achiever.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReminderBroadcast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationUtils _notificationUtils = new NotificationUtils(context);
        NotificationCompat.Builder _builder = _notificationUtils.setNotification("Testing", "Testing notification system");
        _notificationUtils.getManager().notify(101, _builder.build());
    }
}