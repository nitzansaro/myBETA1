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
    AlertDialog.Builder ad;
    LinearLayout dialog;

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
        final View customLayout = getLayoutInflater().inflate(R.layout.dialogx, null);
        builder.setView(customLayout);
        TextView tv1 = customLayout.findViewById(R.id.tv);
        TextView tv2 = customLayout.findViewById(R.id.tv2);
        str = pList.get(position);
        builder.setTitle(str);

        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (str.equals(u.getName())) {
                        TextView tv=customLayout.findViewById(R.id.tv);
                        TextView tv2=customLayout.findViewById(R.id.tv2);
                        tv.setText("email: " +u.getEmail() + " id: " + u.getid());
                        tv2.setText("phone num: " +u.getPhone() + " d.o.b: " + u.getDayofbirth());

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ad.setPositiveButton("remove player", new DialogInterface.OnClickListener() {

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


        ad.setCancelable(false);


        ad.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog adb = ad.create();
        adb.show();



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
