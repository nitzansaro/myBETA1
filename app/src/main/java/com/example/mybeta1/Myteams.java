package com.example.mybeta1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Myteams extends AppCompatActivity {
    Timer timer;
    String c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myteams);
        Intent i=getIntent();
        c=i.getStringExtra("c");
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(c.equals("coach")){
                Intent t = new Intent (Myteams.this,Coachmain.class);
                startActivity(t);}
                else {Intent t = new Intent (Myteams.this,Main3Activity.class);
                    startActivity(t);}
                finish(); }
        },   5000);
    }
    }


