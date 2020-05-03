package com.example.EZteam;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.EZteam.FBref.refAuth;

public class profile extends AppCompatActivity {
    TextView tVnameview, tVemailview, tVidview,tVphoneview,t;
    CheckBox cBconnectview;
    FirebaseUser user = refAuth.getCurrentUser();
    String uid = user.getUid();
    String email=user.getEmail();
    String n,p1,i,c,d;
    Timer timer;

    /**
     *getting info from activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tVnameview = (TextView) findViewById(R.id.tVnameview);
        tVemailview = (TextView) findViewById(R.id.tVemailview);
        tVidview = (TextView) findViewById(R.id.tVidview);
        tVphoneview = (TextView) findViewById(R.id.tVphoneview);
          cBconnectview = (CheckBox) findViewById(R.id.cBconnectview);
        t=findViewById(R.id.titl);

        Intent ins=getIntent();
        n=ins.getStringExtra("n");
        p1=ins.getStringExtra("p");
        i=ins.getStringExtra("i");
        c=ins.getStringExtra("c");
        d=ins.getStringExtra("d");
        tVemailview.setText(email);
        tVnameview.setText(n);
        tVidview.setText(i);
        tVphoneview.setText(p1);
        t.setText(c + "  " +"Profile");
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);


        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                finish();
            }
        },   10000);

    }





}
