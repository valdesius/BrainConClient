package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brainconclient.helpers.StringResourceHelper;

public class SuccessActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private boolean authenticated;
    private Button returnHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {

        }


        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);


        authenticated = preferences.getBoolean("authenticated", false);

        returnHomeBtn = findViewById(R.id.success_return_home_btn);

        returnHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authenticated) {
                    returnToHome();
                } else {
                    goToLoginIfNotAuthenticated();
                }

            }
        });
    }

    public void returnToHome() {
        Intent returnHomeIntent = new Intent(SuccessActivity.this, MainActivity.class);
        startActivity(returnHomeIntent);
        finish();
    }


    public void goToLoginIfNotAuthenticated() {
        Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}