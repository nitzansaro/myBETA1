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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

class BroadcastG extends BroadcastReceiver {
    String thisday,st,gamesoon,firstgame;


    int msg;


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", MODE_PRIVATE);

        gamesoon = settings.getString("gamesoon", "-1 ");
        firstgame = settings.getString("firstgame"," ");
        /**
         * day of today
         */
        thisday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());





        if (!gamesoon.equals("-1")){
            if (thisday.equals(firstgame)){
                st="you have a game today!!";

           // st="you have "+ gamesoon +"  games soon good luck!";

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


        }}}}





