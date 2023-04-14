package com.example.dog_test.ui.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dog_test.R;
import com.example.dog_test.ui.fragment.AddDogFragment;
import com.example.dog_test.ui.fragment.SettingsFragment;
import com.example.dog_test.ui.fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userType = "false";

    DrawerLayout slideMenuLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    TextView toolbarTitle;

    private HomeFragment homeFragment;

    public HomeFragment getHomeFragmentForTest() {
        recoverFragmentHome();
        return homeFragment;
    }

    private void recoverFragmentHome() {
        if (homeFragment == null) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getFragments().size() == 0) {
                homeFragment = new HomeFragment();
            }
            else {
                for (Fragment fragment: fm.getFragments()) {
                    if (fragment instanceof HomeFragment) {
                        homeFragment = (HomeFragment) fragment;
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Home");

        slideMenuLayout = findViewById(R.id.slide_menu);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, slideMenuLayout, R.string.open, R.string.close);
        slideMenuLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This makes sure that the app will open to the Home Fragment
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setDrawerMenu();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //This switch-case is based on the button the user clicks from the navigation menu
        switch(item.getItemId())
        {
            case R.id.home:
                toolbarTitle.setText("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.addDog:
                toolbarTitle.setText("Add New Dog");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddDogFragment()).commit();
                break;
            case R.id.settings:
                toolbarTitle.setText("Settings");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        slideMenuLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDrawerMenu()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                            navigationView.getMenu().findItem(R.id.addDog).setVisible(true);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "User does not exist",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Failed to read data",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}