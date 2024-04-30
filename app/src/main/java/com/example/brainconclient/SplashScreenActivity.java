package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brainconclient.helpers.StringResourceHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    //private static final String USER_DETAIL_PREF = "USER_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(preferences.getBoolean("authenticated", false)) {
                    goToMainIfAuthenticated();
                }else {
                    goToLoginIfNotAuthenticated();
                }

            }

        }, 3000);

    }

    public void goToMainIfAuthenticated(){
        Intent goToMainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(goToMainIntent);
        finish();
    }


    public void goToLoginIfNotAuthenticated(){
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
