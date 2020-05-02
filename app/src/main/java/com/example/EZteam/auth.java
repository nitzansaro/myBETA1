package com.example.EZteam;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.EZteam.Coachmain;
import com.example.EZteam.R;
import com.example.EZteam.User;

import com.example.EZteam.addTeam;
import com.example.EZteam.playeraddt;
import com.example.EZteam.playermain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.Calendar;
import java.util.regex.Pattern;

import static com.example.EZteam.FBref.refAuth;
import static com.example.EZteam.FBref.refUsers;





public class auth extends AppCompatActivity {
    TextView tVtitle, tVregister, tVcoach4,tVb;
    EditText eTname, eTphone, eTemail, eTpass, eTid;
    CheckBox cBstayconnect;
    Button btn;
    Switch pOrc;
    Boolean coach;
    Boolean c=true;

    String name, phone, email, password, uid,id,birthday;
    User userdb;
    Boolean stayConnect, registered, firstrun, Coach1;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
        pOrc = findViewById(R.id.pOrc);
        tVcoach4=findViewById(R.id.tVcoach4);
        tVtitle=(TextView) findViewById(R.id.tVtitle);
        eTid=(EditText)findViewById(R.id.eTid);
        eTname=(EditText)findViewById(R.id.eTname);
        eTemail=(EditText)findViewById(R.id.eTemail);
        eTpass=(EditText)findViewById(R.id.eTpass);
        eTphone=(EditText)findViewById(R.id.eTphone);
        cBstayconnect=(CheckBox)findViewById(R.id.cBstayconnect);

        tVregister=(TextView) findViewById(R.id.tVregister);
        btn=(Button)findViewById(R.id.btn);

        stayConnect=false;
        registered=true;
        Coach1=false;
        /**
         * dialog day of birth, the user should enter his correct date.

         */

