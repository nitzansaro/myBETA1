package com.example.EZteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
   Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

}
    @Override
    public void onStart() {
        super.onStart();

       timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent t = new Intent (MainActivity.this, auth.class);
                  startActivity(t);
                  finish(); }
        },   2000);}

}



