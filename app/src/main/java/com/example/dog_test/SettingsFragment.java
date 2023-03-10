package com.example.dog_test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SettingsActivity";

    Button changePassword;

    Button deleteAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView() called");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        changePassword = (Button) view.findViewById(R.id.btn_change_password);
        changePassword.setOnClickListener(this);

        deleteAccount = (Button) view.findViewById(R.id.btn_delete_account);
        deleteAccount.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                Intent myIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                getActivity().startActivity(myIntent);
                break;
            case R.id.btn_delete_account:
                Intent myIntent2 = new Intent(getActivity(), DeleteAccountActivity.class);
                getActivity().startActivity(myIntent2);
                break;
        }
    }
}
