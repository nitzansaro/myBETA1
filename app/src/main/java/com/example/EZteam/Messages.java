package com.example.EZteam;

class Messages {
    private String coach,player,msg;

    /**
     * @author		Nitzan Saroudi <nitzan2892002@gmail.com>
     * @since		30/5/2020
     * class for the msg.
     */


    public Messages (){}
    public Messages(String coach, String player, String msg) {
        this.coach=coach;
        this.player=player;
        this.msg=msg;

    }

    public String getCoach_msg() {
        return coach;
    }

    public void setCoach_msg(String coach) {
        this.coach=coach;
    }

    public String getPLayer_msg() {
        return player;
    }

    public void setPlayer_msg(String player) {
        this.player=player;
    }

    public String getMsg() {return msg;}

    public void setMsg(String msg) {this.msg=msg;}
}
