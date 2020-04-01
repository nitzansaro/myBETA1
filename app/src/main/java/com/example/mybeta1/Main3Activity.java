package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.mybeta1.FBref.refAuth;
import static com.example.mybeta1.FBref.refGame;
import static com.example.mybeta1.FBref.refTeams;
import static com.example.mybeta1.FBref.refUsers;

public class Main3Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String cOp1,coachName,playerName,tname;
    Button addteam,addgame,coachteams,playerteam;

    ArrayList<String> gList = new ArrayList<>();
    FirebaseUser user = refAuth.getCurrentUser();
    String uid, email = user.getEmail();
    ListView lv;TextView tv,tv2;

    AlertDialog.Builder ad;
    LinearLayout dialog;
    TextView tv1,tv3;
    User p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        addgame=findViewById(R.id.addgame);
        addteam=findViewById(R.id.addteam);
        coachteams=findViewById(R.id.coachteams);
        playerteam=findViewById(R.id.playerteam);
        tv=findViewById(R.id.tv);
        tv2=findViewById(R.id.tv2);
        lv=findViewById(R.id.lv);

        uid = user.getUid();
        Intent i=getIntent();
        cOp1=i.getStringExtra("cOp");
        Intent i2=getIntent();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);



        /*refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (uid.equals(u.getUid())){
                        playerName=u.getName();}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Toast.makeText(Main3Activity.this, playerName, Toast.LENGTH_LONG).show();
        tv.setText("Hello " + playerName + "!");

        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Team t = ds.getValue(Team.class);
                    ArrayList<String> a = t.getPlayerslist();
                    int i=a.size();
                    i--;
                    while (i != 0){
                        if (a.get(i).equals(playerName)) {
                            tname = t.getTeamname();
                            break;
                        }
                        i--;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Toast.makeText(Main3Activity.this, playerName, Toast.LENGTH_LONG).show();
        tv2.setText(tname + "'s upcoming games");

        refGame.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Game g = data.getValue(Game.class);

                    assert g != null;
                    if (g.getTeamName().equals(tname)){
                        gList.add(g.getTeamName2());

                }}
                ArrayAdapter adp = new ArrayAdapter<String>(Main3Activity.this,R.layout.support_simple_spinner_dropdown_item, gList);
                lv.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //String str= waitList.get(position);
        //Toast.makeText(Physiolists.this, str, Toast.LENGTH_LONG).show();
        ///Intent i = new Intent(Physiolists.this, Newtask.class);
        //i.putExtra("key",str);
        //startActivity(i);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialogx, null);
        tv1 = alertLayout.findViewById(R.id.et1);
        tv3 = alertLayout.findViewById(R.id.et2);
        final String str= gList.get(position);
        ad = new AlertDialog.Builder(this);

        refGame.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //gList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Game g = data.getValue(Game.class);

                    //assert g != null;
                    if (g.getTeamName2().equals(str)){
                        tv1.setText("A " + g.getCategory() + " game versus " + str);
                        tv3.setText(g.getTime() + " " + g.getDate() + " at " + g.getPlace());
                        Toast.makeText(Main3Activity.this, g.getTeamName2() + g.getPlace(), Toast.LENGTH_LONG).show();
                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialogx, null);

        ad.setCancelable(false);
        //ad.setTitle("game details");
        //ad.setMessage("hey");
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
            Query query = refUsers.child("Player")
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
                        playerName=(user.getName());
                        p = data.getValue(User.class);

                        tv.setText("Hello " + playerName + "!");

                        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Team t = ds.getValue(Team.class);
                                    ArrayList<String> a = t.getPlayerslist();
                                    int i=a.size();
                                    i--;
                                    while (i != 0){
                                        if (a.get(i).equals(playerName)) {
                                            tname = t.getTeamname();
                                            break;
                                        }
                                        i--;
                                    }

                                    tv2.setText(tname + "'s upcoming games");

                                    refGame.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            gList.clear();
                                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                                Game g = data.getValue(Game.class);
                                                //assert g != null;
                                                if (g.getTeamName().equals(tname))
                                                  gList.add(g.getTeamName2());

                                            }
                                            ArrayAdapter adp = new ArrayAdapter<String>(Main3Activity.this,R.layout.support_simple_spinner_dropdown_item, gList);
                                            lv.setAdapter(adp);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //Toast.makeText(Main3Activity.this, playerName, Toast.LENGTH_LONG).show();

                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };





    public void toTeams(View view) {
    }

    public void toTeam(View view) {
        Intent si = new Intent(Main3Activity.this,MyTeam.class);
        si.putExtra("teamNAME", tname);
        startActivity(si);


    }



    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuCredits) {
            Intent si = new Intent(Main3Activity.this,MainActivity.class);
            startActivity(si);
        }
        if (id==R.id.menuProfile) {
            Intent si = new Intent(Main3Activity.this,Loginok.class);
            si.putExtra("n",p.getName());
            si.putExtra("p",p.getPhone());
            si.putExtra("i",p.getid());
            si.putExtra("cOp","player");
            startActivity(si);
        }

        if (id==R.id.menuMain) {
            Intent si = new Intent(Main3Activity.this,Main3Activity.class);
            startActivity(si);
        }

        return true;
    }



}