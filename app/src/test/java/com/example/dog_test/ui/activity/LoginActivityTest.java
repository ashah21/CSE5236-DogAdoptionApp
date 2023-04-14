package com.example.dog_test.ui.activity;

import static org.junit.Assert.*;

import android.view.View;

import com.google.errorprone.annotations.DoNotMock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;


public class LoginActivityTest{

    private LoginActivity mLoginActivity;

    @Before
    public void setUp() throws Exception {
        mLoginActivity = new LoginActivity();
    }



    @Test
    public void testLoginWithCorrect() {

        LoginActivity mTest = new LoginActivity();

        String username = "";

        mTest.editTextEmail.setText("suzy@gmail.com");
        mTest.editTextPassword.setText("password");
        mTest.buttonLogin.performClick();
        mTest.mAuth.signInWithEmailAndPassword("suzy@gmail.com", "password");

        assertEquals("Valid login", mTest.testMsg);


    }

    @Test
    public void testLogin(){
//        mLoginActivity = new LoginActivity();
        mLoginActivity.onStart();
        mLoginActivity.editTextEmail.setText("suzy@gmail.com");
        mLoginActivity.editTextPassword.setText("password");
        mLoginActivity.buttonLogin.performClick();
        if(mLoginActivity != null){
            View loginActivityView = mLoginActivity.getCurrentFocus();
        }

        assertNotNull(mLoginActivity);
    }



}