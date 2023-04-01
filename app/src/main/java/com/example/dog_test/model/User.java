package com.example.dog_test.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String name;
    public String email;
    public String password;
    public boolean isShelter;

    public String userId;

    public List<String> dogIds;

    public User(String name, String email, String password, boolean isShelter, String userId){
        this.name = name;
        this.email = email;
        this.password = password;
        this.isShelter = isShelter;
        this.userId = userId;
        this.dogIds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDog(String dogId) {
        this.dogIds.add(dogId);
    }

    public void removeDog(String dogId) {
        this.dogIds.remove(String.valueOf(dogId));
    }
}
