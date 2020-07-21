package com.steppers.samp;

public class User {
    String regno;
    String address;
    String mobile;
    String Displayname;
    String Displaylname;
    String Password;

    String Email;
    long createdAt;

    public User (){};
    public User(String displayname,String displaylastname,String email,String pwd,String mobile,String address,String regno,long createdAt){
        this.Displayname=displayname;
        this.Displaylname=displaylastname;
        this.mobile=mobile;
        this.address=address;
        this.regno=regno;
        this.Email=email;
        this.Password=pwd;
        this.createdAt=createdAt;
    }

    public String getUserAddress() {
        return address;
    }

    public String getUserReg() {
        return regno;
    }

    public String getUserMobile() {
        return mobile;
    }

    public String getDisplayLastName() {
        return Displaylname;
    }

    public String getDisplayName() {
        return Displayname;
    }

    public String getUserEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public long getCreatedAt() {
        return createdAt;
    }

}
