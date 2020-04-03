package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.mybeta1.FBref.refGame;
import static com.example.mybeta1.FBref.refTeams;

public class listofteams extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView list1;
    Button b; Spinner sp;
    ArrayList<String> pList = new ArrayList<>(),tList = new ArrayList<>(),dummy = new ArrayList<>();
    String co;
    AlertDialog.Builder ad;
    LinearLayout dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofteams);
        b=findViewById(R.id.b) ;
        sp=findViewById(R.id.spin);
        list1=findViewById(R.id.list1);
       // Intent i=getIntent();
      //  co=i.getStringExtra("cname");

        final String cName=getIntent().getStringExtra("cname");
        co=cName;
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list1.setOnItemClickListener(this);
        Toast.makeText(listofteams.this, cName, Toast.LENGTH_LONG).show();

        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Team t = data.getValue(Team.class);
                    if (t.getCoachname().equals(cName)){
                        tList.add(t.getTeamname());
                    }
                }
                ArrayAdapter a = new ArrayAdapter<String>(listofteams.this,R.layout.support_simple_spinner_dropdown_item, tList);
                sp.setAdapter(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void choose(View view) {

        Toast.makeText(listofteams.this, String.valueOf(sp.getSelectedItem()), Toast.LENGTH_LONG).show();
        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Team t = data.getValue(Team.class);
                    if (t.getTeamname().equals(String.valueOf(sp.getSelectedItem()))){
                        pList=(t.getPlayerslist());
                    } }
                ArrayAdapter a = new ArrayAdapter<String>(listofteams.this,R.layout.support_simple_spinner_dropdown_item, pList);
                list1.setAdapter(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialogx, null);
        ad = new AlertDialog.Builder(this);
        ad.setMessage("remove player?");
        ad.setPositiveButton("remove", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dummy.clear();
                int j = 0;
                Toast.makeText(listofteams.this, pList.get(position), Toast.LENGTH_LONG).show();
                while (j<pList.size()){
                    if (!pList.get(j).equals(pList.get(position)))
                      dummy.add(pList.get(j));
                    j++;
                }
                refTeams.child(String.valueOf(sp.getSelectedItem())).child("Plist").setValue(dummy);
                recreate();
                dialogInterface.dismiss();
            }

        });

        //dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialogx, null);

        ad.setCancelable(false);
        //ad.setTitle("game details");

        ad.setNeutralButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog adb = ad.create();
        adb.show();



    }

    public void back(View view) {
        Intent si = new Intent(listofteams.this, Coachmain.class);
        //si.putExtra("name",co);
        startActivity(si);
    }
}
