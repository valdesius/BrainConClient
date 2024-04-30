package com.example.brainconclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brainconclient.helpers.StringResourceHelper;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextInputEditText txtProfFirstName, txtProfLastName, txtProfEmail;
    private Button profileLogoutBtn;
    ActionBar actionBar;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {
            // Handle the case where ActionBar is null
        }
        actionBar.setTitle("Profile");


        txtProfFirstName        = findViewById(R.id.txt_prof_first_name);
        txtProfLastName         = findViewById(R.id.txt_prof_last_name);
        txtProfEmail            = findViewById(R.id.txt_prof_email);
        profileLogoutBtn        = findViewById(R.id.profile_logout_btn);

        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserOut();
            }
        });

        setUserDetailsInEditTextFields();

    }

    public void setUserDetailsInEditTextFields(){

        prefs = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        txtProfFirstName.setText(prefs.getString("first_name", ""));
        txtProfLastName.setText(prefs.getString("last_name", ""));
        txtProfEmail.setText(prefs.getString("username", ""));
    }

    public void logUserOut(){
        clearPreferences();
        goToLogin();
        Toast.makeText(ProfileActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
    }


    public void clearPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }


    public void goToLogin(){
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
