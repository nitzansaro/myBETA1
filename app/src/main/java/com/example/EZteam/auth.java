package com.example.EZteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


import static com.example.EZteam.FBref.refUsers;

public class auth extends AppCompatActivity {
    TextView tVtitle, tVregister, tVcoach, tVb;
    EditText eTname, eTphone,eTid,eTemail;
    CheckBox cBstayconnect;
    Switch pOrc;
    public static FirebaseAuth refAuth=FirebaseAuth.getInstance();
    AlertDialog ad1;

    private static final String TAG = "Phone";
    String name, phone, uid,id,code,mVerificationId,birthday,email;
    User currentUser;
    Boolean stayConnect, registered, firstrun1,isUID=false,coach,isCoach;
    Boolean mVerificationInProgress = false;
    Button btn;
    FirebaseUser user;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    ValueEventListener usersListener;
    DatePickerDialog.OnDateSetListener mDateSetListener;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);

        /**
         * dialog day of birth, the user should enter his correct date.

         */
        tVb=findViewById(R.id.tVb);
        tVb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                year=year-25;
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
        mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                birthday = dayOfMonth +"/" + month +"/" +year;


            }
        };

        pOrc = findViewById(R.id.pOrc);
        tVcoach=findViewById(R.id.tVcoach4);
        tVtitle=(TextView) findViewById(R.id.tVtitle);
        eTid=(EditText)findViewById(R.id.eTid);
        eTname=(EditText)findViewById(R.id.eTname);
        eTphone=(EditText)findViewById(R.id.eTphone);
        eTemail=(EditText)findViewById(R.id.eTemail);
        cBstayconnect=(CheckBox)findViewById(R.id.cBstayconnect);
        tVregister=(TextView) findViewById(R.id.tVregister);
        btn=(Button)findViewById(R.id.btn);
        stayConnect = false;
        registered = false;


        /**
         * checking whether this is the first run or not using Shared preferences.

         */
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        firstrun1=settings.getBoolean("firstRun",true);

        if (firstrun1) {

            onVerificationStateChanged();
            regoption();
        }
        else {
            registered = true;
            onVerificationStateChanged();
            logoption();
        }
    }

    /**
     * once the activity stars, checking whether the user choosed to stay connected or not
     */

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        if (refAuth.getCurrentUser()!=null && isChecked) {
            stayConnect=true;
            setUsersListener();
        }
    }


    /**
     * when the user left the activity - if he choose to stay connected finish the activity.
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (stayConnect) finish();

    }

    /**
     * login screen, just phone auth.
     *span- to change into registration .
     */


    private void logoption() {
        tVtitle.setText("Login");
        eTemail.setVisibility(View.INVISIBLE);
        eTname.setVisibility(View.INVISIBLE);
        pOrc.setVisibility(View.INVISIBLE);
        tVcoach.setVisibility(View.INVISIBLE);
        eTid.setVisibility(View.INVISIBLE);
        registered=true;
        btn.setText("Login");

        SpannableString ss = new SpannableString("Don't have an account?  Register here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                registered=false;
                isUID=false;
                regoption();
            }
        };
        ss.setSpan(span, 24, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tVregister.setText(ss);
        tVregister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     *registration screen, everything visible .
     *span- to change into login and updating isuid to true because the user already exist .
     */

    private void regoption() {
        tVtitle.setText("Register");
        eTname.setVisibility(View.VISIBLE);
        eTphone.setVisibility(View.VISIBLE);
        pOrc.setVisibility(View.VISIBLE);
        tVcoach.setVisibility(View.VISIBLE);
        eTid.setVisibility(View.VISIBLE);
        eTemail.setVisibility(View.VISIBLE);
        btn.setText("register");
      //  registered=false;

        SpannableString ss = new SpannableString("Already have an account?  Login here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                isUID=true;
                registered=true;
                logoption();
            }
        };
        ss.setSpan(span, 26, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tVregister.setText(ss);
        tVregister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Logging in or Registering to the application
     * Using:   Firebase Auth with phone and sms code
     *          Firebase Realtime database with the object User
     * If login or register process is Ok saving stay connect status

     */

    public void options(View view) {
        if (registered){
            phone = eTphone.getText().toString();
            if (phone.isEmpty()) eTphone.setError("you must enter a phone number");
            else {
                if (!phone.startsWith("+972")) phone = "+972" + phone;

                startPhoneNumberVerification(phone);
                onVerificationStateChanged();

                AlertDialog.Builder adb1 = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                adb1.setMessage("enter the code");
                adb1.setTitle("Authentication");
                adb1.setView(edittext);
                adb1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        code = edittext.getText().toString();
                        if (!code.isEmpty())
                            verifyPhoneNumberWithCode(mVerificationId, code);
                    }
                });
                ad1= adb1.create();
                ad1.show();
                Toast.makeText(this, "the code is on his way to your phone", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            name = eTname.getText().toString();
            id = eTid.getText().toString();
            email=eTemail.getText().toString();
            phone = eTphone.getText().toString();
            uid="a";

            if (pOrc.isChecked())  coach=true;
            else  coach=false;
            if (name.isEmpty()) eTname.setError("you must enter a name");
            if (email.isEmpty()) eTemail.setError("you must enter an email");
            if (id.isEmpty()) eTid.setError("you must enter id ");
            if (phone.isEmpty()) eTphone.setError("you must enter a phone number");
            if (!name.isEmpty() && !id.isEmpty() && !phone.isEmpty()) {
               // if (Pattern.matches("[a-zA-Z]+", id) == true || id.length() != 9)
                //    eTid.setError("invalid id");
                //if (((!email.endsWith(".com")) || (!email.endsWith(".il"))) && (email.indexOf("@") == (-1)))
                 //   eTemail.setError("invalid e-mail");}
               // else {

                if (!phone.startsWith("+972")) phone = "+972" + phone;

                logoption();

        }}}


    /**
     * this function is called when the user wants to login.
     * the function sends sms to his phone number with a verification code.
     *
     * @param	phoneNumber the phone number of the user. The SMS is sent to this phone number.
     */

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        mVerificationInProgress = true;
    }

    /**
     * this function is called to check if the code the user wrote is the code he received and create a credential.
     * if he wrote a right code, "signInWithPhoneAuthCredential" function is called.
     * @param	code the code that the
     * @param verificationId a verification identity to connect with firebase servers.
     */
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    /**
     * this function is called to sign in the user.
     * saving  SharedPreferences- stayConnect,firstRun,coach according to user.

     * if the credential is proper the user is signs in and he sent to the next activity, depends on his status (coach or player)
     * @param	credential a credential that everything was right and he can sign in.
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        refAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("stayConnect", cBstayconnect.isChecked());
                            editor.putBoolean("firstRun", false);
                            editor.putBoolean("coach", isCoach);
                            editor.commit();

                            FirebaseUser user = refAuth.getCurrentUser();
                            uid = user.getUid();
                           if (!isUID){
                              // SharedPreferences settings1=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
                              // Boolean isCoach =settings1.getBoolean("coach",false);
                             //  if (isCoach)
                             User userdb = new User(name,phone,uid,id,coach,email,birthday);
                            refUsers.child(name).setValue(userdb);
                                //else
                                   // refUsers.child("Player").child(name).child("uid").setValue(uid);
                           }
                            setUsersListener();
                        }

                        else {
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(auth.this, "wrong!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * this function checks the status of the verification, if it's completed, failed or inProgress.
     */
    private void onVerificationStateChanged() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(auth.this, "Invalid phone number or code.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(auth.this, "Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
            }
        };
    }

    /**
     * this function connect the current user with his information in the database by checking his uid,
     * in purpose to check his status and sent him to the right activity.
     */

    public void setUsersListener() {
        user = refAuth.getCurrentUser();
        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (user.getUid().equals(data.getValue(User.class).getUid())){
                        currentUser=data.getValue(User.class);
                        if (currentUser.getCoach()){
                            Intent si = new Intent(auth.this, Coachmain.class);
                            startActivity(si);
                            finish();
                        }
                        else {
                            Intent si = new Intent(auth.this, playermain.class);
                            startActivity(si);
                            finish();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        refUsers.addValueEventListener(usersListener);
        //refUsers.child("Player").addValueEventListener(usersListener);
    }


}