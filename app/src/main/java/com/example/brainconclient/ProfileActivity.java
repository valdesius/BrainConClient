package com.example.brainconclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.RedirectHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.utils.MyVolleySingletonUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private RequestQueue requestQueue;
    private TextInputEditText txtProfFirstName, txtProfLastName, txtProfEmail, txtProfPass;
    private Button profileUpdateBtn, profileLogoutBtn;
    ActionBar actionBar;

    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // ACTION BAR SETUP:
        actionBar = getSupportActionBar();
        actionBar.setTitle("Изменение данных");

        requestQueue = MyVolleySingletonUtil.getInstance(ProfileActivity.this).getRequestQueue();
        // HOOK / INITIATE VIEW ELEMENTS:
        txtProfFirstName = findViewById(R.id.txt_prof_first_name);
        txtProfLastName = findViewById(R.id.txt_prof_last_name);
        txtProfEmail = findViewById(R.id.txt_prof_email);
        txtProfPass = findViewById(R.id.txt_prof_password);

        profileUpdateBtn = findViewById(R.id.profile_update_btn);
        profileLogoutBtn = findViewById(R.id.profile_logout_btn);

        profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserOut();
            }
        });
        // END OF PROFILE LOGOUT ON CLICK LISTENER OBJECT.

        setUserDetailsInEditTextFields();

    }
    public void updateProfile() {
        // GET DATA:
        String first_name = txtProfFirstName.getText().toString();
        String last_name = txtProfLastName.getText().toString();
        String email = txtProfEmail.getText().toString();
        String password = txtProfPass.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, ApiLinksHelper.updateUserApiUri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ProfileActivity", response.toString());
                Toast.makeText(ProfileActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                // Update local shared preferences


                prefs.edit().putString("first_name", first_name).apply();
                prefs.edit().putString("last_name", last_name).apply();
                prefs.edit().putString("email", email).apply();
                prefs.edit().putString("password", password).apply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("ProfileActivity", "Failed to update profile");
                Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        // ADD / SEND REQUEST:
        requestQueue.add(stringRequest);
    }

    // END OF ON CREATE METHOD.

    public void setUserDetailsInEditTextFields() {
        // GET STORED PREFERENCES:
        prefs = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);
        // SET TEXT INPUT FIELDS VALUES:
        txtProfFirstName.setText(prefs.getString("first_name", ""));
        txtProfLastName.setText(prefs.getString("last_name", ""));
        txtProfEmail.setText(prefs.getString("username", ""));
        txtProfPass.setText(prefs.getString("password", ""));
    }
    // END OF SET USER DETAILS IN EDIT TEXT FIELDS.

    public void logUserOut() {
        clearPreferences();
        goToLogin();
        Toast.makeText(ProfileActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
    }
    // END OF LOG USER OUT METHOD.

    public void clearPreferences() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
    // END OF LOGOUT OR CLEAR PREFERENCES.

    public void goToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    // END OF GO TO LOGIN ACTIVITY.
}
// END OF PROFILE ACTIVITY CLASS.