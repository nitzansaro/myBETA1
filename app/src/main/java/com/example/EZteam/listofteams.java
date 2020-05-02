package com.example.EZteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.EZteam.FBref.refTeams;
import static com.example.EZteam.FBref.refUsers;

public class listofteams extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView list1;
    Button b; Spinner sp;
    ArrayList<String> pList = new ArrayList<>(),tList = new ArrayList<>(),Pylist = new ArrayList<>();
    String co,str;
    AlertDialog.Builder ad_lift1;
    LinearLayout dialog;
    TextView tv2,tv1;
    String p2,p1;

    /**
     * intent with name
     * create list
     * @return list with teams
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofteams);
        b=findViewById(R.id.b) ;
        sp=findViewById(R.id.spin);
        list1=findViewById(R.id.list1);

        final String cName=getIntent().getStringExtra("cname");
        co=cName;
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list1.setOnItemClickListener(this);

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

    /**
     * on click team
     * setting list of players
     * @param view
     */
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

    /**
     * alert dialog
     * info,delete, ok

     */

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
         final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
         View customLayout1 = getLayoutInflater().inflate(R.layout.coach_dialog, null);
        builder.setView(customLayout1);

        str = pList.get(position);
        builder.setTitle(str);
         tv1 = customLayout1.findViewById(R.id.part1);
         tv2 = customLayout1.findViewById(R.id.part2);
        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (str.equals(u.getName())) {

                       tv1.setText("email: " +u.getEmail() + " id: " + u.getid());
                        tv2.setText("phone num: " +u.getPhone() + " d.o.b: " + u.getDayofbirth());
                    } }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        builder.setPositiveButton("remove player", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Pylist.clear();
                int j = 0;
                Toast.makeText(listofteams.this, pList.get(position), Toast.LENGTH_LONG).show();
                while (j<pList.size()){
                    if (!pList.get(j).equals(pList.get(position)))
                      Pylist.add(pList.get(j));
                    j++;
                }
                refTeams.child(String.valueOf(sp.getSelectedItem())).child("Plist").setValue(Pylist);
                recreate();
                dialogInterface.dismiss();
            }

        });


        builder.setCancelable(false);


        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        android.app.AlertDialog ad_lift = builder.create();
        ad_lift.show();



    }

    /**
     * back to main

     */

    public void back(View view) {
        Intent si = new Intent(listofteams.this, Coachmain.class);
        startActivity(si);
        finish();
    }
}
