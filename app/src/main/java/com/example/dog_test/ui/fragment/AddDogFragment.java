package com.example.dog_test.ui.fragment;

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
import com.example.dog_test.model.Dog;
import com.example.dog_test.ui.activity.RegisterActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddDogFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextInputEditText dogNameInput;

    Button addNewDog;
    Dog dog;
    String dogID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_dog, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("dogs");

        dogNameInput = view.findViewById(R.id.dog_name);
        addNewDog = view.findViewById(R.id.btn_submit);

        dog = new Dog();

        addNewDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dogName = String.valueOf(dogNameInput.getText());
                dogID = databaseReference.push().getKey();

                if(TextUtils.isEmpty((dogName))){
                    Toast.makeText(getActivity().getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    addDatatoFirebase(dogName);
                }


            }
        });

        return view;
    }

    private void addDatatoFirebase(String name) {

        DatabaseReference dogRef = firebaseDatabase.getReference().child("dogs");
        dog.setName(name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dogRef.child(dogID).setValue(dog);
                Toast.makeText(getActivity().getApplicationContext(), "New dog added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Fail to add new dog" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
