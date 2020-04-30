package com.example.EZteam;

class User {
    private String name, phone, uid, id,email,dayofbirth;
    private Boolean coach=false;


    public User() {
    }

    public User(String name, String phone, String uid, String id, Boolean coach,String email,String dayofbirth) {
        this.name = name;
        this.phone = phone;
        this.uid = uid;
        this.coach=coach;
        this.id=id;
        this.email=email;
        this.dayofbirth=dayofbirth;



    }

    public Boolean getCoach() {
        return coach;
    }

    public void setCoach(Boolean coach2) {
        this.coach = coach;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDayofbirth() {
        return dayofbirth;
    }

    public void setDayofbirth(String dayofbirth) {
        this.dayofbirth = dayofbirth;
    }


    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getid() {
        return id;
    }



    public void setName(String name) {
        this.name = name;
    }




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}