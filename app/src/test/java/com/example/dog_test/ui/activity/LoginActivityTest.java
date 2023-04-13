package com.example.dog_test.ui.activity;

import static org.junit.Assert.*;

import android.app.Activity;

import com.google.errorprone.annotations.DoNotMock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void testLoginWithCorrect() {

        LoginActivity mTest = new LoginActivity();

        String username = "";

        mTest.editTextEmail.setText("suzy@gmail.com");
        mTest.editTextPassword.setText("password");
        mTest.buttonLogin.performClick();

        assertEquals("Valid login", mTest.testMsg);


    }


}