        tVb = findViewById(R.id.tVb);
        tVb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                year = year - 25;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        auth.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        /**
         * listener for when the user set his date.
         * user's day of birth = birthday .
         */
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                birthday = dayOfMonth + "/" + month + "/" + year;


            }
        };


        /**
         * checking whether this is the first run or not using Shared preferences.

         */

        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        firstrun=settings.getBoolean("firstRun",false);

        if (firstrun) {
            tVtitle.setText("Register");
            eTname.setVisibility(View.VISIBLE);
            eTphone.setVisibility(View.VISIBLE);
            eTid.setVisibility(View.VISIBLE);
            pOrc.setVisibility(View.VISIBLE);
            tVcoach4.setVisibility(View.VISIBLE);
            btn.setText("Register");
            registered=false;
            logoption();
        }
        else regoption();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);

    }

    /**
     * On activity pause - If logged in & asked to be remembered - kill activity.
     * <p>
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (stayConnect) finish();
    }

    /**
     * registration screen, everything visible .
     * span- to change into login and updating isuid to true because the user already exist .
     */

    private void regoption() {
        SpannableString ss = new SpannableString("Don't have an account?  Register here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                tVtitle.setText("Register");
                eTname.setVisibility(View.VISIBLE);
                eTphone.setVisibility(View.VISIBLE);
                eTid.setVisibility(View.VISIBLE);
                pOrc.setVisibility(View.VISIBLE);
                tVcoach4.setVisibility(View.VISIBLE);
                tVb.setVisibility(View.VISIBLE);
                eTpass.setVisibility(View.VISIBLE);
                eTemail.setVisibility(View.VISIBLE);


                btn.setText("Register");
                registered=false;
                logoption();
            }
        };
        ss.setSpan(span, 24, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tVregister.setText(ss);
        tVregister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * login screen, just mail and password.
     * span- to change into registration .
     */

    private void logoption() {
        SpannableString ss = new SpannableString("Already have an account?  Login here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                tVtitle.setText("Login");
                eTname.setVisibility(View.INVISIBLE);
                eTphone.setVisibility(View.INVISIBLE);
                eTid.setVisibility(View.INVISIBLE);
                pOrc.setVisibility(View.INVISIBLE);
                tVcoach4.setVisibility(View.INVISIBLE);
                tVb.setVisibility(View.INVISIBLE);
                eTpass.setVisibility(View.VISIBLE);
                eTemail.setVisibility(View.VISIBLE);


                btn.setText("Login");
                registered=true;
                regoption();
            }
        };
        ss.setSpan(span, 26, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tVregister.setText(ss);
        tVregister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Logging in or Registering to the application
     * Using:   Firebase Auth with email & password
     *          Firebase Realtime database with the object User to the branch Users
     * If login or register process is Ok saving stay connect status & pass to next activity
     * <p>
     */
    public void options(View view) {
        email = eTemail.getText().toString();
        password = eTpass.getText().toString();
        if (registered) {
            final ProgressDialog pd = ProgressDialog.show(this, "Login", "Connecting...", true);
            refAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("stayConnect", cBstayconnect.isChecked());
                                editor.apply(); //changed from commit
                                Log.d("Main2Activity", "signinUserWithEmail:success");
                                Toast.makeText(auth.this, "Login Success", Toast.LENGTH_LONG).show();

                                refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            User u = ds.getValue(User.class);
                                            if (email.equals(u.getEmail())) {

                                                if (u.getCoach()) {
                                                    Intent si = new Intent(auth.this, Coachmain.class);
                                                    si.putExtra("name", name);
                                                    startActivity(si);
                                                } else {

                                                    Intent si = new Intent(auth.this, playermain.class);
                                                    si.putExtra("name", name);
                                                    startActivity(si);
                                                }}}
                                            }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else {
                                Log.d("Main2Activity", "signinUserWithEmail:fail");
                                Toast.makeText(auth.this, "e-mail or password are wrong!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            name = eTname.getText().toString();
            phone = eTphone.getText().toString();
            if (phone.isEmpty()) eTphone.setError("you must enter a phone number");
            id = eTid.getText().toString();
            if (name.isEmpty()) eTname.setError("you must enter your name");
            if (id.isEmpty()) eTid.setError("you must enter your id");
            if (birthday.isEmpty()) tVb.setError("you must enter your birth day");
            if (pOrc.isChecked())
                coach = true;
            else
                coach= false;
            if ((!name.isEmpty()) && (!email.isEmpty()) &&(!birthday.isEmpty())&&
                    (!phone.isEmpty()) && (!id.isEmpty()) ) {
                if (((!email.endsWith(".com")) || (!email.endsWith(".il"))) && (email.indexOf("@") == (-1)))
                   eTemail.setError("invalid e-mail");
                else{
                    if (Pattern.matches("[a-zA-Z]+", id) == true || id.length() != 9)
                                        eTid.setError("invalid id");
                else {
                    if ((phone.length() != 10) || (!phone.substring(0, 2).equals("05")) || Pattern.matches("[a-zA-Z]+", phone) == true)
                                                eTphone.setError("invalid phone number");
           }}}







            final ProgressDialog pd = ProgressDialog.show(this, "Register", "Registering...", true);
            refAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {

                                SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("stayConnect", cBstayconnect.isChecked());
                                editor.putBoolean("firstRun", false);
                                editor.putBoolean("coach",pOrc.isChecked());
                                editor.apply();
                                Log.d("Main2Activity", "createUserWithEmail:success");

                                FirebaseUser user = refAuth.getCurrentUser();

                                uid = user.getUid();
                                userdb = new User(name, phone, uid, id,coach, email,birthday);

                                if (coach) {
                                    refUsers.child(name).setValue(userdb);
                                    Toast.makeText(auth.this, "Successful registration", Toast.LENGTH_LONG).show();

                                    Intent si = new Intent(auth.this, addTeam.class);

                                    si.putExtra("name",name);
                                    startActivity(si);
                                } else {
                                    refUsers.child(name).setValue(userdb);
                                    Toast.makeText(auth.this, "Successful registration", Toast.LENGTH_LONG).show();
                                    Intent si = new Intent(auth.this, playeraddt.class);
                                    si.putExtra("name",name);
                                    startActivity(si);

                                }
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(auth.this, "User with e-mail already exist!", Toast.LENGTH_LONG).show();
                                else {
                                    Log.w("Main2Activity", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(auth.this, "User create failed.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }}