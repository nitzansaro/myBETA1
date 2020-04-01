package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.mybeta1.FBref.refTeams;

public class MyTeam extends AppCompatActivity {
    ListView list1;

    ArrayList<String> pList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        list1=findViewById(R.id.list1);
        final String tName=getIntent().getStringExtra("teamNAME");
        //list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //list1.setOnItemClickListener(this);
        Toast.makeText(MyTeam.this, tName, Toast.LENGTH_LONG).show();

        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Team t = data.getValue(Team.class);
                    if (t.getTeamname().equals(tName)){
                        pList=t.getPlayerslist();
                        pList.add("test player");
                        pList.add("nitzan is kutchi");

                    }

                }
                ArrayAdapter a = new ArrayAdapter<String>(MyTeam.this,R.layout.support_simple_spinner_dropdown_item, pList);
                list1.setAdapter(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
