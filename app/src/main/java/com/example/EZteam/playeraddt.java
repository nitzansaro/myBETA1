package com.example.EZteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.EZteam.FBref.refTeams;


public class playeraddt extends AppCompatActivity {
    EditText name,number;
    String name1="g", number1="",Nplayer="";
    Boolean teamEX=true;

    ArrayList<String> playerslist1  = new ArrayList <> ();


    Team teamt;

    /**
     *on getting name from main, only gets here if has no team.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeraddt);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        Intent t=getIntent();
        Nplayer= t.getExtras().getString("name");


        }

    /**
     * on click for check team
     * @param view
     */

    public void submit(View view) {
        name1 = name.getText().toString();
        teamEX=false;
        number1 = number.getText().toString();
        Toast.makeText(playeraddt.this, Nplayer, Toast.LENGTH_LONG).show();
        Query query = refTeams.orderByChild("teamname").equalTo(name1);
        query.addListenerForSingleValueEvent(VEL);

        }

    /**
     * if exists team can be added
     * add player name into players list.
     * update team
     */
    ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    teamt = data.getValue(Team.class);
                    if (teamt.getTeamnum().equals(number1)){
                    playerslist1 = teamt.getPlayerslist();
                    playerslist1.add( Nplayer);
                    teamt.setPlayerslist(playerslist1);
                    refTeams.child(name1).setValue(teamt);
                    Intent a2 = new Intent(playeraddt.this, playermain.class);
                    startActivity(a2);}
                    else{
                        Toast.makeText(playeraddt.this, "Team number is wrong", Toast.LENGTH_LONG).show();

                    }

                }
            }
            else {
                Toast.makeText(playeraddt.this, "Team Doesn't exists, ask your coach", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

}


