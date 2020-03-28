package com.example.mybeta1;

import java.util.ArrayList;

public class Team {

    ArrayList Playerslist  = new ArrayList  ();

        public String Tnum;
        public String Tname;

         public String coachname;


        public Team(){}

        public Team(String Tnum, String Tname,String coachname, ArrayList Plaerslist ) {
            this.Tnum = Tnum;
            this.Tname = Tname;
            this.Playerslist=Plaerslist;

            this.coachname=coachname;
        }

    public ArrayList getPlayerslist() {
        return Playerslist;
    }

    public void setPlayerslist(ArrayList Playerslist) {
        this.Playerslist = Playerslist;
    }


}
