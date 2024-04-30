package com.example.brainconclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.RedirectHelper;
import com.example.brainconclient.helpers.RegistrationFormValidatorHelper;
import com.example.brainconclient.utils.MyVolleySingletonUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private TextView txtGoToSignUp;
    private Button registerBtn;
    private TextInputEditText txtRegFirstName, txtRegLastName, txtRegEmail, txtRegPassword, txtRegConfirm;
    private TextInputLayout txtRegFirstNameLayout, txtRegLastNameLayout, txtRegEmailLayout, txtRegPassLayout, txtRegConfirmLayout;
    private RadioButton studRButton;
    private RadioButton mentRButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {
        }


        requestQueue = MyVolleySingletonUtil.getInstance(RegisterActivity.this).getRequestQueue();


        txtGoToSignUp = findViewById(R.id.txt_go_to_sign_in);
        registerBtn = findViewById(R.id.register_btn);


        txtRegFirstName = findViewById(R.id.txt_first_name);
        txtRegLastName = findViewById(R.id.txt_last_name);
        txtRegEmail = findViewById(R.id.txt_email);
        txtRegPassword = findViewById(R.id.txt_password);
        txtRegConfirm = findViewById(R.id.txt_confirm);

        txtRegFirstNameLayout = findViewById(R.id.txt_first_name_layout);
        txtRegLastNameLayout = findViewById(R.id.txt_last_name_layout);
        txtRegEmailLayout = findViewById(R.id.txt_email_layout);
        txtRegPassLayout = findViewById(R.id.txt_password_layout);
        txtRegConfirmLayout = findViewById(R.id.txt_confirm_layout);
        studRButton = findViewById(R.id.studentRadioButton);
        mentRButton = findViewById(R.id.mentorRadioButton);



        RegistrationFormValidatorHelper regFormValidator
                = new RegistrationFormValidatorHelper(txtRegFirstName,
                txtRegLastName, txtRegEmail, txtRegPassword, txtRegConfirm,
                txtRegFirstNameLayout, txtRegLastNameLayout, txtRegEmailLayout,
                txtRegPassLayout, txtRegConfirmLayout, registerBtn, studRButton, mentRButton);

        txtRegFirstName.addTextChangedListener(regFormValidator);
        txtRegLastName.addTextChangedListener(regFormValidator);
        txtRegEmail.addTextChangedListener(regFormValidator);
        txtRegPassword.addTextChangedListener(regFormValidator);
        txtRegConfirm.addTextChangedListener(regFormValidator);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }

        });


        txtGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectHelper.goToLogin(RegisterActivity.this);
                finish();
            }

        });

    }


    public void registerUser() {

        String first_name = txtRegFirstName.getText().toString();
        String last_name = txtRegLastName.getText().toString();
        String email = txtRegEmail.getText().toString();
        String password = txtRegPassword.getText().toString();

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("first_name", first_name);
            jsonRequest.put("last_name", last_name);
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
            if (studRButton.isChecked()) {
                jsonRequest.put("role", "STUDENT");
            } else {
                jsonRequest.put("role", "MENTOR");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiLinksHelper.registerUserApiUri(), jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RegisterActivity", response.toString());
                        Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        RedirectHelper.goToSuccessActivity(RegisterActivity.this);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i("RegisterActivity", "Failed to register user");
                        Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
    // END OF REGISTER USER METHOD.

}