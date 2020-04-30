package com.example.EZteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class credits extends AppCompatActivity {
    Timer timer;
    String c;

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
                if(c.equals("coach")){
                Intent t = new Intent (credits.this,Coachmain.class);
                startActivity(t);}
                else {Intent t = new Intent (credits.this, playermain.class);
                    startActivity(t);}
                finish(); }
        },   5000);
    }
    }


