package com.example.cafeteria.Ui.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Activity.MainActivity;
import com.example.cafeteria.Ui.Fragment.NotificationFragment;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BroadCast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        if(intent.getAction().equals("com.example.Broadcast"))
        {
              PendingIntent pendingIntent = null;
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
              {

                  NotificationChannel notificationChannel = new NotificationChannel("my notification", "الإشعارات", NotificationManager.IMPORTANCE_DEFAULT);
                  Intent intent1 = new Intent(context, MainActivity.class);
                  intent1.putExtra("notification_status",true);
                  pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
                  NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                  notificationManager.createNotificationChannel(notificationChannel);
              }
              NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my notification")
                      .setContentTitle(bundle.getString("name"))
                      .setContentText(bundle.getString("notification"))
                      .setSmallIcon(R.drawable.logo_notification)
                      .setContentIntent(pendingIntent)
                      .setAutoCancel(true);

              NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
              notificationManager.notify((int) System.currentTimeMillis(), builder.build());
      }
    }
}
