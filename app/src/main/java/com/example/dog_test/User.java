package com.example.dog_test;

public class User {

    public String name;
    public String email;
    public String password;

    public String userId;

    public User(){

    }

    public User(String name, String email, String password, String userId){
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }


    public String getName() {
        return name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
