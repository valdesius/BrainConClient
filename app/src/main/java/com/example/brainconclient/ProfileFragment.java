package com.example.brainconclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private TextView profileLogoutBtn;
    private SharedPreferences prefs;
    private boolean isLoggedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        prefs = getActivity().getSharedPreferences("YourPrefsName", Context.MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        profileLogoutBtn = view.findViewById(R.id.txt_logout);
        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserOut();
            }
        });

        return view;
    }

    public void logUserOut() {
        clearPreferences();
        goToLogin();
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false); // Update the login status
        editor.apply();
    }

    public void goToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}