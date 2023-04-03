package com.example.dog_test.ui.fragment;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dog_test.R;
import com.example.dog_test.ui.activity.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UserProfileFragment extends Fragment{

    private ImageView profilePic;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Button uploadImage;
    Button camera;
    FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        profilePic = view.findViewById(R.id.user_profile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(user == null){
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);

//                choosePicture();
            }
        });

        return view;
    }

//    private void choosePicture() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    protected void onActivityForResult(int requestCode, int resultCode, @Nullable Intent data){
//        if(resultCode == 1 && resultCode == RESULT_OK && data != null){
//            imageUri = data.getData();
//            profilePic.setImageURI(imageUri);
//            uploadPicture();
//        }
//    }
//
//    private void uploadPicture() {
//         final String randomKey = UUID.randomUUID().toString();
//         StorageReference riverRef = storageReference.child("images/" + randomKey);
//
//         riverRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//             @Override
//             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                 Snackbar.make(findViewById(R.id.user_profile))
//             }
//         });
//    }


}