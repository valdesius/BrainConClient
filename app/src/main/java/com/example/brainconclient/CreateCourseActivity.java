package com.example.brainconclient;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.utils.MyVolleySingletonUtil;

import java.util.HashMap;
import java.util.Map;

public class CreateCourseActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private RequestQueue requestQueue;

    private SharedPreferences preferences;
    private TextView createCourseTitleField, createCourseBodyField;
    private Button createCourseBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Создать курс");
        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        createCourseTitleField = findViewById(R.id.create_course_title_field);
        createCourseBodyField = findViewById(R.id.create_course_body_field);
        createCourseBtn = findViewById(R.id.create_course_btn);

        requestQueue = MyVolleySingletonUtil.getInstance(CreateCourseActivity.this).getRequestQueue();

        createCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCourse();
            }
        });

    }

    public void createCourse() {
        String title = createCourseTitleField.getText().toString();
        String body = createCourseBodyField.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, ApiLinksHelper.createCourseApiUri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CreateCourseActivity", "Created Course!");
                Toast.makeText(CreateCourseActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("CreateCourseActivity", "Ошибка в создании курса");

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("body", body);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);

    }

    public void goToSuccessActivity() {
        Intent goToSuccess = new Intent(CreateCourseActivity.this, SuccessActivity.class);
        startActivity(goToSuccess);
        finish();
    }
}