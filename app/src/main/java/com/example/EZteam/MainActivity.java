package com.example.EZteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=4000;
    private FirebaseAuth mAuth;
   Timer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent t = new Intent(MainActivity.this, auth.class);
                startActivity(t);
                finish();
            }
        } ,SPLASH_TIME_OUT);


};}



