package com.example.dog_test.ui.fragment;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dog_test.R;
import com.example.dog_test.databinding.FragmentUserProfileBinding;
import com.example.dog_test.ui.activity.MainActivity;
import com.example.dog_test.ui.activity.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UserProfileFragment extends Fragment{

    private ImageView profilePic;
    private Uri imagePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Button btnUploadImage;
    Button btnCamera;
    FirebaseUser user;
    ProgressBar progressBar;

    // for using camera to take a pic
    FragmentUserProfileBinding profileBinding;
    ActivityResultLauncher<Uri> takePictureLauncher;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;
    private static final int CAMERA_PERMISSION_CODE = 1;

    private static final int REQUEST_PHOTO= 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        btnUploadImage = (Button) view.findViewById(R.id.btn_upload_image);
//        uploadImage.setOnClickListener(this);

        btnCamera = (Button) view.findViewById(R.id.btn_camera);
//        camera.setOnClickListener(this);

//        user = FirebaseAuth.getInstance().getCurrentUser();

        profilePic = view.findViewById(R.id.user_profile);
        progressBar = view.findViewById(R.id.progressBar);

//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();

        // camera
//        profileBinding = FragmentUserProfileBinding.inflate(getLayoutInflater());
        //imageUri = createUri();
        //registerPictureLauncher();

        ActivityResultLauncher takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {

            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

                }
                else {
                    // Permission already granted
                    takePictureLauncher.launch(null);
                }


//                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(open_camera, REQUEST_PHOTO);
            }
        });


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                cameraIntent.setType("image/*");
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 1000);
                }
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        return view;
    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }


    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image upload success, get the download URL and save it to Firebase Realtime Database or Firestore
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Save the download URL to Firebase Realtime Database or Firestore
                        // ...
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Image upload failed, handle the error
                // ...
            }
        });
    }

        private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                            returnUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                profilePic.setImageBitmap(bitmapImage);
            }else if(requestCode == REQUEST_PHOTO){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // chatgpt test
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            uploadImageToFirebase(imageUri);
        }

        imagePath = data.getData();
        Glide.with(this)
                .load(imagePath)
                .override(800, 800)
                .centerCrop()
                .into(profilePic);
    }



    private void uploadImage(){

        progressBar.setVisibility(View.VISIBLE);
        FirebaseStorage.getInstance().getReference("image/" + UUID.randomUUID().toString()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                updateProfilePicture(task.getResult().toString());
                            }
                        }
                    });
                    Toast.makeText( getActivity().getApplicationContext(), "Image Uploaded!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText( getActivity().getApplicationContext(), "Image not uploaded", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
            }
        });

    }

    private void updateProfilePicture(String url){
        FirebaseDatabase.getInstance().getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "/userImage").setValue(url);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePictureLauncher.launch(imageUri);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Camera Permission denied, please allow permission to take picture", Toast.LENGTH_LONG).show();
            }
        }
    }





}