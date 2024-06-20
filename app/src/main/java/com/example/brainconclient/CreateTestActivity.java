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

public class CreateTestActivity extends AppCompatActivity {
    private RequestQueue requestQueue;

    private SharedPreferences preferences;
    private TextView createTestTitleField, createTestBodyField, createTestQuestionField, createTestAnswerField;
    private Button createTestBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        // SET ACTION BAR ATTRIBUTES:


        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        // HOOK / INITIATE VIEW ELEMENTS / COMPONENTS:
        createTestTitleField    = findViewById(R.id.create_test_title_field);
        createTestBodyField     = findViewById(R.id.create_test_body_field);
        createTestQuestionField     = findViewById(R.id.create_test_question_field);
        createTestAnswerField = findViewById(R.id.create_test_answer_field);
        createTestBtn           = findViewById(R.id.create_test_btn);

        // INITIATE REQUEST QUE:
        requestQueue = MyVolleySingletonUtil.getInstance(CreateTestActivity.this).getRequestQueue();

        createTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTest();
            }
        });

    }

    public void createTest(){
        String title = createTestTitleField.getText().toString();
        String body = createTestBodyField.getText().toString();
        String question = createTestQuestionField.getText().toString();
        String answer = createTestAnswerField.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, ApiLinksHelper.createTestApiUri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CreateTestActivity", "Created test!");
                Toast.makeText(CreateTestActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("CreateTestActivity", "Ошибка в создании теста");
                Toast.makeText(CreateTestActivity.this, "Failed to create test", Toast.LENGTH_LONG).show();
            }
            // END OF ON ERROR RESPONSE METHOD.
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("body", body);
                params.put("question", question);
                params.put("answer", answer);
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
}
