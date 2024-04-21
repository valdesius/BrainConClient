package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brainconclient.helper.StringResourceHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // GET PREFERENCES:
        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);


        // HIDE ACTION BAR IF IT EXISTS:
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // HANDLER METHOD TO REDIRECT TO LOGIN OR MAIN:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // CHECK IF USER IS AUTHENTICATED:
                if (preferences.getBoolean("authenticated", false)) {
                    goToMainIfAuthenticated();
                } else {
                    goToLoginIfNotAuthenticated();
                }
            }
        }, 5000);
    }

    public void goToMainIfAuthenticated() {
        Intent goToMainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(goToMainIntent);
        finish();
    }

    public void goToLoginIfNotAuthenticated() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}