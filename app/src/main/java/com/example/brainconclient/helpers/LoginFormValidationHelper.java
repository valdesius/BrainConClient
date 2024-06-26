package com.example.brainconclient.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFormValidationHelper implements TextWatcher {

    private TextInputEditText emailTextField, passwordTextField;
    private TextInputLayout emailFieldLayout, passwordFieldLayout;
    private Button loginBtn;
    private String emailField, passwordField;

    public LoginFormValidationHelper(TextInputEditText emailTextField,
                                     TextInputEditText passwordTextField,
                                     TextInputLayout emailFieldLayout,
                                     TextInputLayout passwordFieldLayout,
                                     Button loginBtn) {
        this.emailTextField = emailTextField;
        this.passwordTextField = passwordTextField;
        this.emailFieldLayout = emailFieldLayout;
        this.passwordFieldLayout = passwordFieldLayout;
        this.loginBtn = loginBtn;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {
        emailField = emailTextField.getText().toString().trim();
        passwordField = passwordTextField.getText().toString().trim();
        emailFieldLayout.setError(null);
        passwordFieldLayout.setError(null);

        if (emailField.isEmpty() || emailField.isBlank()) {
            emailFieldLayout.setError("Email cannot be empty");
        }

        if (passwordField.isEmpty() || passwordField.isBlank()) {
            passwordFieldLayout.setError("Password cannot be empty");
        }
        loginBtn.setEnabled(!emailField.isBlank() && !passwordField.isBlank());
    }

}