package com.example.EZteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class credits extends AppCompatActivity {
    Timer timer;
    String c;

    /**

     * timer
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        Intent i=getIntent();
        c=i.getStringExtra("c");
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                finish();
            }
        },   3000);
    }
    }


