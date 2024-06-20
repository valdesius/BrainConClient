package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    private Intent intent;
    //private static final String USER_DETAIL_PREF = "USER_INFO";
    private SharedPreferences preferences;
    private RequestQueue mRequestQueue;

    private LoginFormValidationHelper loginValidator;
    private TextView  txtGoToSignIn , txtGoToGuest;

    private TextInputEditText txt_email, txt_password;
    private TextInputLayout txtEmailLayout, txtPasswordLayout;

    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // REMOVE ACTION BAR:
        getSupportActionBar().hide();

        // INITIALIZE REQUEST QUEUE:
        mRequestQueue = MyVolleySingletonUtil.getInstance(LoginActivity.this).getRequestQueue();


        // INITIATE / HOOK VIEW COMPONENTS:
        txt_email        = findViewById(R.id.txt_email);
        txt_password     = findViewById(R.id.txt_password);
        txtGoToSignIn   = findViewById(R.id.txt_go_to_sign_up);
        loginBtn        = findViewById(R.id.login_btn);
        // GET LAYOUTS:
        txtEmailLayout      = findViewById(R.id.txt_email_layout);
        txtPasswordLayout   = findViewById(R.id.txt_password_layout);
        txtGoToGuest = findViewById(R.id.txt_go_to_guest);

        // GET FORM VALIDATOR OBJECT:
        loginValidator = new LoginFormValidationHelper(txt_email, txt_password, txtEmailLayout, txtPasswordLayout, loginBtn);

        // ADD TEXT FIELD LISTENERS:
        txt_email.addTextChangedListener(loginValidator);
        txt_password.addTextChangedListener(loginValidator);

        // PROCESS LOGIN:
        processLogin();

        // REDIRECT TO REGISTER ACTIVITY:
        redirectToRegister();

        txtGoToGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("isGuest", true); // Передаем флаг, указывающий на вход как гость
                startActivity(intent);
                finish();
            }
        });

    }
    // END OF ON CREATE LOGIN ACTIVITY METHOD.


    public void processLogin(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser(txt_email.getText().toString(), txt_password.getText().toString());
                // Toast.makeText(LoginActivity.this, "Login Button Clicked!", Toast.LENGTH_SHORT).show();
            }
            // END OF ONCLICK METHOD.
        });
        // END OF ON LOGIN CLICK LISTENER METHOD.
    }
    // END OF PROCESS LOGIN ACTION BUTTON.

    public void redirectToRegister(){
        txtGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
            // END OF ON CLICK METHOD.
        });
        // END OF GO TO REGISTER ON CLICK LISTENER METHOD.
    }
    // END OF REDIRECT TO REGISTER PAGE ACTION METHOD.

    public void authenticateUser(String email, String password){

        // SET USER DATA MAP OBJECT:
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        JsonObjectRequest request
                = new JsonObjectRequest(Request.Method.POST, ApiLinksHelper.authUserApiUri(), new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                Log.d("LoginActivity", "Response from server: " + response.toString()); // Добавлено логирование ответа
                try {
                    String role = response.getString("role").replace("ROLE_", "");

                    editor.putInt("user_id", response.getInt("userId"));
                    editor.putString("first_name", response.getString("firstName"));
                    editor.putString("last_name", response.getString("lastName"));
                    editor.putString("username", response.getString("username"));
                    editor.putString("role", role);
                    editor.putString("token", response.getString("token"));
                    editor.putBoolean("authenticated", true);
                    editor.apply();

                    switch (role) {

                        case "STUDENT":
                            goToMainStudentIfAuthenticated();
                            Log.e("LoginActivity", "студент");
                            break;
                        case "MENTOR":
                            Log.e("LoginActivity", "МЕНТОР");
                            goToMainIfAuthenticated();
                            break;
                        default:

                            break;
                    }
                } catch (JSONException e) {
                    Log.e("LoginActivity", "Key missing in the response: " + e.getMessage(), e); // Улучшенное логирование ошибки
                }
            }
            // END OF ON RESPONSE METHOD.
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
        // END OF JSON OBJECT REQUEST OBJECT.

        // ADD TO REQUEST QUE:
        mRequestQueue.add(request);
    }
    // END OF AUTHENTICATED USER METHOD.

    public void goToRegister(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
    // END OF GO TO REGISTER ACTIVITY.

    public void goToMainIfAuthenticated(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        // DISPLAY SUCCESS MESSAGE IF AUTHENTICATED:
        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
        finish();
    }
    public void goToMainStudentIfAuthenticated(){
        Intent intent = new Intent(LoginActivity.this, MainActivityStudent.class);
        startActivity(intent);
        // DISPLAY SUCCESS MESSAGE IF AUTHENTICATED:
        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
        finish();
    }
    // END OF GO TO LOGIN INTENT METHOD.

}
// END OF LOGIN ACTIVITY CLASS.