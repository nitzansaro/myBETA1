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

import static com.example.EZteam.FBref.refAuth;
import static com.example.EZteam.FBref.refGame;
import static com.example.EZteam.FBref.refMsg;
import static com.example.EZteam.FBref.refTeams;
import static com.example.EZteam.FBref.refUsers;

public class playermain extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String playerName, tname = "";
    Button addteam, addgame, coachteams, playerteam;
    ArrayList<String> gList = new ArrayList<>();
    ArrayList<String> mList = new ArrayList<>();
    FirebaseUser user = refAuth.getCurrentUser();
    String uid, phone,str,str1;
    ListView lv, lvmsg;
    TextView tv, tv2;
    Team t;
    AlertDialog.Builder ad;
    TextView tv1, tv3;
    User p;
    TextView  tvvv,tvv;
    Game g = new Game();
    AlertDialog.Builder add;
    LinearLayout dialog;
    /**
     * creating list view for msg and games.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playermain);
        addgame = findViewById(R.id.addgame);
        addteam = findViewById(R.id.addteam);
        coachteams = findViewById(R.id.coachteams);
        playerteam = findViewById(R.id.playerteam);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        lv = findViewById(R.id.lv);
        lvmsg = findViewById(R.id.lvmsg);
        uid = user.getUid();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        lvmsg.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvmsg.setOnItemClickListener(this);

    }

    /**
     * finding corrent user
     */
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
        phone= user.getPhoneNumber();
        uid = user.getUid();
    }

    /**
     * once user found, name updated
     */

    com.google.firebase.database.ValueEventListener VEL1 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    playerName = user.getName();
                    p = data.getValue(User.class);

                    tv.setText("Hello " + playerName + "!");
                    /**
                     * finding the player team, by going trough all the team list of player/
                     */

                    refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                t = ds.getValue(Team.class);
                                ArrayList<String> a = t.getPlayerslist();
                                int i = a.size();
                                i--;
                                while (i != 0) {
                                    if (a.get(i).equals(playerName)) {
                                        tname = t.getTeamname();
                                        break;
                                    }
                                    i--;
                                }
                                /**
                                 * find if this team has a msg- show
                                 */
                                tv2.setText(tname + "'s upcoming games");
                                if (tname.equals(" "))
                                {Intent si = new Intent(playermain.this,MyTeam.class);
                                    si.putExtra("name",playerName);
                                    startActivity(si);}

                                refMsg.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        mList.clear();
                                        for(DataSnapshot data : dataSnapshot.getChildren()){
                                            Messages m = data.getValue(Messages.class);
                                            if (m.getPLayer_msg().equals(tname))
                                                mList.add(m.getMsg());


                                        }
                                        ArrayAdapter adp = new ArrayAdapter<String>(playermain.this,R.layout.support_simple_spinner_dropdown_item, mList);
                                        lvmsg.setAdapter(adp);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                /**
                                 * has games?
                                 * - into list view title is the enemy/
                                 */

                                refGame.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        gList.clear();
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            Game g = data.getValue(Game.class);
                                            if (g.getTeamName().equals(tname))
                                                gList.add(g.getTeamName2());


                                        }
                                        ArrayAdapter adp = new ArrayAdapter<String>(playermain.this, R.layout.support_simple_spinner_dropdown_item, gList);
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
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /**
     *msg- showing the messege can delete and ok.
     */

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.lvmsg:
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                final View customLayout = getLayoutInflater().inflate(R.layout.dialogx, null);
                builder.setView(customLayout);
                TextView tv1 = customLayout.findViewById(R.id.tv);
                TextView tv2 = customLayout.findViewById(R.id.tv2);
                str1 = mList.get(position);
                builder.setTitle("Message from your coach");
                builder.setMessage(str1);


                builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterfac, int i) {
                        dialogInterfac.cancel();
                    }
                });

                android.app.AlertDialog adb = builder.create();
                adb.show();

                break;
            /**
             * case because if this is what the user choose
             * alert for game informetion
             * can only read
             */

            case R.id.lv:
                //final android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(this);


                //final View customLayout2 = getLayoutInflater().inflate(R.layout.dialogx, null);
                /*dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialogx, null);
                add = new AlertDialog.Builder(this);
                add.setView(dialog);


                ad = new AlertDialog.Builder(this);*/
                final String str = gList.get(position);
                refGame.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Game gg = data.getValue(Game.class);
                            if (gg.getTeamName2().equals(str)) {
                                Toast.makeText(playermain.this, gg.getCategory() + "      at " + gg.getTime() + " " + gg.getDate() + " at " + gg.getPlace(), Toast.LENGTH_LONG).show();
                               // add.setTitle("A " + gg.getCategory() + " game versus " + gg.getTeamName2());
                                //add.setMessage();

                            }

                        }
                        //game1.setText();
                        //game2.setText();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /*
                add.setCancelable(false);
                add.setNeutralButton("exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog ad2 = add.create();
                ad2.show();*/
                break;
        }
    }


    /**
     * move to watch team list
     */

    public void toTeam(View view) {
        Intent si = new Intent(playermain.this,MyTeam.class);
        si.putExtra("teamNAME", tname);
        si.putExtra("name",playerName);
        startActivity(si);
    }

    /**
     *new menu
     * @see Coachmain
     */

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.menuout) {
              refAuth.signOut();
                Intent si = new Intent(playermain.this, MainActivity.class);
               startActivity(si);

        }
        if (id==R.id.menuCredits) {
            Intent si = new Intent(playermain.this, credits.class);
            si.putExtra("c","player");
            startActivity(si);
        }
        if (id==R.id.menuProfile) {
            Intent si = new Intent(playermain.this, profile.class);
            si.putExtra("n",p.getName());
            si.putExtra("p",p.getPhone());
            si.putExtra("i",p.getid());

           si.putExtra("d",p.getDayofbirth());
           si.putExtra("c","player");
            startActivity(si);
        }

        if (id==R.id.menuMain) {
            Intent si = new Intent(playermain.this, playermain.class);
            startActivity(si);
        }

        return true;
    }



}