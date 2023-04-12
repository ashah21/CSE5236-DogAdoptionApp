package com.example.dog_test.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dog_test.R;
import com.example.dog_test.model.Dog;
import com.example.dog_test.ui.activity.LoginActivity;
import com.example.dog_test.ui.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ViewGroup placeholder;
    LayoutInflater inflater;
    ViewGroup container;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;

    DatabaseReference dogRef;
    String userType;
    List dogList;
    Button yesButton;
    Button noButton;
    Dog cardDog;
    boolean reset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        placeholder = (ViewGroup) view;
        setUserView();

        dogList = new ArrayList();
        reset = false;
        getDogInfo();

        return placeholder;
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

                            setCardInfo();
                            buttonController();
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

    public void buttonController()
    {
        yesButton = (Button) getActivity().findViewById(R.id.yes);
        noButton = (Button) getActivity().findViewById(R.id.no);


        yesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(dogList.contains(cardDog))
                {
                    deleteDogFromFirebase();
                    dogList.remove(cardDog);
                }

                if(dogList.size() > 0)
                {
                    setCardInfo();
                }
                else
                {
                    getDogInfo();
                    reset = true;
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(dogList.contains(cardDog))
                {
                    dogList.remove(cardDog);
                }

                if(dogList.size() > 0)
                {
                    setCardInfo();
                }
                else
                {
                    getDogInfo();
                    reset = true;
                }

            }
        });
    }

    public void getDogInfo() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dogRef = firebaseDatabase.getReference("dogs");

        int randomDogAge = (int) (Math.random() * 2) + 1;

        dogRef.orderByChild("age").startAt(randomDogAge).limitToFirst(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                addNewDog(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addNewDog(DataSnapshot snapshot) {
        Dog newDog = new Dog();


        newDog.setId(snapshot.getKey());
        newDog.setName(String.valueOf(snapshot.child("name").getValue()));
        newDog.setAge(Integer.parseInt(String.valueOf(snapshot.child("age").getValue())));
        newDog.setBreed(String.valueOf(snapshot.child("breed").getValue()));
        newDog.setWeight(Integer.parseInt(String.valueOf(snapshot.child("weight").getValue())));
        newDog.setImageUrl(String.valueOf(snapshot.child("imageUrl").getValue()));
        newDog.setIsAdopted(Boolean.parseBoolean(String.valueOf(snapshot.child("isAdopted").getValue())));
        newDog.setIsSterilized(Boolean.parseBoolean(String.valueOf(snapshot.child("isSterilized").getValue())));
        newDog.setIsVaccinated(Boolean.parseBoolean(String.valueOf(snapshot.child("isVaccinated").getValue())));
        newDog.setBio(String.valueOf(snapshot.child("bio").getValue()));

        dogList.add(newDog);

        if(reset) {
            setCardInfo();
            reset = false;
        }
    }

    public void setCardInfo(){
        TextView dogName = (TextView) getActivity().findViewById(R.id.dogName);
        TextView dogBio = (TextView) getActivity().findViewById(R.id.bio);
        TextView dogBreed = (TextView) getActivity().findViewById(R.id.breed);
        TextView dogAge = (TextView) getActivity().findViewById(R.id.age);
        TextView dogWeight = (TextView) getActivity().findViewById(R.id.weight);
        TextView sterelizedStatus = (TextView) getActivity().findViewById(R.id.sterilized);
        TextView vaccinationStatus = (TextView) getActivity().findViewById(R.id.vaccinated);
        ImageView dogImage = (ImageView) getActivity().findViewById(R.id.dogImage);

        int randomDogIndex = (int) (Math.random() * dogList.size());
        cardDog = (Dog) dogList.get(randomDogIndex);

        Uri imageUri = Uri.parse(cardDog.getImageUrl());
        Glide.with(this)
                .load(imageUri)
                .override(800,800)
                .centerCrop()
                .into(dogImage);

        dogName.setText(cardDog.getName());
        dogBio.setText("Bio: " + cardDog.getBio());
        dogBreed.setText("Breed: " + cardDog.getBreed());
        dogAge.setText("Age: " + cardDog.getAge());
        dogWeight.setText("Weight: " + cardDog.getWeight());

        if(cardDog.isSterilized)
        {
            sterelizedStatus.setText("Is Sterilized: Yes");
        }
        else
        {
            sterelizedStatus.setText("Is Sterilized: No");
        }

        if(cardDog.isVaccinated)
        {
            vaccinationStatus.setText("Is Vaccinated: Yes");
        }
        else
        {
            vaccinationStatus.setText("Is Vaccinated: No");
        }
    }

    public void deleteDogFromFirebase()
    {
        // these lines delete the dog from the real-time db
        dogRef = FirebaseDatabase.getInstance().getReference("dogs");
        dogRef.child(cardDog.dogId).removeValue();

        dogRef.child(cardDog.dogId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Deleted Dog Successfully.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: Dog not deleted.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }
}
