package com.example.EZteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.EZteam.FBref.refTeams;
import static com.example.EZteam.FBref.refUsers;

public class MyTeam extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView mtlv;
    String str;

    ArrayList<String> pList = new ArrayList<>();

    /**
     *create list view for players
     * cheking if has team- if not send into player add team
     * data looking for the team of player
     * adding into list view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        mtlv=findViewById(R.id.mtlv);

        mtlv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mtlv.setOnItemClickListener(this);

        final String tName=getIntent().getExtras().getString("teamNAME");
        final String Name=getIntent().getExtras().getString("name");

        if(tName==null)
        {  Intent si = new Intent(MyTeam.this, playeraddt.class);
            si.putExtra("name",Name);
            startActivity(si);}



        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Team t = data.getValue(Team.class);
                    if (t.getTeamname().equals(tName)){
                        pList=t.getPlayerslist(); }
                }
                ArrayAdapter a = new ArrayAdapter<String>(MyTeam.this,R.layout.support_simple_spinner_dropdown_item, pList);
                mtlv.setAdapter(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     *on click data looking for user in team, if found alert dialog showing info
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
                    } } }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        builder.setCancelable(false);
        builder.setNeutralButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog adb = builder.create();
        adb.show();
    }

    /**
     * back to main
     */

    public void back(View view) {
        Intent si = new Intent(MyTeam.this, playermain.class);
        startActivity(si);
        finish();
    }
}
