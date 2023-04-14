package com.example.dog_test.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DogTest {

    Dog mDog = new Dog();

    @Test
    public void setId() {
        mDog.setId("dog1234");
        assertNotNull(mDog.getId());
    }

//    @Test
//    public void getId() {
//        assertNotNull(mDog.getId());
//    }

    @Test
    public void setName() {
        mDog.setName("Joe");
        assertNotNull(mDog.getName());
    }

//    @Test
//    public void getName() {
//        assertNotNull(mDog.getName());
//    }

    @Test
    public void setBreed() {
        mDog.setBreed("husky");
        assertNotNull(mDog.getBreed());
    }

//    @Test
//    public void getBreed() {
//        assertNotNull(mDog.getBreed());
//    }

    @Test
    public void setAge() {
        mDog.setAge(1);
        assertNotNull(mDog.getAge());
    }

//    @Test
//    public void getAge() {
//        assertNotNull(mDog.getAge());
//    }

    @Test
    public void setWeight() {
        mDog.setWeight(30);
        assertNotNull(mDog.getWeight());
    }

//    @Test
//    public void getWeight() {
//        assertNotNull(mDog.getWeight());
//    }

    @Test
    public void setIsVaccinated() {
        mDog.setIsVaccinated(false);
        assertFalse(mDog.getIsVaccinated());
    }


//    @Test
//    public void getIsVaccinated() {
//        assertFalse(mDog.getIsVaccinated());
//    }

    @Test
    public void setIsSterilized() {
        mDog.setIsSterilized(false);
        assertFalse(mDog.getIsSterilized());
    }

//    @Test
//    public void getIsSterilized() {
//        assertFalse(mDog.getIsSterilized());
//    }

    @Test
    public void setBio() {
        mDog.setBio("Hyper");
        assertEquals("Hyper", mDog.getBio());
    }


//    @Test
//    public void getBio() {
//        assertEquals("Hyper", mDog.getBio());
//    }


}