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
    EditText name,number;
    String name1="g", number1="",Nplayer="";
    Boolean teamEX=true;

    ArrayList<String> playerslist1  = new ArrayList <> ();
    ArrayList <String> nitzan = new ArrayList<>();

    Team teamt;
    int numberofchildinlist=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeraddt);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        Intent t=getIntent();
        Nplayer= t.getExtras().getString("name");


        }

    public void submit(View view) {
        name1 = name.getText().toString();
        teamEX=false;
        number1 = number.getText().toString();
        Toast.makeText(playeraddt.this, Nplayer, Toast.LENGTH_LONG).show();
        Query query = refTeams.orderByChild("teamname").equalTo(name1);
        query.addListenerForSingleValueEvent(VEL);

        }

    ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    teamt = data.getValue(Team.class);
                    //Toast.makeText(playeraddt.this, "" + teamt.getTeamname(), Toast.LENGTH_SHORT).show();
                    playerslist1 = teamt.getPlayerslist();
                   // int s=playerslist1.size();
                    playerslist1.add( Nplayer);
                    teamt.setPlayerslist(playerslist1);

                    refTeams.child(name1).setValue(teamt);
                    Intent a2 = new Intent(playeraddt.this, Main3Activity.class);
                    startActivity(a2);
                    finish();
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


