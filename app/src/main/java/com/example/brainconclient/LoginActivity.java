package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.LoginFormValidationHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.utils.MyVolleySingletonUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    //private static final String USER_DETAIL_PREF = "USER_INFO";
    private SharedPreferences preferences;
    private RequestQueue mRequestQueue;

    private LoginFormValidationHelper loginValidator;
    private TextView txtGoToSignIn;

    private TextInputEditText txt_email, txt_password;
    private TextInputLayout txtEmailLayout, txtPasswordLayout;

    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {

        }


        mRequestQueue = MyVolleySingletonUtil.getInstance(LoginActivity.this).getRequestQueue();


        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txtGoToSignIn = findViewById(R.id.txt_go_to_sign_up);
        loginBtn = findViewById(R.id.login_btn);

        txtEmailLayout = findViewById(R.id.txt_email_layout);
        txtPasswordLayout = findViewById(R.id.txt_password_layout);


        loginValidator = new LoginFormValidationHelper(txt_email, txt_password, txtEmailLayout, txtPasswordLayout, loginBtn);


        txt_email.addTextChangedListener(loginValidator);
        txt_password.addTextChangedListener(loginValidator);


        processLogin();

        redirectToRegister();

    }


    public void processLogin() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser(txt_email.getText().toString(), txt_password.getText().toString());
                // Toast.makeText(LoginActivity.this, "Login Button Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void redirectToRegister() {
        txtGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }

        });

    }

    public void authenticateUser(String email, String password) {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        JsonObjectRequest request
                = new JsonObjectRequest(Request.Method.POST, ApiLinksHelper.authUserApiUri(), new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                try {
                    if (response.has("userId")) {
                        editor.putInt("user_id", response.getInt("userId"));
                    }
                    if (response.has("firstName")) {
                        editor.putString("first_name", response.getString("firstName"));
                    }
                    if (response.has("lastName")) {
                        editor.putString("last_name", response.getString("lastName"));
                    }
                    if (response.has("username")) {
                        editor.putString("username", response.getString("username"));
                    }
                    if (response.has("token")) {
                        editor.putString("token", response.getString("token"));
                    }
                    editor.putBoolean("authenticated", true);
                    editor.apply();


                    goToMainIfAuthenticated();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Error in try block: " + e.getMessage());
                    Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    throw new RuntimeException(e);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error msg 1: " + error.getMessage());
                System.out.println("Auth Error: " + error.networkResponse);
                System.out.println("Auth Error Two: " + error.getLocalizedMessage());

                Toast.makeText(LoginActivity.this, "Failed To Log-In", Toast.LENGTH_LONG).show();
            }
        });


        mRequestQueue.add(request);
    }


    public void goToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    public void goToMainIfAuthenticated() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
        finish();
    }

}