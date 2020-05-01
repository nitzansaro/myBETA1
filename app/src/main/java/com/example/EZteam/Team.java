package com.example.EZteam;

import java.util.ArrayList;


public class Team {
    public String teamnum;
    public String teamname;
    public String coachname;
    public  ArrayList<String> Plist  = new ArrayList  ();

    /**
     * @author		Nitzan Saroudi <nitzan2892002@gmail.com>
     * @since		4/1/2020
     * class for the teams.
     */


    public Team(){}
    public Team(String teamnum, String teamname, String coachname, ArrayList<String> Plist ) {
        this.teamnum = teamnum;
        this.teamname = teamname;
        this.coachname = coachname;
        this.Plist = Plist;
    }

    public void setTeamnum(String teamnum) {
        this.teamnum = teamnum;
    }

    public String getTeamnum() {
        return teamnum;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setPlayerslist(ArrayList<String  > Plist) {
        this.Plist = Plist;
    }

    public ArrayList<String> getPlayerslist() {
        return Plist;
    }

}