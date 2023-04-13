package com.example.dog_test.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dog_test.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Button changePassword;

    Button deleteAccount;

    Button userProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        changePassword = (Button) view.findViewById(R.id.btn_change_password);
        changePassword.setOnClickListener(this);

        deleteAccount = (Button) view.findViewById(R.id.btn_delete_account);
        deleteAccount.setOnClickListener(this);

        userProfile = (Button) view.findViewById(R.id.btn_user_profile);
        userProfile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChangePasswordFragment()).commit();
                break;
            case R.id.btn_delete_account:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DeleteAccountFragment()).commit();
                break;
            case R.id.btn_user_profile:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserProfileFragment()).commit();
                break;
        }
    }
}
