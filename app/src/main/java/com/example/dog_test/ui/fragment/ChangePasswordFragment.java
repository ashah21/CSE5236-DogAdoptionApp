package com.example.dog_test.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dog_test.R;
import com.example.dog_test.ui.activity.LoginActivity;
import com.example.dog_test.ui.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordFragment extends Fragment {

    FirebaseUser user;

    TextInputEditText currentPasswordInput, newPasswordInput, confirmNewPasswordInput;

    Button submitNewPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_passwords, container, false);

        currentPasswordInput = view.findViewById(R.id.current_password);
        newPasswordInput = view.findViewById(R.id.new_password);
        confirmNewPasswordInput = view.findViewById(R.id.confirm_password);
        submitNewPassword = view.findViewById(R.id.btn_submit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        submitNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = String.valueOf(currentPasswordInput.getText());
                String newPassword = String.valueOf(newPasswordInput.getText());
                String confirmNewPassword = String.valueOf(confirmNewPasswordInput.getText());

                if (TextUtils.isEmpty((currentPassword)) || TextUtils.isEmpty((newPassword))
                        || TextUtils.isEmpty((confirmNewPassword))) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill out all the fields above.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Passwords do NOT match.",
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
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Password updated.",
                                                        Toast.LENGTH_SHORT).show();

                                                //Updates users password in RealTime DB
                                                String userId = user.getUid();
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                                                reference.child(userId).child("password").setValue(newPassword);

                                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Error: Password not updated.",
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Password was incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });


        return view;
    }
}
