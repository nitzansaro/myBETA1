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
import static com.example.mybeta1.FBref.refUsers;

import static com.example.mybeta1.FBref.refTeam;




public class addTeam extends AppCompatActivity {
    EditText nameText, numText;


    String Tname5,Tnum5,Ncoach;
    ArrayList Playerslist=new ArrayList();
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        nameText = findViewById(R.id.nameText);
        numText = findViewById(R.id.numText);

    }






    public void submit(View view) {
        Playerslist.clear();

        Tname5 = nameText.getText().toString();
        Tnum5 = numText.getText().toString();


        refTeam.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("" + Tnum5).getValue() == null)
                {
                    Intent i=getIntent();
                    Ncoach=i.getStringExtra("name");
                    Team t = new Team(Tnum5, Tname5,Ncoach,Playerslist);
                    refTeam.child("" + Tname5).setValue(t);
                    finish();
                    Toast.makeText(addTeam.this, "Team added successfully", Toast.LENGTH_LONG).show();
                    Intent si = new Intent(addTeam.this,Main3Activity.class);
                    si.putExtra("cOp", "coach");
                    startActivity(si);
                }
                else
                    Toast.makeText(addTeam.this, "Team already exists", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
