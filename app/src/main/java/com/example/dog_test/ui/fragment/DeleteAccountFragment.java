package com.example.dog_test.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAccountFragment extends Fragment {

    TextInputEditText currentPasswordInput;

    Button deleteAccountButton;

    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delete_account, container, false);

        currentPasswordInput = view.findViewById(R.id.current_password);
        deleteAccountButton = view.findViewById(R.id.btn_submit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentPassword = String.valueOf(currentPasswordInput.getText());

                if (TextUtils.isEmpty((currentPassword))) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill out the field above.",
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
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Deleted User Successfully.",
                                                        Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                                                getActivity().finish();
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Error: Account not deleted.",
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
