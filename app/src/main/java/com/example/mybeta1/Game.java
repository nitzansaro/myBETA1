package com.example.mybeta1;

import java.sql.Time;

public class Game {
    public String teamName,teamName2,place,category, time;


    public Game(){}
         public Game(String teamName, String teamName2,String place, String category ,String time) {
            this.teamName = teamName;
            this.teamName2= teamName2;
            this.category=category;
            this.place=place;
            this.time=time;

    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public String getTeamName2() {
        return teamName2;
    }

    public void setTeamName2(String teamName2) {
        this.teamName2 = teamName2;
    }



    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }




    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }




    public String getTime() {
        return time;
    }

    public void setTime( String time) {
        this.time = time;
    }



}
