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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.RedirectHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.utils.MyVolleySingletonUtil;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private RequestQueue requestQueue;
    private TextInputEditText txtProfFirstName, txtProfLastName, txtProfEmail;
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

        profileUpdateBtn = findViewById(R.id.profile_update_btn);

        profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        // END OF PROFILE LOGOUT ON CLICK LISTENER OBJECT.

        setUserDetailsInEditTextFields();

    }
    public void updateProfile() {


        // Получаем значения из полей ввода:
        String firstName = txtProfFirstName.getText().toString().trim();
        String lastName = txtProfLastName.getText().toString().trim();
        String email = txtProfEmail.getText().toString().trim();
        // Пароль не отправляем, так как он не обновляется

        // Создаем новый запрос на обновление профиля:
        StringRequest updateRequest = new StringRequest(Request.Method.PUT, ApiLinksHelper.updateUserApiUri(),
                response -> {
                    // Обработка успешного ответа:
                    Toast.makeText(ProfileActivity.this, "Профиль успешно обновлен", Toast.LENGTH_SHORT).show();
                    // Можно здесь обновить SharedPreferences, если необходимо
                },
                error -> {
                    // Обработка ошибки:
                    Toast.makeText(ProfileActivity.this, "Ошибка обновления профиля", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Добавьте заголовки, если это необходимо, например, токен авторизации:
                headers.put("Authorization", "Bearer " + prefs.getString("token", ""));
                return headers;
            }
        };

        // Добавляем запрос в очередь запросов:
        requestQueue.add(updateRequest);
    }
    // END OF ON CREATE METHOD.

    public void setUserDetailsInEditTextFields() {
        // GET STORED PREFERENCES:
        prefs = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);
        // SET TEXT INPUT FIELDS VALUES:

        int userId = prefs.getInt("user_id", 2); // 2 - это значение по умолчанию, если user_id не найден

        txtProfFirstName.setText(prefs.getString("first_name", ""));
        txtProfLastName.setText(prefs.getString("last_name", ""));
        txtProfEmail.setText(prefs.getString("username", ""));
    }
    // END OF SET USER DETAILS IN EDIT TEXT FIELDS.

    public void logUserOut() {
        clearPreferences();
        goToLogin();
        Toast.makeText(ProfileActivity.this, "Вы вышли с аккаунта", Toast.LENGTH_SHORT).show();
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