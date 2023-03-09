package com.example.dog_test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    FirebaseUser user;

    TextInputEditText currentPasswordInput, newPasswordInput, confirmNewPassowordInput;

    Button submitNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        currentPasswordInput = findViewById(R.id.current_password);
        newPasswordInput = findViewById(R.id.new_password);
        confirmNewPassowordInput = findViewById(R.id.confirm_password);
        submitNewPassword = findViewById(R.id.btn_submit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        submitNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = String.valueOf(currentPasswordInput.getText());
                String newPassword = String.valueOf(newPasswordInput.getText());
                String confirmNewPassword = String.valueOf(confirmNewPassowordInput.getText());

                if (TextUtils.isEmpty((currentPassword)) || TextUtils.isEmpty((newPassword))
                        || TextUtils.isEmpty((confirmNewPassword))) {
                    Toast.makeText(getApplicationContext(), "Please fill out all the fields above.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do NOT match.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), currentPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "Password updated.",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Error: Password not updated.",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Password was incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }
}
