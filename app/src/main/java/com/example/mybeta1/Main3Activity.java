package com.example.mybeta1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void toGames(View view) {
    }

    public void toMesseges(View view) {
    }

    public void toTeams(View view) {
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
            startActivity(si);
        }

        if (id==R.id.menuMain) {
            Intent si = new Intent(Main3Activity.this,Main3Activity.class);
            startActivity(si);
        }

        return true;
    }
}
