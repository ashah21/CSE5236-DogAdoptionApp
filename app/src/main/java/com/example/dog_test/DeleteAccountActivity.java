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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DeleteAccountActivity extends AppCompatActivity {

    TextInputEditText currentPasswordInput;

    Button deleteAccountButton;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        currentPasswordInput = findViewById(R.id.current_password);
        deleteAccountButton = findViewById(R.id.btn_submit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentPassword = String.valueOf(currentPasswordInput.getText());

                if (TextUtils.isEmpty((currentPassword))) {
                    Toast.makeText(getApplicationContext(), "Please fill out the field above.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), currentPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // these lines delete the user from the real-time db
                            String userId = user.getUid();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                            reference.child(userId).removeValue();

                            user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Deleted User Successfully.",
                                                    Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Error: Account not deleted.",
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