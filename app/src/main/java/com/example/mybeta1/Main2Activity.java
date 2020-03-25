package com.example.mybeta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import static com.example.mybeta1.FBref.refAuth;
import static com.example.mybeta1.FBref.refUsers;

public class Main2Activity extends AppCompatActivity {
    TextView tVtitle, tVregister, tVcoach4;
    EditText eTname, eTphone, eTemail, eTpass, eTid;
    CheckBox cBstayconnect;
    Button btn;
    Switch pOrc;
    String coach2;

    String name, phone, email, password, uid,id;
    User userdb;
    Boolean stayConnect, registered, firstrun, Coach1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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


        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        firstrun=settings.getBoolean("firstRun",false);
        Toast.makeText(this, ""+firstrun, Toast.LENGTH_SHORT).show();
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
        Intent si = new Intent(Main2Activity.this,Loginok.class);
        if (refAuth.getCurrentUser()!=null && isChecked) {
            stayConnect=true;
            startActivity(si);
        }
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

                btn.setText("Register");
                registered=false;
                logoption();
            }
        };
        ss.setSpan(span, 24, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tVregister.setText(ss);
        tVregister.setMovementMethod(LinkMovementMethod.getInstance());
    }

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
    public void logorreg(View view) {
        email=eTemail.getText().toString();
        password=eTpass.getText().toString();
        if (registered) {
            final ProgressDialog pd=ProgressDialog.show(this,"Login","Connecting...",true);
            refAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
                                SharedPreferences.Editor editor=settings.edit();
                                editor.putBoolean("stayConnect",cBstayconnect.isChecked());
                                editor.apply(); //changed from commit
                                Log.d("Main2Activity", "signinUserWithEmail:success");
                                Toast.makeText(Main2Activity.this, "Login Success", Toast.LENGTH_LONG).show();
                                Intent si = new Intent(Main2Activity.this,Loginok.class);
                                startActivity(si);
                            } else {
                                Log.d("Main2Activity", "signinUserWithEmail:fail");
                                Toast.makeText(Main2Activity.this, "e-mail or password are wrong!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            name=eTname.getText().toString();
            phone=eTphone.getText().toString();
            id=eTid.getText().toString();
            Coach1=pOrc.isChecked();
            if (Coach1)
                coach2="coach";
            else coach2="player";





            final ProgressDialog pd=ProgressDialog.show(this,"Register","Registering...",true);
            refAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
                                SharedPreferences.Editor editor=settings.edit();
                                editor.putBoolean("stayConnect",cBstayconnect.isChecked());
                                editor.putBoolean("firstRun",false);
                                editor.commit();
                                Log.d("Main2Activity", "createUserWithEmail:success");
                                FirebaseUser user = refAuth.getCurrentUser();
                                uid = user.getUid();
                                userdb=new User(name,email,phone,uid,id,coach2);
                                refUsers.child(name).setValue(userdb);
                                Toast.makeText(Main2Activity.this, "Successful registration", Toast.LENGTH_LONG).show();
                                Intent si = new Intent(Main2Activity.this,Loginok.class);
                                startActivity(si);
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(Main2Activity.this, "User with e-mail already exist!", Toast.LENGTH_LONG).show();
                                else {
                                    Log.w("Main2Activity", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Main2Activity.this, "User create failed.",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }

}
