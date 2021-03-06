package com.example.EZteam;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {

    public static FirebaseAuth refAuth=FirebaseAuth.getInstance();
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();

    public static DatabaseReference refUsers=FBDB.getReference("Users");
    public static DatabaseReference refTeams=FBDB.getReference("Teams");
    public static DatabaseReference refGame=FBDB.getReference("Game");
    public static DatabaseReference refMsg=FBDB.getReference("Messages");
}
