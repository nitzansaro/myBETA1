package com.example.EZteam;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import static com.example.EZteam.FBref.refAuth;

public class profile extends AppCompatActivity {
    TextView tVnameview, tVemailview, tVidview,tVphoneview,t;
    CheckBox cBconnectview;
    FirebaseUser user = refAuth.getCurrentUser();
    String uid = user.getUid();
    String email=user.getEmail();
    String n,p1,i,c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tVnameview = (TextView) findViewById(R.id.tVnameview);
        tVemailview = (TextView) findViewById(R.id.tVemailview);
        tVidview = (TextView) findViewById(R.id.tVidview);
        tVphoneview = (TextView) findViewById(R.id.tVphoneview);
        //tVcoachview = (TextView) findViewById(R.id.tVcoachview);
        cBconnectview = (CheckBox) findViewById(R.id.cBconnectview);
        t=findViewById(R.id.titl);

        Intent ins=getIntent();
        n=ins.getStringExtra("n");
        p1=ins.getStringExtra("p");
        i=ins.getStringExtra("i");
        c=ins.getStringExtra("c");
        tVemailview.setText(email);
        tVnameview.setText(n);
        tVidview.setText(i);
        tVphoneview.setText(p1);
        t.setText(c + "  " +"Profile");
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);

    }



    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuCredits) {
            Intent si = new Intent(profile.this, credits.class);
            startActivity(si);
        }
        if (id==R.id.menuProfile) {

            Intent si = new Intent(profile.this, profile.class);
            si.putExtra("n",n);
            si.putExtra("p",p1);
            si.putExtra("i",i);
            if (c.equals("Player")){
               si.putExtra("cOp","player");}
            else { si.putExtra("cOp","coach");}
            startActivity(si);
        }

        if (id==R.id.menuout) {
            FirebaseUser user = refAuth.getCurrentUser();
            if (!cBconnectview.isChecked()){
                refAuth.signOut();
            }
            SharedPreferences settings=getSharedPreferences("PREFS_NAME1",MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("stayConnect",cBconnectview.isChecked());
            editor.apply(); //changed from commit
            finish();
            Intent si = new Intent(profile.this, MainActivity.class);
            startActivity(si);

        }

        if (id==R.id.menuMain) {
            if (c.equals("Player")){
                Intent si = new Intent(profile.this, playermain.class);
                startActivity(si); }
            else {
                if (c.equals("Coach")){
                Intent si = new Intent(profile.this,Coachmain.class);
                startActivity(si);
            }}
        }

        return true;
    }
}