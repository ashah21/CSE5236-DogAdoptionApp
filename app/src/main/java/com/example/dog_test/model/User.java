package com.example.dog_test.model;

public class User {

    public String name;
    public String email;
    public String password;
    public boolean isShelter;

    public String userId;

    public User(String name, String email, String password, boolean isShelter, String userId){
        this.name = name;
        this.email = email;
        this.password = password;
        this.isShelter = isShelter;
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
