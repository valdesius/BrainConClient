package com.example.brainconclient.helper.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginValidationHelper implements TextWatcher {
    private TextInputEditText emailTextField, passwordTextField;
    private TextInputLayout emailFieldLayout, passwordFieldLayout;
    private Button loginBtn;
    private String emailField, passwordField;

    public LoginValidationHelper(TextInputEditText emailTextField,
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
    // END OF ALL ARGS CONSTRUCTOR.

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

        if (emailField.isEmpty() || emailField == null) {
            emailFieldLayout.setError("Email cannot be empty");
        }

// Check password field
        if (passwordField.isEmpty() || passwordField == null) {
            passwordFieldLayout.setError("Password cannot be empty");
        }

// RE-ENABLE BUTTON IF ALL FIELDS ARE FILLED:
        loginBtn.setEnabled(!emailField.isEmpty() && !passwordField.isEmpty());

    }
    // END OF ALL ARGS CONSTRUCTOR.

}
