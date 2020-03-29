package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.mybeta1.FBref.refAuth;
import static com.example.mybeta1.FBref.refGame;
import static com.example.mybeta1.FBref.refTeam;
import static com.example.mybeta1.FBref.refUsers;

public class gameInfo extends AppCompatActivity {
    Button time;
    Spinner teamName, category;
    EditText place, teamName2;
    String place1,teamname1,teamname2,category1;


    FirebaseAuth mAuth;
    String cname;
    FirebaseUser user = refAuth.getCurrentUser();
    String uid = user.getUid();
    ArrayList<String> teamList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        mAuth = FirebaseAuth.getInstance();


        place = findViewById(R.id.place);
        teamName = findViewById(R.id.teamName);
        teamName2 = findViewById(R.id.teamName2);
        time = findViewById(R.id.time);
        category  = findViewById(R.id.catagory);

        //final List<String> pList = new ArrayList<>();

        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (uid.equals(u.getUid()))
                        cname=u.getName();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Toast.makeText(Physiolists.this, , Toast.LENGTH_LONG).show();

        refTeam.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Team t = data.getValue(Team.class);
                    if (cname.equals(t.getCoachname()))
                        teamList.add(t.getTname());
                }
                ArrayAdapter adp = new ArrayAdapter<String>(gameInfo.this,R.layout.support_simple_spinner_dropdown_item, teamList);
                teamName.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        List<String> list = new ArrayList<String>();
        list.add("League game");
        list.add("Cup competition");
        list.add("Training session");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(gameInfo.this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(dataAdapter);

    }



    //



    public void chooseTime(View view) {
        TimePickerDialog picker = new TimePickerDialog(gameInfo.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        time.setText(sHour + ":" + sMinute);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        picker.show();

    }


    public void next(View view) {

        place1=place.getText().toString();
        teamname2=teamName2.getText().toString();
        teamname1=String.valueOf(teamName.getSelectedItem());
        category1=String.valueOf(category.getSelectedItem());time.getText().toString();

        Game game=new Game(teamname1,teamname2,place1,category1,time.getText().toString());
        refGame.child(cname).setValue(game);




    }
}
