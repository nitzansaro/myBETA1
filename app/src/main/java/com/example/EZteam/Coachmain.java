package com.example.EZteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.EZteam.FBref.refAuth;
import static com.example.EZteam.FBref.refGame;
import static com.example.EZteam.FBref.refMsg;
import static com.example.EZteam.FBref.refTeams;
import static com.example.EZteam.FBref.refUsers;

public class Coachmain extends AppCompatActivity implements AdapterView.OnItemClickListener {
     TextView tvNAME ,tvGAME;

    TextView tv1,tv3;
    ArrayList<String> gList = new ArrayList<>();
    ArrayList<String> msgList = new ArrayList<>();
    ArrayList tList = new ArrayList<>();
    ListView lv;

    String c1,coachName;
    FirebaseUser user = refAuth.getCurrentUser();
    String uid=user.getUid(), email = user.getEmail();
    AlertDialog.Builder ad;
    User p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coachmain);
        lv=findViewById(R.id.lv);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        tvNAME=findViewById(R.id.tvNAME);
        tvGAME=findViewById(R.id.tvGame);
    }


    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialogx, null);
        tv1 = alertLayout.findViewById(R.id.tv1);
        tv3 = alertLayout.findViewById(R.id.tv2);
        final String str= gList.get(position);
        ad = new AlertDialog.Builder(this);

        refGame.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Game g = data.getValue(Game.class);
                    assert g != null;
                    if (g.getDate().equals(gList.get(position))){
                        tv1.setText("A " + g.getCategory() + " game versus " + g.getTeamName2());
                        tv3.setText(g.getTime() + " " + g.getDate() + " at " + g.getPlace());
                        ad.setTitle(g.getTeamName());
                        Toast.makeText(Coachmain.this, g.getTeamName2() + g.getPlace(), Toast.LENGTH_LONG).show();
                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ad.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Coachmain.this, gList.get(position), Toast.LENGTH_LONG).show();
                refGame.child(gList.get(position)).removeValue();
                recreate();
                dialogInterface.dismiss();
            }
        });
        ad.setCancelable(false);
        ad.setView(alertLayout);
        ad.setNeutralButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog adb = ad.create();
        adb.show();
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseUser user = refAuth.getCurrentUser();
        uid = user.getUid();
        Query query = refUsers.child("Coach")
                .orderByChild("uid")
                .equalTo(uid)
                .limitToFirst(1);
        query.addListenerForSingleValueEvent(VEL1);
        email = user.getEmail();
        uid = user.getUid(); }




    com.google.firebase.database.ValueEventListener VEL1 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    coachName=(user.getName());
                    p = data.getValue(User.class);
                    tvNAME.setText("Hello " + coachName + "!");
                    refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Team t = ds.getValue(Team.class);
                                c1=t.getCoachname();
                                ArrayList<String> a = t.getPlayerslist();
                                if(c1.equals(coachName))
                                    tList.add(t.getTeamname());
                                tvGAME.setText("your teams upcoming games");
                                refGame.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        gList.clear();
                                        for(DataSnapshot data : dataSnapshot.getChildren()){
                                            Game g = data.getValue(Game.class);
                                            int i = tList.size();
                                            i--;
                                            while (i>=0){
                                                if (tList.get(i).equals(g.getTeamName()))
                                                    gList.add(g.getDate());
                                                i--;

                                            }
                                        }
                                        Collections.sort(gList);
                                        ArrayAdapter adp = new ArrayAdapter<String>(Coachmain.this,R.layout.support_simple_spinner_dropdown_item, gList);
                                        lv.setAdapter(adp);

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            } }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }); }
            }}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };


    public void toTeams(View view) {
        Intent si = new Intent(Coachmain.this,listofteams.class);
        si.putExtra("cname",coachName);
        startActivity(si);

    }

    public void toaddTeam(View view) {

        Intent si = new Intent(Coachmain.this,addTeam.class);
        si.putExtra("name",coachName);
        startActivity(si);

    }

    public void toaddGAME(View view) {
        Intent si = new Intent(Coachmain.this,gameInfo.class);
        si.putExtra("n",coachName);
        startActivity(si);
    }


    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuCredits) {
            Intent si = new Intent(Coachmain.this, credits.class);
            si.putExtra("c","coach");
            startActivity(si);
        }
        if (id==R.id.menuProfile) {
            Intent si = new Intent(Coachmain.this, profile.class);
           si.putExtra("n",p.getName());
           si.putExtra("p",p.getPhone());
           si.putExtra("i",p.getid());
           si.putExtra("c","coach");

            startActivity(si);
        }

        if (id==R.id.menuMain) {
            Intent si = new Intent(Coachmain.this,Coachmain.class);
            startActivity(si);
        }

        if (id==R.id.menuout) {
            refAuth.signOut();
            Intent si = new Intent(Coachmain.this, MainActivity.class);
            startActivity(si);

        }

        return true;
    }

    public void newmsg(View view) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);


        final View customLayout = getLayoutInflater().inflate(R.layout.dialogx, null);
        builder.setView(customLayout);
        TextView tv=customLayout.findViewById(R.id.tv1);
        TextView tv2=customLayout.findViewById(R.id.tv2);
        EditText edmsg = customLayout.findViewById(R.id.edmsg);

        tv.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);

        edmsg.setVisibility(View.VISIBLE);

        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgList.clear();
                Spinner spinner = customLayout.findViewById(R.id.msgspn);
                spinner.setVisibility(View.VISIBLE);
                msgList.add("all teams");
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Team t = data.getValue(Team.class);
                    if (t.getCoachname().equals(coachName)){
                        msgList.add(t.getTeamname());
                    }
                }
                ArrayAdapter a = new ArrayAdapter<String>(Coachmain.this,R.layout.support_simple_spinner_dropdown_item, msgList);
                spinner.setAdapter(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        builder.setTitle("New Message");

        /**/
        builder.setPositiveButton("Send Message", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                final EditText edmsg2 = customLayout.findViewById(R.id.edmsg);
                final String msg = edmsg2.getText().toString();
                final Spinner spinner = customLayout.findViewById(R.id.msgspn);
                final String tmsg = String.valueOf(spinner.getSelectedItem());
                refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            Team t = data.getValue(Team.class);

                            //Toast.makeText(Patientlists.this, exList.get(position), Toast.LENGTH_LONG).show();
                            if (tmsg.equals("all teams")){
                            if ((coachName.equals(t.getCoachname()))){
                                Messages m = new Messages(coachName,t.getTeamname(),msg);
                                refMsg.child(t.getTeamname()+" "+msg).setValue(m);
                            }}
                            else {
                                if ((tmsg.equals(t.getTeamname()))){
                                    Messages m = new Messages(coachName,t.getTeamname(),msg);
                                    refMsg.child(t.getTeamname()+" "+msg).setValue(m);
                            }}
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                Toast.makeText(Coachmain.this, "Message Sent!" , Toast.LENGTH_LONG).show();
                dialogInterface.cancel();
            }
        });

        builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterfac, int i) {
                dialogInterfac.cancel();
            }
        });

        android.app.AlertDialog adb = builder.create();
        adb.show();
    }
}
