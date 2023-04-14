package com.example.dog_test.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dog_test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    String testMsg;

    private void isUser(){
        // get the email and password user enters
        String userEnteredEmail = editTextEmail.getText().toString();
        String userEnteredPassword = editTextPassword.getText().toString();

        // get reference to the users
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(userEnteredEmail);    // check if that email is in the db

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if there is a user than there is data in the DataSnapshot

                if(snapshot.exists()){

                    editTextEmail.setError(null);
                    editTextEmail.setEnabled(false);
                    // if data in snapshot, get password from the db
                    String passwordFromDb = snapshot.child(userEnteredEmail).child("password").getValue(String.class);

                    if(passwordFromDb.equals(userEnteredPassword)){

                        editTextEmail.setError(null);
                        editTextEmail.setEnabled(false);

                        // user entered correct password and login
                        String nameFromDb = snapshot.child(userEnteredEmail).child("name").getValue(String.class);
                        String userIdFromDb = snapshot.child(userEnteredEmail).child("userId").getValue(String.class);

                    }else{
                        editTextPassword.setError("Incorrect Password");
                        editTextPassword.requestFocus();
                    }
                }else{
                    editTextEmail.setError("Email doesn't exist");
                    editTextEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // error methods
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    public void loginUser(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    testMsg = "Invalid login";
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    testMsg = "Invalid login";
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    testMsg = "Valid Login";

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    String userEnteredEmail = editTextEmail.getText().toString();
                                    String userEnteredPassword = editTextPassword.getText().toString();

                                    // get reference to the users
//                                    Query checkUser = ref.child("users").orderByChild("email").equalTo(userEnteredEmail);

                                    //checkUser.addListenerFor...
                                    ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                Log.d(TAG, "USER EXISTS");
//                                                String passwordFromDb = snapshot.child(userEnteredEmail).child("password").getValue().toString();
//                                                if(passwordFromDb.equals(userEnteredPassword)){
//                                                    Log.d(TAG, "CORRECT PASSWORD");
//                                                }else{
//                                                    Log.d(TAG, "INCORRECT PASSWORD");
//                                                }
                                            }else{
                                                Log.d(TAG, "USER DOES NOT EXIST");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d(TAG, "error message");
                                        }
                                    });

//                                    isUser();   // check if they are a user

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}