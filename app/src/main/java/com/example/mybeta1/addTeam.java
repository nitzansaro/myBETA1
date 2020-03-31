package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import static com.example.mybeta1.FBref.refAuth;
import static com.example.mybeta1.FBref.refTeams;
import static com.example.mybeta1.FBref.refUsers;


public class addTeam extends AppCompatActivity {

    EditText nameText, numText;
    Boolean teamEX;
    String tname,tnum,Ncoach;
    ArrayList <String> playlist=new ArrayList();
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        nameText = findViewById(R.id.nameText);
        numText = findViewById(R.id.numText);
        teamEX=false;
        Intent i=getIntent();
        Ncoach=i.getStringExtra("name");
    }

    com.google.firebase.database.ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            teamEX=false;
            if (dataSnapshot.exists()){
                teamEX=true; }
/*                for(DataSnapshot data : dataSnapshot.getChildren()){
                       teamEX=true; }
            }*/
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    public void submit(View view) {
        playlist.clear();
        tname = nameText.getText().toString();
        tnum = numText.getText().toString();
        Query query = refTeams
                .orderByChild("teamname")
                .equalTo(tname)
                .limitToFirst(1);
        query.addListenerForSingleValueEvent(VEL);

        if (!teamEX) {
            playlist.add(Ncoach);
            Team t = new Team(tnum, tname, Ncoach, playlist);
            refTeams.child(tname).setValue(t);
            Toast.makeText(addTeam.this, "Team added successfully", Toast.LENGTH_LONG).show();
            Intent si = new Intent(addTeam.this, Main3Activity.class);
            si.putExtra("cOp", "coach");
            startActivity(si); }
        else
            Toast.makeText(addTeam.this, "Team already exist", Toast.LENGTH_LONG).show(); }



}
