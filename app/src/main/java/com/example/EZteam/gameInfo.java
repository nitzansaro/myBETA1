package com.example.EZteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.EZteam.FBref.refAuth;
import static com.example.EZteam.FBref.refGame;
import static com.example.EZteam.FBref.refTeams;
import static com.example.EZteam.FBref.refUsers;

public class gameInfo extends AppCompatActivity {
    Button time;
    Spinner teamName, category;
    EditText place, teamName2;
    String p1,t1,t2,c1,d1;
    String co,date;
    FirebaseAuth mAuth;
    String cname,date1;
    FirebaseUser user = refAuth.getCurrentUser();
    String uid = user.getUid();
    ArrayList<String> teamList = new ArrayList<>();
    TextView mDisplayDate;
    private static final String TAG = "gameinfo";
    DatePickerDialog.OnDateSetListener mDateSetListener;
    BroadcastG broadcastg;
    String thisdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        broadcastg = new BroadcastG();

        mDisplayDate=(TextView)findViewById(R.id.tvdate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        gameInfo.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
                Log.d(TAG,"onDataSet: dd/mm/yyyy:" + dayOfMonth +"/" + month +"/" +year );
                date = year+"-" + month +"-" + dayOfMonth;
                mDisplayDate.setText(date);

            }

        };

       Intent i=getIntent();
        co=i.getStringExtra("n");
        mAuth = FirebaseAuth.getInstance();


        place = findViewById(R.id.place);
        teamName = findViewById(R.id.teamName);
        teamName2 = findViewById(R.id.teamName2);
        time = findViewById(R.id.time);
        category  = findViewById(R.id.catagory);


        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (uid.equals(u.getUid()))
                        cname=u.getName();
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refTeams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Team t = data.getValue(Team.class);
                    if (co.equals(t.getCoachname()))
                        teamList.add(t.getTeamname());
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
    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(broadcastg,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }


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

        date1= mDisplayDate.getText().toString();
        p1=place.getText().toString();
        t2=teamName2.getText().toString();
        t1=String.valueOf(teamName.getSelectedItem());
        c1=String.valueOf(category.getSelectedItem());
        time.getText().toString();
        Game game=new Game(t1,t2,p1,c1,time.getText().toString(),date1);
        refGame.child(date).setValue(game);

        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString("thisday",thisdate);
        editor.putString("gameday",date1);
        editor.putString("team1",t1);
        editor.putString("team2",t2);
        editor.putString("time",time.getText().toString());
        editor.commit();
        moveTaskToBack(true);
        Intent si = new Intent(gameInfo.this,Coachmain.class);
        startActivity(si);}


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("BACK")
                .setMessage("Are you sure you want to exit? Any change will be deleted ")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        gameInfo.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastg);
    }
}
