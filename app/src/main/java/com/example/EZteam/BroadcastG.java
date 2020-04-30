package com.example.EZteam;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

class BroadcastG extends BroadcastReceiver {
    String thisday,gameday,st,time,team1="",team2="";
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        thisday = settings.getString("thisday", " ");
        gameday = settings.getString("gameday", " ");
        time = settings.getString("time", " ");
        team1 = settings.getString("team1", " ");
        team2 = settings.getString("team2", " ");



        if (thisday.equals(gameday)){
            st=team1+" VS "+ team2 + " good luck!";
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context.getApplicationContext(), "my_notify");
            Intent tmpInt = new Intent(context.getApplicationContext(), BroadcastG.class);
            PendingIntent pI = PendingIntent.getActivity(context, 0, tmpInt, 0);
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

            bigText.setBigContentTitle("GAME alert");
            bigText.setSummaryText("GAME IN AN HOUR");
            myNoti.setContentIntent(pI);
            myNoti.setSmallIcon(R.drawable.mirf);
            myNoti.setContentTitle(team1);
            myNoti.setContentText(st);
            myNoti.setPriority(Notification.PRIORITY_MAX);
            myNoti.setStyle(bigText);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "my_notify";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                nm.createNotificationChannel(channel);
                myNoti.setChannelId(channelId);
            }
            nm.notify(0, myNoti.build());


        }


    }
}
