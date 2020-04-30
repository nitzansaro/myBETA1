package com.example.EZteam;

import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastG {
    String uid;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context,gameInfo.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
