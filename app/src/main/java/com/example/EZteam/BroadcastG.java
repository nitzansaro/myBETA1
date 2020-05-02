package com.example.EZteam;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

class BroadcastG extends BroadcastReceiver {
    String thisday,st,time,team1="",team2="";
           String gamesoon;

    int msg;


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", MODE_PRIVATE);

        gamesoon = settings.getString("gamesoon", "-1 ");





        if (!gamesoon.equals("-1")){
            st="you have "+ gamesoon +"  games soon good luck!";
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context.getApplicationContext(), "my_notify");
            Intent tmpInt = new Intent(context.getApplicationContext(), BroadcastG.class);
            PendingIntent pI = PendingIntent.getActivity(context, 0, tmpInt, 0);
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

            bigText.setBigContentTitle("GAME alert");

            myNoti.setContentIntent(pI);
            myNoti.setSmallIcon(R.drawable.mirf);

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



    }}

/*
    @Override
    public void onReceive(Context context, Intent ri) {
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
      msg = settings.getInt("newmsg", 0);



        if (msg !=0) {

            st = "you have  " + msg + "massages";

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context.getApplicationContext(), "my_notify");
            Intent tmpInt = new Intent(context.getApplicationContext(), BroadcastG.class);
            PendingIntent pI = PendingIntent.getActivity(context, 0, tmpInt, 0);
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

            bigText.setBigContentTitle("updates");
            bigText.setSummaryText("msg");
            myNoti.setContentIntent(pI);
            myNoti.setSmallIcon(R.drawable.mirf);
            myNoti.setContentTitle("you have updates");
            myNoti.setContentText(st);
            myNoti.setPriority(Notification.PRIORITY_MAX);
            myNoti.setStyle(bigText);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
*/