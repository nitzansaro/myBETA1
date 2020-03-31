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
import static com.example.mybeta1.FBref.refTeams;
import static com.example.mybeta1.FBref.refUsers;


public class playeraddt extends AppCompatActivity {
    EditText nameText, numText;
    String name1, num1,Nplayer;


    Boolean teamEX;
    Team t;

    ArrayList<String> playerslist1  = new ArrayList <> ();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeraddt);
        nameText = findViewById(R.id.nameText);
        Intent i = getIntent();
        Nplayer=i.getStringExtra("name");
        numText = findViewById(R.id.numText);



        }


  /*  com.google.firebase.database.ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            teamEX=false;
            if (dataSnapshot.exists()){
              //  for(DataSnapshot data : dataSnapshot.getChildren()){
                    //if (dataSnapshot.child("" + name1).getValue() != null)
                teamEX=true;
                 t = dataSnapshot.getValue(Team.class);
                playerslist1=(t.getPlayerslist());

                }}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



       public void submit(View view) {
        name1 = nameText.getText().toString();
        num1 = numText.getText().toString();
        playerslist1.clear();
        Query query = refTeams
                .orderByChild("teamname")
                .equalTo(name1)
                .limitToFirst(1);
        query.addListenerForSingleValueEvent(VEL);

        if (teamEX ) {
            playerslist1.add(Nplayer);
            t.setPlayerslist(playerslist1);
            refTeams.child(name1).child("Plist").setValue(playerslist1);
            }
            else{
            Toast.makeText(playeraddt.this, "Team Doesn't exists, ask your coach", Toast.LENGTH_LONG).show();


        }



        }}

*/


    public void submit(View view) {
        name1 = nameText.getText().toString();
        num1 = numText.getText().toString();

        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange (@NonNull DataSnapshot dataSnapshot){
            playerslist1.clear();


            if (dataSnapshot.exists()) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.getValue(Team.class) != null) {
                        Team t = data.getValue(Team.class);
                        if (t.getPlayerslist() != null){
                            if (name1.equals(t.getTeamname())) {
                                playerslist1 = (t.getPlayerslist());
                                playerslist1.add(Nplayer);
                                t.setPlayerslist(playerslist1);

                                //Team t1=new Team(t.getTnum(),t.getTname(),t.getCoachname(),Playerslist);
                                refTeams.child(name1).child("Plist").setValue(playerslist1);
                            }}
                        Toast.makeText(playeraddt.this, "Team  added!", Toast.LENGTH_LONG).show();
                        Intent si = new Intent(playeraddt.this, Loginok.class);
                        si.putExtra("cOp", "player");
                        startActivity(si);
                    } else {
                        Toast.makeText(playeraddt.this, "Team Doesn't exists, ask your coach", Toast.LENGTH_LONG).show();

                }}
            }
        }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){
        }});}}



