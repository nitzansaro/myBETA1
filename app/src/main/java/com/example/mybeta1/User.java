package com.example.mybeta1;

class User {
    private String name, email, phone, uid, id,coach2,teamName,teamNum;
    private Boolean coach=false;


    public User() {
    }

    public User(String name, String email, String phone, String uid, String id, Boolean coach) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
        this.coach=coach;
        this.id=id;


    }

    public Boolean getCoach() {
        return coach;
    }

    public void setCoach(Boolean coach2) {
        this.coach = coach;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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