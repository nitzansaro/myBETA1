package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.mybeta1.FBref.refAuth;
import static com.example.mybeta1.FBref.refUsers;

public class Loginok extends AppCompatActivity {

    String name,   id,cOp1;
    TextView tVnameview, tVemailview, tVuidview, tVidview,tVcoachview,tVphoneview,t;
    CheckBox cBconnectview;
    Boolean coach2;
    FirebaseUser user = refAuth.getCurrentUser();
    String uid = user.getUid();
    String email=user.getEmail();
    String a;
    String b;
    //boolean c;
    String d;
    String n,p,i,c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginok);
        tVnameview = (TextView) findViewById(R.id.tVnameview);
        tVemailview = (TextView) findViewById(R.id.tVemailview);
        tVuidview = (TextView) findViewById(R.id.tVuidview);
        tVidview = (TextView) findViewById(R.id.tVidview);
        tVphoneview = (TextView) findViewById(R.id.tVphoneview);
        tVcoachview = (TextView) findViewById(R.id.tVcoachview);
        cBconnectview = (CheckBox) findViewById(R.id.cBconnectview);
        t=findViewById(R.id.titl);
        //tVnameview.setText("idan");
        Intent ins=getIntent();
        n=ins.getStringExtra("n");
        p=ins.getStringExtra("p");
        i=ins.getStringExtra("i");
        c=ins.getStringExtra("cOp");


        /*refUsers.child("Player").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (email.equals(u.getEmail())){
                       a=u.getName();
                        b=u.getid();
                        //Toast.makeText(Loginok.this, u.getName(), Toast.LENGTH_LONG).show();
                        c=u.getPhone();
                        if (u.getCoach())
                            t.setText("Coach information");
                        if (!u.getCoach()) {
                            t.setText("Player information");
                    }}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
       // tVphoneview.setText("numbers");
        tVemailview.setText(email);
        tVuidview.setText(uid);
        tVnameview.setText(n);
        tVidview.setText(i);
        tVphoneview.setText(p);
        t.setText(c + "information");
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);

    }


    /*@Override
    public void onStart() {
        super.onStart();
         //אחרי שיעבוד זה יצטרך לקבל מידע מהמניו, איך יגשים למה שמתחת למאמן/שחקן
        Intent i=getIntent();
        //cOp1= i.getStringExtra("cOp");
        FirebaseUser user = refAuth.getCurrentUser();
        uid = user.getUid();
        Query query = refUsers
                .orderByChild("uid")
                .equalTo(uid)
                .limitToFirst(1);
        query.addListenerForSingleValueEvent(VEL);
        email = user.getEmail();
        tVemailview.setText(email);
        uid = user.getUid();
        tVuidview.setText(uid);
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);}
    com.google.firebase.database.ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    tVnameview.setText(user.getName());
                    tVidview.setText(user.getid());
                    tVphoneview.setText(user.getPhone());
                    if (user.getCoach())
                        t.setText("Coach information");
                    if (!user.getCoach()) {
                        t.setText("Player information");
                    }

                }

            }}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };*/

    public void update(View view) {
        FirebaseUser user = refAuth.getCurrentUser();
        if (!cBconnectview.isChecked()){
            refAuth.signOut();
        }
        SharedPreferences settings=getSharedPreferences("PREFS_NAME1",MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("stayConnect",cBconnectview.isChecked());
        editor.apply(); //changed from commit
        finish();
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuCredits) {
            Intent si = new Intent(Loginok.this,MainActivity.class);
            startActivity(si);
        }
        if (id==R.id.menuProfile) {
            Intent si = new Intent(Loginok.this,Loginok.class);
            startActivity(si);
        }

        if (id==R.id.menuMain) {
            Intent si = new Intent(Loginok.this,Main3Activity.class);
            startActivity(si);
        }

        return true;
    }
}
