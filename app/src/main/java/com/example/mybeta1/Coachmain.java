package com.example.mybeta1;

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
import android.widget.Button;
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

public class Coachmain extends AppCompatActivity implements AdapterView.OnItemClickListener {
     TextView tvNAME ,tvGAME;

    TextView tv1,tv3;
    ArrayList<String> gList = new ArrayList<>();
    ArrayList tList = new ArrayList<>();
  ListView lv;


    String c1,coachName,tname;
    FirebaseUser user = refAuth.getCurrentUser();
    String uid=user.getUid(), email = user.getEmail();
    AlertDialog.Builder ad;
    LinearLayout dialog;

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
                //String str= ClientList.get(position);
                Toast.makeText(Coachmain.this, gList.get(position), Toast.LENGTH_LONG).show();
                refGame.child(gList.get(position)).removeValue();
                recreate();
                dialogInterface.dismiss();
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

                               // if (a.get(0).equals(coachName)){
                                //if (t.getCoachname().equals(coachName)){
                                if(c1.equals(coachName))
                                    tList.add(t.getTeamname());


                                tvGAME.setText("your teams upcoming games");

                                refGame.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        gList.clear();
                                        for(DataSnapshot data : dataSnapshot.getChildren()){
                                            Game g = data.getValue(Game.class);
                                            //assert g != null;
                                            int i = tList.size();
                                            i--;
                                            while (i>=0){
                                                if (tList.get(i).equals(g.getTeamName()))
                                                    gList.add(g.getDate());
                                                i--;
                                            }
                                            //if (g.getTeamName().equals(tname))


                                        }
                                        ArrayAdapter adp = new ArrayAdapter<String>(Coachmain.this,R.layout.support_simple_spinner_dropdown_item, gList);
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
            Intent si = new Intent(Coachmain.this,Myteams.class);
            si.putExtra("c","coach");
            startActivity(si);
        }
        if (id==R.id.menuProfile) {
            Intent si = new Intent(Coachmain.this,Loginok.class);
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
            Intent si = new Intent(Coachmain.this,MainActivity.class);
            startActivity(si);

        }

        return true;
    }

}
