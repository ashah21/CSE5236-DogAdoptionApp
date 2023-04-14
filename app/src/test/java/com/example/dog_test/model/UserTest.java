package com.example.dog_test.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

    User mUser = new User("Franz", "franz6@lleshaj.com", "password", false,"2FowhU05snQMc0CCFfpOJv4nQ7V2","https://firebasestorage.googleapis.com/v0/b/dog-adoption-app-9cc4a.appspot.com/o/image%2Fc37c52d6-db4f-4a7a-a220-c39be0bdcc79?alt=media&token=aa2ae10c-c52b-4d03-8445-c97bdc7a569d" );
    User nullUser = new User(null, "null@gmail.com", "password", false, "jsd9o2093", "");

//    @Test
//    public void newUser(){
//        mUser = new User("Franz", "franz6@lleshaj.com", "password", false,"2FowhU05snQMc0CCFfpOJv4nQ7V2","https://firebasestorage.googleapis.com/v0/b/dog-adoption-app-9cc4a.appspot.com/o/image%2Fc37c52d6-db4f-4a7a-a220-c39be0bdcc79?alt=media&token=aa2ae10c-c52b-4d03-8445-c97bdc7a569d" );
//        assertNotNull(mUser.name);
//    }

    @Test
    public void getNameTest() {
        assertEquals("Franz", mUser.getName());
    }

    @Test
    // checking if all the user values are not null
    public void userValues(){
        assertNotNull(mUser.name);
        assertNotNull(mUser.userId);
        assertFalse(mUser.isShelter);
        assertNotNull(mUser.password);
    }

    @Test
    // setting a user name that's null
    public void setNameTest(){
        nullUser.setName("Bob");
        assertNotNull(nullUser.name);
    }

    @Test
    public void addNewDog(){
        mUser.addDog("NRu81UPdasM3vqe-Lyj");
        assertNotNull(mUser.dogIds);
    }

}