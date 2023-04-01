package com.example.dog_test.ui.fragment;

import static android.app.Activity.RESULT_OK;

import com.bumptech.glide.Glide;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.dog_test.R;
import com.example.dog_test.model.Dog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddDogFragment extends Fragment {

    Uri imageUri;

    FirebaseDatabase firebaseDatabase;

    StorageReference storageReference;

    DatabaseReference dogRef;

    TextInputEditText dogNameInput;

    TextInputEditText dogBreedInput;

    TextInputEditText dogAgeInput;

    TextInputEditText dogWeightInput;

    RadioGroup dogVaxInput;

    RadioGroup dogSterilizatonInput;

    TextInputEditText dogBioInput;

    Button addImage;

    Button addNewDog;

    ImageView imageView;
    Dog dog;
    String dogID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_dog, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        dogRef = firebaseDatabase.getReference("dogs");
        storageReference = FirebaseStorage.getInstance().getReference();
        imageView = view.findViewById(R.id.dog_image);
        dogNameInput = view.findViewById(R.id.dog_name);
        dogBreedInput = view.findViewById(R.id.dog_breed);
        dogAgeInput = view.findViewById(R.id.dog_age);
        dogWeightInput = view.findViewById(R.id.dog_weight);
        dogVaxInput = (RadioGroup)view.findViewById(R.id.vaccination_status);
        dogSterilizatonInput = (RadioGroup)view.findViewById(R.id.sterilization_status);
        dogBioInput = view.findViewById(R.id.dog_bio);
        addImage = view.findViewById(R.id.btn_add_picture);
        addNewDog = view.findViewById(R.id.btn_submit_dog);


        addNewDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dogName = String.valueOf(dogNameInput.getText());
                String dogBreed = String.valueOf(dogBreedInput.getText());
                int dogAge = Integer.valueOf(String.valueOf(dogAgeInput.getText()));
                int dogWeight = Integer.valueOf(String.valueOf(dogWeightInput.getText()));

                boolean dogVax = true;
                int selectedId = dogVaxInput.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)dogVaxInput.findViewById(selectedId);
                if (radioButton.getText().equals("No")) {
                    dogVax = false;
                }

                boolean dogSterilized = true;
                int selectedId2 = dogSterilizatonInput.getCheckedRadioButtonId();
                radioButton = (RadioButton)dogSterilizatonInput.findViewById(selectedId2);
                if (radioButton.getText().equals("No")) {
                    dogSterilized = false;
                }

                String dogBio = String.valueOf(dogBioInput.getText());

                dogID = dogRef.push().getKey();
                dog = new Dog();

                if(TextUtils.isEmpty((dogName)) || TextUtils.isEmpty(dogBreed) || TextUtils.isEmpty(dogBio)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addDatatoFirebase(dogName, dogBreed, dogAge, dogWeight, dogVax, dogSterilized, dogBio);
                }
            }
        });

        dogVaxInput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                    }
                });

        dogVaxInput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton= (RadioButton)group.findViewById(checkedId);
                    }
                });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                cameraIntent.setType("image/*");
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 1000);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                            returnUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                imageView.setImageBitmap(bitmapImage);
            }
        }

        imageUri = data.getData();
        Glide.with(this)
                .load(imageUri)
                .override(800, 800)
                .centerCrop()
                .into(imageView);
    }

    private void addDatatoFirebase(String name, String dogBreed, int dogAge, int dogWeight, boolean dogVax,
                                   boolean dogSterilized, String dogBio) {
        dog.setName(name);
        dog.setBreed(dogBreed);
        dog.setAge(dogAge);
        dog.setWeight(dogWeight);
        dog.setIsVaccinated(dogVax);
        dog.setIsSterilized(dogSterilized);
        dog.setBio(dogBio);
        dog.setIsAdopted(false);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dog.setImageUrl(uri.toString());
                            dogRef.child(dogID).setValue(dog);
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "New dog added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Fail to add new dog" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

