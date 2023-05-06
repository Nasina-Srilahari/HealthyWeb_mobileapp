package com.example.lmc;

public class User {
    public String user_name;
    public String user_phoneno;
    public String user_password;

    public User(){

    }
    public User(String user_name, String user_phoneno, String user_password){
        this.user_name = user_name;
        this.user_phoneno = user_phoneno;
        this.user_password = user_password;
    }
}
