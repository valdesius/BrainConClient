package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brainconclient.helpers.StringResourceHelper;

public class SuccessTestActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private Button returnHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_test);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {

        }


        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);




        returnHomeBtn = findViewById(R.id.success_return_course_btn);

        returnHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            returnToHome();
            }
        });
    }

    public void returnToHome() {
        Intent returnHomeIntent = new Intent(SuccessTestActivity.this, MainActivity.class);
        startActivity(returnHomeIntent);
        finish();
    }


    public void goToLoginIfNotAuthenticated() {
        Intent intent = new Intent(SuccessTestActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}