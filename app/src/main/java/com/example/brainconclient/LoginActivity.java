package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brainconclient.helper.validation.LoginValidationHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private LoginValidationHelper loginValidator;
    private TextView txtGoToSignIn;

    private TextInputEditText txt_email, txt_password;
    private TextInputLayout txtEmailLayout, txtPasswordLayout;

    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // REMOVE ACTION BAR:

        // INITIALIZE REQUEST QUEUE:


        // INITIATE / HOOK VIEW COMPONENTS:
        txt_email        = findViewById(R.id.txt_email);
        txt_password     = findViewById(R.id.txt_password);
        txtGoToSignIn   = findViewById(R.id.txt_go_to_sign_up);
        loginBtn        = findViewById(R.id.login_btn);
        // GET LAYOUTS:
        txtEmailLayout      = findViewById(R.id.txt_email_layout);
        txtPasswordLayout   = findViewById(R.id.txt_password_layout);

        loginValidator = new LoginValidationHelper(txt_email, txt_password, txtEmailLayout, txtPasswordLayout, loginBtn);

        txt_email.addTextChangedListener(loginValidator);
        txt_password.addTextChangedListener(loginValidator);


    }
    // END OF ON CREATE LOGIN ACTIVITY METHOD.
    public void goToMainIfAuthenticated(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        // DISPLAY SUCCESS MESSAGE IF AUTHENTICATED:
        Toast.makeText(LoginActivity.this, "Вход подтвержден", Toast.LENGTH_LONG).show();
        finish();
    }


}