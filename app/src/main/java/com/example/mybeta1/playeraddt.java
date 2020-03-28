package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.mybeta1.FBref.refAuth;
import static com.example.mybeta1.FBref.refTeam;
import static com.example.mybeta1.FBref.refUsers;


public class playeraddt extends AppCompatActivity {
    EditText nameText, numText;
    String name1, num1, uid,Nplayer;

    String coach2;

    ArrayList Playerslist  = new ArrayList <User> ();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeraddt);
        nameText = findViewById(R.id.nameText);
        numText = findViewById(R.id.numText);


    }


    public void submit(View view) {
        name1 = nameText.getText().toString();
        num1 = numText.getText().toString();

        Query query = refTeam.orderByChild("name1").equalTo(name1).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {


                        if (dataSnapshot.child("" + name1).getValue() != null) {
                            Intent i = getIntent();
                            Nplayer = i.getStringExtra("name");
                            Playerslist.add(Nplayer);
                            Team t = data.getValue(Team.class);
                            t.setPlayerslist(Playerslist);

                            Toast.makeText(playeraddt.this, "Team  added!", Toast.LENGTH_LONG).show();
                            Intent si = new Intent(playeraddt.this, Loginok.class);
                            si.putExtra("cOp", "player");
                            startActivity(si);
                        }

                 else {
                    Toast.makeText(playeraddt.this, "Team Doesn't exists, ask your coach", Toast.LENGTH_LONG).show();
                }
            }}}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }}

















