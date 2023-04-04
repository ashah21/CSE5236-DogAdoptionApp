package com.example.dog_test.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dog_test.R;
import com.example.dog_test.model.Dog;
import com.example.dog_test.ui.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    ViewGroup placeholder;
    LayoutInflater inflater;
    ViewGroup container;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    
    DatabaseReference dogRef;
    String userType;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        placeholder = (ViewGroup) view;
        setUserView();
        getDogInfo();

        return placeholder;
    }


    public void getDogInfo() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dogRef = firebaseDatabase.getReference("dogs");
        System.out.println("dkaakakakdk");
        dogRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Query dogs = dogRef.orderByKey();
                dogs.startAt(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    public void setUserView(){
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users");
        userRef.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        DataSnapshot userSnapshot = task.getResult();
                        userType = String.valueOf(userSnapshot.child("isShelter").getValue());

                        if(userType.matches("true"))
                        {
                            View shelter = inflater.inflate(R.layout.fragment_home_shelter, container, false);
                            placeholder.removeAllViews();
                            placeholder.addView(shelter);
                        }
                        else
                        {
                            View adopter = inflater.inflate(R.layout.fragment_home_adopter, container, false);
                            placeholder.removeAllViews();
                            placeholder.addView(adopter);
                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "User does not exist",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Failed to read data",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
