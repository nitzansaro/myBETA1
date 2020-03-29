package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.mybeta1.FBref.refAuth;
import static com.example.mybeta1.FBref.refUsers;

public class Main3Activity extends AppCompatActivity {
    String uid;
    Button addteam;
     Boolean coach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        addteam=findViewById(R.id.addteam);

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = refAuth.getCurrentUser();
        uid = user.getUid();
        Query query = refUsers
                .orderByChild("uid")
                .equalTo(uid)
                .limitToFirst(1);


        query.addListenerForSingleValueEvent(VEL);
        uid = user.getUid();}
        com.google.firebase.database.ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    coach=user.getCoach();
                    if (coach){
                       addteam.setVisibility(View.VISIBLE);
                        Intent si = new Intent(Main3Activity.this,addTeam.class);
                        startActivity(si);
                }}

            }}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };




    public void toaddTeams(View view) {

        Intent si = new Intent(Main3Activity.this,addTeam.class);
        startActivity(si);

    }



    public void toMesseges(View view) {
    }

    public void toTeams(View view) {
    }


    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuCredits) {
            Intent si = new Intent(Main3Activity.this,MainActivity.class);
            startActivity(si);
        }
        if (id==R.id.menuProfile) {
            Intent si = new Intent(Main3Activity.this,Loginok.class);
            startActivity(si);
        }

        if (id==R.id.menuMain) {
            Intent si = new Intent(Main3Activity.this,Main3Activity.class);
            startActivity(si);
        }

        return true;
    }


    public void toaddGAME(View view) {
        Intent si = new Intent(Main3Activity.this,gameInfo.class);
        startActivity(si);
    }
}
