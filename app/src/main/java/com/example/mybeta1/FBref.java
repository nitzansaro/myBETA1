package com.example.mybeta1;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {



    public static FirebaseAuth refAuth=FirebaseAuth.getInstance();
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();


    public static DatabaseReference refUsers=FBDB.getReference("Users");

    public static DatabaseReference RefMesseges= FBDB.getReference("message");

    public static DatabaseReference refTeam=FBDB.getReference("Team");
    public static DatabaseReference refGame=FBDB.getReference("Game");

}
