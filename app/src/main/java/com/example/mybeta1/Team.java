package com.example.mybeta1;

import java.util.ArrayList;

public class Team {

              public  ArrayList<String> Playerslist  = new ArrayList  ();

        public String Tnum;
        public String Tname;

         public String coachname;


        public Team(){}

        public Team(String Tnum, String Tname,String coachname, ArrayList<String> Playerslist ) {
                this.Tnum = Tnum;
            this.Tname = Tname;
            this.Playerslist=Playerslist;

            this.coachname=coachname;
        }

    public ArrayList<String> getPlayerslist() {
        return Playerslist;
    }

    public void setPlayerslist(ArrayList<String  > Playerslist) {
        this.Playerslist = Playerslist;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }
    public String getTname() {
        return Tname;
    }

    public void setTname(String Tname) {
        this.Tname = Tname;
    }

    public String getTnum() {
        return Tnum;
    }

    public void setTnum(String Tnum) {
        this.Tnum = Tnum;
    }





}
