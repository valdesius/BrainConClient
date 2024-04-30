package com.example.brainconclient.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationFormValidatorHelper implements TextWatcher {

    private TextInputEditText txtRegFirstName, txtRegLastName, txtRegEmail, txtRegPassword, txtRegConfirm;
    private TextInputLayout txtRegFirstNameLayout, txtRegLastNameLayout, txtRegEmailLayout, txtRegPassLayout, txtRegConfirmLayout;
    private RadioButton studentRadioButton;
    private RadioButton mentorRadioButton;
    private Button registerBtn;


    public RegistrationFormValidatorHelper(TextInputEditText txtRegFirstName,
                                           TextInputEditText txtRegLastName,
                                           TextInputEditText txtRegEmail,
                                           TextInputEditText txtRegPassword,
                                           TextInputEditText txtRegConfirm,
                                           TextInputLayout txtRegFirstNameLayout,
                                           TextInputLayout txtRegLastNameLayout,
                                           TextInputLayout txtRegEmailLayout,
                                           TextInputLayout txtRegPassLayout,
                                           TextInputLayout txtRegConfirmLayout,
                                           Button registerBtn,
                                           RadioButton studentRadioButton,
                                           RadioButton mentorRadioButton) {
        this.txtRegFirstName = txtRegFirstName;
        this.txtRegLastName = txtRegLastName;
        this.txtRegEmail = txtRegEmail;
        this.txtRegPassword = txtRegPassword;
        this.txtRegConfirm = txtRegConfirm;
        this.txtRegFirstNameLayout = txtRegFirstNameLayout;
        this.txtRegLastNameLayout = txtRegLastNameLayout;
        this.txtRegEmailLayout = txtRegEmailLayout;
        this.txtRegPassLayout = txtRegPassLayout;
        this.txtRegConfirmLayout = txtRegConfirmLayout;
        this.registerBtn = registerBtn;
        this.mentorRadioButton = mentorRadioButton;
        this.studentRadioButton = studentRadioButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        txtRegPassLayout.setError(null);
        txtRegConfirmLayout.setError(null);
        String passwordField = txtRegPassword.getText().toString().trim();
        String confirmField = txtRegConfirm.getText().toString().trim();

        if (passwordField.length() < 8) {
            txtRegPassLayout.setError("Password must be 8 characters or more");
        }


        if (confirmField.length() < 8) {
            txtRegPassLayout.setError("Password must be 8 characters or more");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String firstName = txtRegFirstName.getText().toString().trim();
        String lastName = txtRegLastName.getText().toString().trim();
        String email = txtRegEmail.getText().toString().trim();
        String password = txtRegPassword.getText().toString().trim();
        String confirm = txtRegConfirm.getText().toString().trim();

        txtRegFirstNameLayout.setError(null);
        txtRegLastNameLayout.setError(null);
        txtRegEmailLayout.setError(null);
        txtRegPassLayout.setError(null);
        txtRegConfirmLayout.setError(null);

        if (firstName.isBlank() || firstName.isBlank()) {
            txtRegFirstNameLayout.setError("First name cannot be empty!");
        }

        if (lastName.isBlank() || lastName.isBlank()) {
            txtRegLastNameLayout.setError("Last name cannot be empty!");
        }


        if (email.isBlank() || email.isBlank()) {
            txtRegEmailLayout.setError("Email cannot be empty!");
        }


        if (!StringResourceHelper.regexEmailValidationPattern(email)) {
            txtRegEmailLayout.setError("Enter a valid email address");
        }


        if (password.isBlank() || password.isBlank()) {
            txtRegPassLayout.setError("Password cannot be empty!");
        }


        if (confirm.isBlank() || confirm.isBlank()) {
            txtRegConfirmLayout.setError("Confirm cannot be empty!");
        }


        if (!password.equals(confirm)) {
            txtRegPassLayout.setError("Passwords do not match!");
            txtRegConfirmLayout.setError("Passwords do not match!");
            //registerBtn.setEnabled(false);
        }


        boolean isEmailValid = StringResourceHelper.regexEmailValidationPattern(email);
        // RE-ENABLE BUTTON IF ALL FIELDS ARE FILLED AND PASSWORDS MATCH
        registerBtn.setEnabled(!firstName.isBlank() && !lastName.isBlank()
                && !email.isBlank() && isEmailValid && password.equals(confirm));

    }


}

